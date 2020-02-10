package tools

import com.tinkerpop.blueprints.Direction
import models.Answer
import models.AnswerType
import models.items.Router
import java.lang.IllegalArgumentException


class RouterTester {
    companion object{
        fun test(router : Router): RouterTestClass{
            return RouterTestClass(router);
        }

    }

    class RouterTestClass(private val router : Router){
        private val graph  = router.graph
        private val items  = router.items
        init {
            if(items.isEmpty()) throw IllegalAccessException("items list is empty!")
            if(!graph.vertices.iterator().hasNext()) throw IllegalAccessException("vertices in the graph is empty!")
            if(!graph.edges.iterator().hasNext()) throw IllegalAccessException("edges in the graph is empty!")
        }

        @Throws(IllegalAccessException::class)
        public fun isAllVertexHasItems() : RouterTestClass{
            val list = mutableListOf<String>()
            graph.vertices.forEach{
                if(items[it.id] == null) {
                    list.add(it.id.toString());
                }
            }
            if(list.isNotEmpty()){
                throw IllegalAccessException("vertexes dont have items : ${list.toTypedArray().contentToString()}")
            }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun checkStartPoint() : RouterTestClass{
           try{
               router.startPoint
           }catch (e: IllegalArgumentException){
               throw IllegalAccessException(e.message);
           }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun isAllItemsHasVertex() : RouterTestClass{
            val list = mutableListOf<String>()


           items.forEach{
               if(graph.getVertex(it.key) == null){
                   list.add(it.key);
               }
           }
            if(list.isNotEmpty()){
                throw IllegalAccessException("Items dont have vertexes : ${list.toTypedArray().contentToString()}")
            }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun isFullFunctional() : RouterTestClass{

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
        public fun isGraphRelated()  : RouterTestClass{
            val arr = bfs(graph.vertices.iterator().next().id as String);
            if(arr.size != graph.vertices.count()){
               val list = mutableListOf<String>()
                 graph.vertices.forEach { if(!arr.contains(it.id as String)) list.add(it.id as String)}
                throw IllegalAccessException("graph not Related!, check vertexes: ${list.toTypedArray().contentToString()}")
            }
            return this
        }

        @Throws(IllegalAccessException::class)
        public fun isItemsLinkedCorrectly()  : RouterTestClass{
            val errList = mutableMapOf<String, String>()
            for (item in items.values) {
               item.getAnswers().forEach {
                   if(!isConnected(item.getId(), it.getId()) && it.type == AnswerType.SIMPLE){
                       errList[item.getId()] = it.getId();
                   }
               }
            }
            if(errList.isNotEmpty()){
                throw IllegalAccessException("some items linked not right: ${errList.toList().toTypedArray().contentToString()}")
            }
            return this
        }

        private fun isConnected(sourceId: String, desctId:String) : Boolean{
            graph.getVertex(sourceId).getEdges(Direction.OUT).forEach{
                if(it.getVertex(Direction.IN).id as String == desctId) return true;
            }
            return false;
        }

        public fun checkTypesOfPhases() {
            val errList_enter = arrayListOf<Answer>()
            val errList_simple = arrayListOf<Answer>()
            val errList_exit = arrayListOf<Answer>()

            graph.vertices.forEach{
                if(items[it.id] != null) {
                    if (it.getVertices(Direction.OUT).count() == 0) {
                        items[it.id]?.getAnswers()?.forEach { ans ->
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
                        items[it.id]?.getAnswers()?.forEach { ans ->
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

    }

}