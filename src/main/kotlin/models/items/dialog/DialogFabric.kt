package models.items.dialog

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import models.items.Router
import java.io.*
import java.lang.NullPointerException
import java.lang.StringBuilder

class DialogFabric {
    companion object{
        public fun create(pathToGraphsFolder: String, pathToRouter: String): Dialog{
            var router: Router? = null
            var graph: TinkerGraph;
            var routerObj : JsonObject? = null;

            if(!File(pathToRouter).exists()){
                throw FileNotFoundException("$pathToRouter not found")
            }

            FileReader(pathToRouter).use {
                routerObj = Klaxon().parseJsonObject(it);
                if (routerObj == null || routerObj!!["id"] == null ) {
                    throw NullPointerException("cant parse $pathToRouter")
                }
            }
            val pathToGraph = StringBuilder(pathToGraphsFolder).append("/").append("graph.${router?.id}.xml").toString()
            if(!File(pathToGraph).exists()){
                throw FileNotFoundException("$pathToGraph not found")
            }

            graph = TinkerGraph(pathToGraph, TinkerGraph.FileType.GML);
            router = Router(routerObj!!["id"] as String , graph )

            return Dialog(router.id, router!!)

        }
    }
}