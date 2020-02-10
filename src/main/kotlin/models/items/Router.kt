package models.items

import com.beust.klaxon.Json
import com.tinkerpop.blueprints.Direction
import models.Indexable;
import com.tinkerpop.blueprints.Graph
import models.Answer
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

    public val startPoint: DialogItem
        get() {
            if (startPointId == null) {
                throw IllegalArgumentException("start point is null!")
            }
            return get(startPointId!!)
        }

    public val graph: Graph
        get;



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


    public fun get(answer: Answer): DialogItem {
        logger.info("[$id] << intput: $answer")
        val res = get(answer.id)
        if (currentPoint != null && !isConnected(res, currentPoint!!)) {
            throw throw IllegalAccessException("Items '${currentPoint?.id}' and '${res.id}' not connected");
        }
        currentPoint = res;
        logger.info("[$id] >> output item: ${res.id}")
        return res;
    }

    private fun get(id: String): DialogItem {
        if(items == null){
            throw IllegalAccessException("Items is null!")
        }
        else if (items!![id] == null) {
            throw IllegalAccessException("[${this.id}] Item id='$id' not found")
        };
        else {
            return items!![id]!!;
        }
    }

    public fun addItem(item: DialogItem) {
        if(items == null){
            throw IllegalAccessException("Items is null!")
        }
        else if (items!![item.id] != null) {
            logger.warn("$id: Rewrite ${items!![item.id]?.id} to ${item.id}")
        }
        items!![item.id] = item;
    }

    // TODO : need to test
    private fun isConnected(source: DialogItem, dest: DialogItem): Boolean {
        for (edge in graph.edges) {
            val v1 = edge.getVertex(Direction.IN).getProperty<String>("vId")
            val v2 = edge.getVertex(Direction.OUT).getProperty<String>("vId")
            if (source.id == v1 && dest.id == v2) return true;
        }
        return false;
    }

}