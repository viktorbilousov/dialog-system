package dialog.system.io

import com.beust.klaxon.*
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter
import dialog.system.models.router.Router
import org.codehaus.jettison.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

class RouterStream {
    companion object{
        private val logger = LoggerFactory.getLogger(RouterStream::class.java) as Logger


        public fun read(pathToRouter: String, pathToGraphsFolder: String): Array<Router>{
            logger.info(">> readMany : pathToGraphsFolder=$pathToGraphsFolder, pathToRouter=$pathToRouter")

            val routersList = arrayListOf<Router>()
            var propertiesList : ArrayList<Map<String, Any>> = ArrayList();

            if(!File(pathToRouter).exists()){
                logger.error("$pathToRouter not found")
                throw FileNotFoundException("$pathToRouter not found")
            }

            FileReader(pathToRouter).use {

                val json : String
                try {
                    json = it.readText();
                }catch (e: FileNotFoundException) {
                    logger.error("File not found")
                    throw e;
                }

                val parser: Parser = Parser.default()

                val obj : Any
                try {
                    obj= parser.parse(json.byteInputStream())
                } catch (e: Exception){
                    logger.error("Error by json parsing: $pathToRouter")
                    throw e;
                }

                val array : JsonArray<*>

                if(obj is JsonObject) {
                    array = JsonArray(obj)
                } else if(obj is JsonArray<*>){
                    array = obj;
                }else{
                    logger.error("Bad file")
                    throw IllegalArgumentException("Bad file")
                }

                array.forEach{propertiesList.add( getProperties( it as JsonObject ))}
            }

            for (properties in propertiesList) {
                val router = readRouterFromProperties(properties, pathToGraphsFolder)
                routersList.add(router);
            }
            logger.info("<< readMany : ${routersList.toTypedArray()}")
            return routersList.toTypedArray()
        }



        private fun readRouterFromProperties(properties: Map<String, Any>, pathToGraphsFolder: String) : Router{
            val pathToGraph =  File(pathToGraphsFolder, "${properties["id"]}.graphml" )

            if(!pathToGraph.exists()){
                logger.warn("$pathToGraph not found")
                throw FileNotFoundException("$pathToGraph not found")
            }
            val graph = TinkerGraph();
            GraphMLReader.inputGraph(graph, pathToGraph.absolutePath);

            logger.info("read graph: OK ${graph}}")
            val router = Router(properties["id"] as String, graph)
            router.startPointId = properties["startPointId"] as String;
            router.isResetToStart = properties["isResetToStart"] as Boolean;
            logger.info("created router: $router")
            return router
        }

        private fun getProperties(routerObj: JsonObject) : HashMap<String, Any>{
            logger.info("<< getProperties")
            val map = hashMapOf<String, Any>()
            val properties = "id isResetToStart startPointId".split(" ");

            val errList = arrayListOf<String>()
            for (field in properties) {
                if(routerObj[field] == null){
                    errList.add(field);
                }else{
                    map[field] = routerObj[field]!!;
                }
            }
            if(errList.isNotEmpty()){
                logger.error("cannot parse ${routerObj.toJsonString()}: miss fields: ${errList.toTypedArray().contentToString()}")
                throw NullPointerException("cannot parse ${routerObj.toJsonString()}: miss fields: ${errList.toTypedArray().contentToString()}")
            }
            logger.info("routers property is read: ${map.values.toList().toTypedArray().contentToString()}")
            logger.info(">> getProperties")
            return map;
        }

        private fun write(obj: Any, pathToFile: String){
            logger.info(">> write: ${obj} to $pathToFile")
            val result = Klaxon().toJsonString(obj);
            logger.info("json: $result")
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
            logger.info("<< write: $obj")
        }

        public fun write(router: Router, pathToFile: String, pathToGraphsFolder: String) {
            return write(arrayOf(router), pathToFile, pathToGraphsFolder)
        }

        public fun write(router: Array<Router>, pathToFile: String, pathToGraphsFolder: String) {
            logger.info(">> write routerArray=${router.contentToString()}, pathToFile=$pathToFile, pathToGraphsFolder=$pathToGraphsFolder")
            write(router, pathToFile)
            router.forEach {
                val pathGraph = File(pathToGraphsFolder , "${it.id}.graphml").absolutePath
                logger.info(" write a graph to $pathGraph")
                GraphMLWriter.outputGraph(it.graph.graph, pathGraph);
                logger.info(" write a graph : ok")
            }
            logger.info("<< write a graph : ok")
        }
    }
}