package dialog.system.models.router

import com.beust.klaxon.Json
import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.Indexable
import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.ADialogItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class Router : Indexable {

    companion object {
        private val logger = LoggerFactory.getLogger(Router::class.java) as Logger
    }

    private var currentPoint: ADialogItem? = null


    @Json(name = "id")
    override val id: String

    @Json(name = "isResetToStart")
    public var isResetToStart = false

    @Json(ignored = true)
    public var items : HashMap<String, ADialogItem> = hashMapOf()
    private set

    @Json(name = "startPointId")
    public var startPointId: String? = null


    @Json( ignored = true)
    public val startPoint: ADialogItem?
        get() {
            return if(startPointId == null) null
            else items[startPointId!!]
        }

    @Json( ignored = true)
    public val graph: RouterGraph

    constructor(id: String) : this(id, TinkerGraph(), hashMapOf()){ }
    constructor(id: String, graph: Graph) : this(id, graph, hashMapOf()){ }
    constructor(id: String, graph: Graph, items: HashMap<String, ADialogItem>) : this (id, graph, items, null, false)
    constructor(id: String, graph: Graph, items: HashMap<String, ADialogItem>, startPointId: String) : this(id, graph, items, startPointId, false)

    constructor(
        id: String,
        graph: Graph,
        items: HashMap<String, ADialogItem>,
        startPointId: String?,
        isResetToStart: Boolean
    ) {
        this.graph = RouterGraph(graph)
        this.id = id
        this.items = items
        this.startPointId = startPointId
        this.isResetToStart = isResetToStart
    }

    public fun connectToItems(items: HashMap<String, ADialogItem>){
        this.items = items
    }
    public fun addAll(items: Collection<ADialogItem>){
        items.forEach { addItem(it) }
    }

    public fun getNext(answer: Answer): ADialogItem {
        logger.info("[$id] << intput: $answer")
        val res = getItem(answer.id)
        if (currentPoint != null && !graph.isConnected( currentPoint!!.id, res.id)) {
            logger.error("Items '${currentPoint?.id}' and '${res.id}' not connected")
            throw throw IllegalAccessException("Items '${currentPoint?.id}' and '${res.id}' not connected")
        }
        currentPoint = res
        logger.info("[$id] >> output item: ${res.id}")
        return res
    }

    public fun goTo(id: String) : ADialogItem?{
        logger.info("[$this.id] << goTo: $id")
        val res =
            try{
                getItem(id)
            }catch (e: Exception){
                return null
            }
        currentPoint = res
        logger.info("[$id] >> output item: ${res.id}")
        return res
    }

    private fun getItem(id: String): ADialogItem {
        logger.info("[${this.id}] >> get: $id")
        if (items[id] == null) {
            logger.error("${this.id}] Item id='$id' not found")
            throw IllegalAccessException("${this.id}] Item id='$id' not found")
        }
        if(!contains(id)){
            logger.error("router ${this.id} not containd Item id=$id")
            throw IllegalAccessException("router ${this.id} not containd Item id=$id")
        }
        logger.info("[$this.id] << get: return: ${items[id]!!}")
        return items[id]!!
    }

    public fun addItem(item: ADialogItem) {
        logger.info("[${this.id} >> addItem: $item")

        if (items[item.id] != null) {
            logger.warn("$id: Rewrite ${items[item.id]?.id} to ${item.id}")
        }
        items[item.id] = item

        graph.addItem(item)

        logger.info("[$this.id] << addItem: OK")
    }

    public fun removeItem(item: ADialogItem, onlyFromGraph: Boolean = false){
        if(!onlyFromGraph) {
            items.remove(item.id)
        }
        graph.removeItem(item)
    }


    public fun contains(id: String): Boolean{
        return graph.contains(id)  && items[id] != null
    }

}