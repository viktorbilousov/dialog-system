package tools

import com.tinkerpop.blueprints.Direction
import models.items.Router

class RouterTester {
    companion object{
        fun test(router : Router): RouterTestClass{
            return RouterTestClass(router);
        }

    }

    class RouterTestClass(private val router : Router){
        private val graph  = router.graph
        private val items  = router.getItemsMap()
        init {
            if(items.isEmpty()) throw IllegalAccessException("items list is empty!")
            if(!graph.vertices.iterator().hasNext()) throw IllegalAccessException("vertices in the graph is empty!")
            if(!graph.edges.iterator().hasNext()) throw IllegalAccessException("edges in the graph is empty!")
            router.startPoint
        }

        public fun isAllVertexHasItems(){
            val list = mutableListOf<String>()
            graph.vertices.forEach{
                if(items[it.id] == null) {
                    list.add(it.id.toString());
                }
            }
            if(list.isNotEmpty()){
                throw IllegalAccessException("vertexes dont have items : ${list.toTypedArray().contentToString()}")
            }
        }

        public fun isAllItemsHasVertex(){
            val list = mutableListOf<String>()


           items.forEach{
               if(graph.getVertex(it.key) == null){
                   list.add(it.key);
               }
           }
            if(list.isNotEmpty()){
                throw IllegalAccessException("Items dont have vertexes : ${list.toTypedArray().contentToString()}")
            }
        }

        public fun isFullFunctional(){

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

        }

        public fun isGraphRelated(){
            val arr = bfs(router.startPoint.getId());
            if(arr.size != graph.vertices.count()){
               val list = mutableListOf<String>()
                 graph.vertices.forEach { if(!arr.contains(it.id as String)) list.add(it.id as String)}
                throw IllegalAccessException("graph not Related!, check vertexes: ${list.toTypedArray().contentToString()}")
            }
        }

        public fun isItemsLinkedCorrectly(){
            val errList = mutableMapOf<String, String>()
            for (item in items.values) {
               item.getAnswers().forEach {
                   if(!isConnected(item.getId(), it.getId())){
                       errList[item.getId()] = it.getId();
                   }
               }
            }
            if(errList.isNotEmpty()){
                throw IllegalAccessException("some items linked not right: ${errList.toList().toTypedArray().contentToString()}")
            }
        }

        private fun isConnected(sourceId: String, desctId:String) : Boolean{
            graph.getVertex(sourceId).getEdges(Direction.OUT).forEach{
                if(it.getVertex(Direction.OUT).id as String == desctId) return true;
            }
            return false;
        }

        private fun bfs(startPointId: String): Array<String>{
            val queue = mutableListOf<String>();
            val passedVertexes = mutableSetOf<String>()
            var currentVertex = graph.getVertex(startPointId).id as String;
            passedVertexes.add(currentVertex)
            queue.add(currentVertex);
            while (queue.isNotEmpty()){
                currentVertex = queue.removeAt(queue.size-1);
                graph.getVertex(currentVertex).getEdges(Direction.OUT).forEach{
                   val vertexId= it.getVertex(Direction.OUT).id as String;
                    if(!passedVertexes.contains(vertexId)){
                        passedVertexes.add(vertexId);
                        queue.add(vertexId);
                    }
                }
            }
            return passedVertexes.toTypedArray();
        }

    }

}