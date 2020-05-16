package dialog.system.tools

import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.Vertex
import dialog.system.models.Answer
import dialog.system.models.AnswerType
import dialog.system.models.Indexable
import dialog.system.models.items.ADialogItem
import dialog.system.models.router.Router
import java.lang.IllegalArgumentException


class RouterTester {
    companion object{
        fun test(router : Router): RouterTestClass {
            return RouterTestClass(router);
        }

    }

    class RouterTestClass(private val router : Router){
        private val graph  = router.graph
        private val items : HashMap<String, ADialogItem>
        private val vertexMap = HashMap<String, Vertex>()
        init {
            if(router.items == null ) throw IllegalAccessException("items list is null!")
            this.items = router.items!!
            if(items.isEmpty()) throw IllegalAccessException("items list is empty!")
            if(!graph.vertices.iterator().hasNext()) throw IllegalAccessException("vertices in the graph is empty!")
           // if(!graph.edges.iterator().hasNext()) throw IllegalAccessException("edges in the graph is empty!")

            graph.vertices.forEach{
                vertexMap[it.getProperty(Indexable.ID_NAME)] = it;
            }
        }

        @Throws(IllegalAccessException::class)
        public fun isAllVertexHasItems() : RouterTestClass {
            val list = mutableListOf<String>()
            graph.vertices.forEach{
                val id = it.getProperty<String>(Indexable.ID_NAME);
                if(items[id] == null) {
                    list.add("id:${it.id} item:$id")
                }
            }
            if(list.isNotEmpty()){
                throw IllegalAccessException("vertexes dont have items : ${list.toTypedArray().contentToString()}")
            }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun checkStartPoint() : RouterTestClass {
           try{
               router.startPoint
           }catch (e: IllegalArgumentException){
               throw IllegalAccessException(e.message);
           }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun isAllItemsHasVertex() : RouterTestClass {
            val list = mutableListOf<String>()

           items.forEach{
               if(vertexMap[it.key] == null){
                   list.add(it.key);
               }
           }
            if(list.isNotEmpty()){
                throw IllegalAccessException("Items dont have vertexes : ${list.toTypedArray().contentToString()}")
            }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun isFullFunctional() : RouterTestClass {

            var exceptionText = "";

            try {
                isAllItemsHasVertex()
            }catch (e: IllegalAccessException){
                exceptionText += e.message
            }

            try {
                isAllVertexHasItems()
            }catch (e: IllegalAccessException){
                if(exceptionText.isNotEmpty()){
                    exceptionText += " && "
                }
                exceptionText += e.message;
            }

            if(exceptionText.isNotEmpty()){
                throw IllegalAccessException(exceptionText);
            }
            return this

        }

        @Throws(IllegalAccessException::class)
        public fun isGraphRelated()  : RouterTestClass {
            val startPointVetex = findVertex(router.startPointId!!) ?:
                throw IllegalArgumentException("router not contain start Point ${router.startPointId}")
            val arr = bfs(startPointVetex.id as String);
            if(arr.size != graph.vertices.count()){
               val list = mutableListOf<String>()
                 graph.vertices.forEach {
                     if(!arr.contains(it.id as String)) {
                         list.add(it.getProperty(Indexable.ID_NAME) as String)
                     }
                 }
                throw IllegalAccessException("graph not Related!, check vertexes: ${list.toTypedArray().contentToString()}")
            }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun isItemsLinkedCorrectly()  : RouterTestClass {
            val errList = hashMapOf<String, String>()
            for (item in items.values) {
                if(vertexMap[item.id] == null) {
                    continue
                };
               item.answers.forEach {
                   if(!isConnected(item.id, it.id) && it.type == AnswerType.SIMPLE){
                       errList[item.id] = it.id;
                   }
               }
            }
            if(errList.isNotEmpty()){
                throw IllegalAccessException("some items linked not right: ${errList.toList().toTypedArray().contentToString()}")
            }
            return this
        }

        private fun isConnected(sourceId: String, destId:String) : Boolean{
            vertexMap[sourceId]?.getEdges(Direction.OUT)?.forEach{
                if(it.getVertex(Direction.IN).getProperty(Indexable.ID_NAME) as String == destId) return true;
            }
            return false;
        }

        public fun checkTypesOfPhases() : RouterTestClass {
            val errList_enter = arrayListOf<Answer>()
            val errList_simple = arrayListOf<Answer>()
            val errList_exit = arrayListOf<Answer>()

            graph.vertices.forEach{
                if(items[it.id] != null) {
                    if (it.getVertices(Direction.OUT).count() == 0) {
                        items[it.id]?.answers?.forEach { ans ->
                            if (ans.type != AnswerType.EXIT) {
                                errList_exit.add(ans);
                            }
                        }
                   /* } else if (it.getVertices(Direction.IN).count() == 0) {
                        items[it.id]?.getAnswers()?.forEach { ans ->
                            if (ans.type != AnswerType.ENTER) {
                                errList_enter.add(ans);
                            }
                        }*/
                    } else{
                        items[it.id]?.answers?.forEach { ans ->
                            if (ans.type != AnswerType.SIMPLE) {
                                errList_simple.add(ans);
                            }
                        }
                    }
                }
            }
            if(errList_enter.size + errList_simple.size + errList_exit.size != 0){
                throw IllegalAccessException(
                    "some Answers don`t have right type: \n" +
                            "must ENTER: ${errList_enter.toTypedArray().contentToString()} \n" +
                            "must SIMPLE: ${errList_simple.toTypedArray().contentToString()} \n" +
                            "must EXIT: ${errList_exit.toTypedArray().contentToString()}"
                )
            }
            return this;
        }

        private fun bfs(startPointId: String): Array<String>{
            val queue = mutableListOf<String>();
            val passedVertexes = mutableSetOf<String>()
            var currentVertex = graph.getVertex(startPointId).id as String;
            passedVertexes.add(currentVertex)
            queue.add(currentVertex);
            while (queue.isNotEmpty()){
                currentVertex = queue.removeAt(queue.size-1);
                graph.getVertex(currentVertex).getEdges(Direction.BOTH).forEach{
                    for(i in 0..1) {
                        var vertexId = it.getVertex(Direction.IN).id as String;
                        if(i == 1){
                            vertexId = it.getVertex(Direction.OUT).id as String;
                        }
                        if (!passedVertexes.contains(vertexId)) {
                            passedVertexes.add(vertexId);
                            queue.add(vertexId);
                        }
                    }
                }
            }
            return passedVertexes.toTypedArray();
        }

        private fun findVertex(item_id: String) : Vertex?{
            graph.vertices.forEach{
                if(it.getProperty<String>(Indexable.ID_NAME) == item_id) return it
            }
            return null
        }

    }

}