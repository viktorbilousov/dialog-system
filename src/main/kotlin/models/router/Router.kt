package models.router

import com.beust.klaxon.Json
import com.tinkerpop.blueprints.Direction
import models.Indexable;
import com.tinkerpop.blueprints.Graph
import models.Answer
import models.items.DialogItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class Router : Indexable {

    companion object {
        private val logger = LoggerFactory.getLogger(Router::class.java) as Logger
    }

    private var currentPoint: DialogItem? = null


    @Json(name = "id")
    override val id: String

    @Json(name = "isResetToStart")
    public var isResetToStart = false

    @Json(ignored = true)
    public var items : HashMap<String, DialogItem>? = null
    set(value) {
        if(value == null) {
            throw IllegalArgumentException("items can not set items as null")
        }
        field = value;
    }

    @Json(name = "startPointId")
    public var startPointId: String? = null
        get
        set(value) {
            if(value == null) {
                throw IllegalArgumentException("id can not be null!")
            }
            field = value
        };

    @Json( ignored = true)
    public val startPoint: DialogItem
        get() {
            if (startPointId == null) {
                throw IllegalArgumentException("start point is null!")
            }
            return get(startPointId!!)
        }

    @Json( ignored = true)
    public val graph: Graph
        get



    constructor(id: String, graph: Graph) {
        this.graph = graph
        this.id = id
    }

    constructor(id: String, graph: Graph, items: HashMap<String, DialogItem>) {
        this.graph = graph
        this.id = id
        this.items = items
    }

    constructor(id: String, graph: Graph, items: HashMap<String, DialogItem>, startPointId: String) {
        this.graph = graph
        this.id = id
        this.items = items
        this.startPointId = startPointId
    }

    constructor(
        id: String,
        graph: Graph,
        items: HashMap<String, DialogItem>,
        startPointId: String,
        isResetToStart: Boolean
    ) : this(id, graph, items, startPointId) {
        this.isResetToStart = isResetToStart;
    }


    public fun getNext(answer: Answer): DialogItem {
        logger.info("[$id] << intput: $answer")
        val res = get(answer.id)
        if (currentPoint != null && !isConnected(res, currentPoint!!)) {
            throw throw IllegalAccessException("Items '${currentPoint?.id}' and '${res.id}' not connected");
        }
        currentPoint = res;
        logger.info("[$id] >> output item: ${res.id}")
        return res;
    }

    public fun goTo(id: String) : DialogItem?{
        logger.info("[$this.id] << goTo: $id")
        if(items == null){
            throw IllegalAccessException("Items is null!")
        }

        var res : DialogItem? = null;
        if(items!![id] != null && graph.getVertex(id)!= null) {
            res = items!![id];
            currentPoint = res
        }
        logger.info("[$id] >> output item: ${res?.id}")
        return res;


    }

    private fun get(id: String): DialogItem {
        logger.info("[$this.id] >> get: $id")
        if(items == null){
            logger.error("Items is null!")
            throw IllegalAccessException("Items is null!")
        }
        else if (items!![id] == null) {
            logger.error("${this.id}] Item id='$id' not found")
            throw IllegalAccessException("[${this.id}] Item id='$id' not found")
        };
        else {
            logger.info("[$this.id] << get: return: ${items!![id]!!}")
            return items!![id]!!
        }
    }

    public fun addItem(item: DialogItem) {
        logger.info("[$this.id] >> addItem: $item")

        if(items == null){
            logger.error("Items is null!")
            throw IllegalAccessException("Items is null!")
        }
        else if (items!![item.id] != null) {
            logger.warn("$id: Rewrite ${items!![item.id]?.id} to ${item.id}")
        }
        items!![item.id] = item;
        logger.info("[$this.id] << addItem: OK")
    }

    public fun contains(id: String): Boolean{
        return this.graph.getEdge(id) != null  && items!![id] != null;
    }

    private fun isConnected(source: DialogItem, dest: DialogItem): Boolean {
        for (edge in graph.edges) {
            val v1 = edge.getVertex(Direction.IN).getProperty<String>("vId")
            val v2 = edge.getVertex(Direction.OUT).getProperty<String>("vId")
            if (source.id == v1 && dest.id == v2) return true;
        }
        return false;
    }

    override fun toString(): String {
        return "{id=$id, startPointId=$startPointId, graph=$graph}"
    }


}