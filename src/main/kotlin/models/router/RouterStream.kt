package models.router

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.tinkerpop.blueprints.impls.tg.TinkerGraph

import java.io.*
import java.lang.NullPointerException
import java.lang.StringBuilder

//TODO:  add logger
class RouterStream {
    companion object{
        public fun readOne(pathToGraphsFolder: String, pathToRouter: String): Router {
            val router: Router
            val graph: TinkerGraph;
            var routerObj : JsonObject? = null;
            var properties = emptyMap<String, Any>()

            if(!File(pathToRouter).exists()){
                throw FileNotFoundException("$pathToRouter not found")
            }

            FileReader(pathToRouter).use {
                routerObj = Klaxon().parseJsonObject(it);
                if (routerObj == null) {
                    throw NullPointerException("cannot parse $pathToRouter")
                }
                properties = getProperties(routerObj!!);
            }
            val pathToGraph = StringBuilder(pathToGraphsFolder)
                .append("/")
                .append("graph.${properties["id"]}.xml")
                .toString()

            if(!File(pathToGraph).exists()){
                throw FileNotFoundException("$pathToGraph not found")
            }

            graph = TinkerGraph(pathToGraph, TinkerGraph.FileType.GML);
            router = Router(properties["id"] as String, graph)
            router.startPointId = properties["startPointId"] as String;
            router.isResetToStart = properties["isResetToStart"] as Boolean;

            return router
        }

        private fun readMany(pathToGraphsFolder: String, pathToRouter: String): Array<Router>{
            val routersList = arrayListOf<Router>()
            var propertiesList : ArrayList<Map<String, Any>> = ArrayList();

            if(!File(pathToRouter).exists()){
                throw FileNotFoundException("$pathToRouter not found")
            }

            FileReader(pathToRouter).use {
                val routerArray = Klaxon().parseJsonArray(it)
                if (routerArray == null) {
                    throw NullPointerException("cannot parse $pathToRouter")
                }
                routerArray.forEach{
                    propertiesList.add(getProperties(it as JsonObject))
                }
            }
            for (properties in propertiesList) {
                val pathToGraph = StringBuilder(pathToGraphsFolder)
                    .append("/")
                    .append("graph.${properties["id"]}.xml")
                    .toString()

                if(!File(pathToGraph).exists()){
                    throw FileNotFoundException("$pathToGraph not found")
                }
                val graph = TinkerGraph(pathToGraph, TinkerGraph.FileType.GML);
                val router = Router(properties["id"] as String, graph)
                router.startPointId = properties["startPointId"] as String;
                router.isResetToStart = properties["isResetToStart"] as Boolean;
                routersList.add(router);
            }
            return routersList.toTypedArray();
        }

        private fun getProperties(routerObj: JsonObject) : HashMap<String, Any>{
            val map = hashMapOf<String, Any>()
            val properties = "key isResetToStart startPointId".split(" ");

            val errList = arrayListOf<String>()
            for (field in properties) {
                if(routerObj!![field] == null){
                    errList.add(field);
                }else{
                    map[field] = routerObj[field]!!;
                }
            }
            if(errList.isNotEmpty()){
                throw NullPointerException("cannot parse ${routerObj.toJsonString()}: miss fields: ${errList.toTypedArray().contentToString()}")
            }
            return map;
        }

        private fun write(obj: Any, pathToFile: String){
            val result = Klaxon().toJsonString(obj);
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
        }

        public fun write(router: Router, pathToFile: String) {
           write(router,pathToFile);
        }

        public fun write(router: Array<Router>, pathToFile: String) {
            write(router,pathToFile);
        }
    }
}