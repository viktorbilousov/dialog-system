package models.items

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

    override val id: String
    private var isResetToStart = false;
    private var currentPoint: ADialogItem? = null;
    public var startPointId: String? = null
        get
        set(value) {
            if(value == null) {
                throw IllegalArgumentException("id can not be null!")
            }
            field = value
        };

    public val startPoint: ADialogItem
        get() {
            if (startPointId == null) {
                throw IllegalArgumentException("start point is null!")
            }
            return get(startPointId!!)
        }
    public val graph: Graph
        get;

    private val itemsMap = HashMap<String, ADialogItem>()


    public val items: HashMap<String, ADialogItem>
        get() = itemsMap.clone() as HashMap<String, ADialogItem>;


    constructor(id: String, graph: Graph, items: Array<ADialogItem>) {
        this.graph = graph;
        this.id = id;
        items.forEach {
            this.itemsMap[it.id] = it
        };
    }

    constructor(id: String, graph: Graph, items: Array<ADialogItem>, startPointId: String) {
        this.graph = graph;
        this.id = id;
        items.forEach {
            this.itemsMap[it.id] = it
        };
        this.startPointId = startPointId;
    }

    constructor(
        id: String,
        graph: Graph,
        items: Array<ADialogItem>,
        startPointId: String,
        isResetToStart: Boolean
    ) : this(id, graph, items, startPointId) {
        this.isResetToStart = isResetToStart;
    }

    public fun get(answer: Answer): ADialogItem {
        logger.info("[$id] << intput: $answer")
        val res = get(answer.id)
        if (currentPoint != null && !isConnected(res, currentPoint!!)) {
            throw throw IllegalAccessException("Items '${currentPoint?.id}' and '${res.id}' not connected");
        }
        currentPoint = res;
        logger.info("[$id] >> output item: ${res.id}")
        return res;
    }

    private fun get(id: String): ADialogItem {
        if (itemsMap[id] == null) {
            throw IllegalAccessException("[${this.id}] Item id='$id' not found")
        };
        return itemsMap[id]!!;
    }

    public fun addItem(item: ADialogItem) {
        if (itemsMap[item.id] != null) {
            logger.warn("$id: Rewrite ${itemsMap[item.id]?.id} to ${item.id}")
        }
        itemsMap[item.id] = item;
    }

    // TODO : need to test
    private fun isConnected(source: ADialogItem, dest: ADialogItem): Boolean {
        for (edge in graph.edges) {
            val v1 = edge.getVertex(Direction.IN).getProperty<String>("vId")
            val v2 = edge.getVertex(Direction.OUT).getProperty<String>("vId")
            if (source.id == v1 && dest.id == v2) return true;
        }
        return false;
    }

    private fun isAllVerticesFulled() {
        val list = ArrayList<String>()
        graph.vertices.forEach {
            if (itemsMap[it.id] == null) {
                list.add(it.id as String)
            }
        }
        if (list.isNotEmpty()) {
            throw IllegalAccessException("Vertexes doest have items : ${list.toTypedArray().contentToString()}")
        }
        return;
    }
}