package models.router

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter


class RouterStream {
    companion object{
        private val logger = LoggerFactory.getLogger(Router::class.java) as Logger

        public fun readOne(pathToRouter: String, pathToGraphsFolder: String ): Router {

            logger.info(">> readOne : pathToGraphsFolder=$pathToGraphsFolder, pathToRouter=$pathToRouter")

            val router: Router
            val graph: TinkerGraph = TinkerGraph()
            var routerObj : JsonObject? = null;
            var properties = emptyMap<String, Any>()

            if(!File(pathToRouter).exists()){
                logger.error("$pathToRouter not found")
                throw FileNotFoundException("$pathToRouter not found")
            }

            FileReader(pathToRouter).use {
                try {
                    routerObj = Klaxon().parseJsonObject(it);
                }catch (e: ClassCastException){
                    logger.error("Content is not single Object")
                    throw IllegalArgumentException("Content is not single Object")
                } catch (e: KlaxonException){
                    logger.error("Bad file")
                    throw IllegalArgumentException("Bad file")
                }
                if (routerObj == null) {
                    logger.error("cannot parse $pathToRouter")
                    throw NullPointerException("cannot parse $pathToRouter")
                }
                properties = getProperties(routerObj!!);
            }
            val pathToGraph = StringBuilder(pathToGraphsFolder)
                .append("/")
                .append("${properties["id"]}.xml")
                .toString()
                .replace("//", "/")

            if(!File(pathToGraph).exists()){
                logger.error("$pathToGraph not found")
                throw FileNotFoundException("$pathToGraph not found")
            }

            GraphMLReader.inputGraph(graph, pathToGraph);

            logger.info("read graph: OK, " +
                    "V=${graph.vertices.toList().toTypedArray().contentToString()}, " +
                    "E=${graph.edges.toList().toTypedArray().contentToString()}")
            router = Router(properties["id"] as String, graph)
            router.startPointId = properties["startPointId"] as String;
            router.isResetToStart = properties["isResetToStart"] as Boolean;
            logger.info("created router: $router")
            logger.info("<< readOne")
            return router
        }

        public fun readMany(pathToRouter: String, pathToGraphsFolder: String): Array<Router>{
            logger.info(">> readMany : pathToGraphsFolder=$pathToGraphsFolder, pathToRouter=$pathToRouter")

            val routersList = arrayListOf<Router>()
            var propertiesList : ArrayList<Map<String, Any>> = ArrayList();

            if(!File(pathToRouter).exists()){
                logger.error("$pathToRouter not found")
                throw FileNotFoundException("$pathToRouter not found")
            }

            FileReader(pathToRouter).use {
                val routerArray =
                    try {
                         Klaxon().parseJsonArray(it)
                    }catch (e: ClassCastException) {
                        logger.error("Content is not an Array")
                        throw IllegalArgumentException("Content is not an Array")
                    } catch (e: KlaxonException) {
                        logger.error("Bad file")
                        throw IllegalArgumentException("Bad file")
                    }
                routerArray.forEach{
                    propertiesList.add(getProperties(it as JsonObject))
                }
            }
            for (properties in propertiesList) {
                val pathToGraph = StringBuilder(pathToGraphsFolder)
                    .append("/")
                    .append("${properties["id"]}.xml")
                    .toString()
                    .replace("//", "/")

                if(!File(pathToGraph).exists()){
                    throw FileNotFoundException("$pathToGraph not found")
                }
                val graph = TinkerGraph();
                GraphMLReader.inputGraph(graph, pathToGraph);

                logger.info("read graph: OK, " +
                        "V=${graph.vertices.toList().toTypedArray().contentToString()}, " +
                        "E=${graph.edges.toList().toTypedArray().contentToString()}")
                val router = Router(properties["id"] as String, graph)
                router.startPointId = properties["startPointId"] as String;
                router.isResetToStart = properties["isResetToStart"] as Boolean;
                logger.info("created router: $router")
                routersList.add(router);
            }
            logger.info("<< readMany : ${routersList.toTypedArray()}")
            return routersList.toTypedArray()
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
            logger.info("<< write: ${obj.toString()}")
        }

        public fun write(router: Router, pathToFile: String, pathToGraphsFolder: String) {
            logger.info(">> write router=$router, pathToFile=$pathToFile, pathToGraphsFolder=$pathToGraphsFolder")
            val pathGraph = pathToGraphsFolder + "\\${router.id}.xml"
            write(router as Any, pathToFile);
            logger.info(" write a graph to $pathGraph")
            GraphMLWriter.outputGraph(router.graph, pathGraph);
            logger.info("<< write a graph : ok")
        }

        public fun write(router: Array<Router>, pathToFile: String, pathToGraphsFolder: String) {
            logger.info(">> write routerArray=${router.contentToString()}, pathToFile=$pathToFile, pathToGraphsFolder=$pathToGraphsFolder")
            write(router, pathToFile)
            router.forEach {
                val pathGraph = pathToGraphsFolder + "\\${it.id}.xml"
                logger.info(" write a graph to $pathGraph")
                GraphMLWriter.outputGraph(it.graph, pathGraph);
                logger.info(" write a graph : ok")
            }
            logger.info("<< write a graph : ok")
        }
    }
}