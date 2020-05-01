package dialog.system.models.router

import com.beust.klaxon.Json
import com.tinkerpop.blueprints.Direction
import dialog.system.models.Indexable;
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import dialog.system.models.Answer
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
    public var items : HashMap<String, ADialogItem>? = null
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
    public val startPoint: ADialogItem
        get() {
            if (startPointId == null) {
                throw IllegalArgumentException("start point is null!")
            }
            return getItem(startPointId!!)
        }

    @Json( ignored = true)
    public val graph: Graph
        get



    constructor(id: String, graph: Graph) {
        this.graph = graph
        this.id = id
    }

    constructor(id: String, graph: Graph, items: HashMap<String, ADialogItem>?) {
        this.graph = graph
        this.id = id
        this.items = items
    }

    constructor(id: String, graph: Graph, items: HashMap<String, ADialogItem>?, startPointId: String) {
        this.graph = graph
        this.id = id
        this.items = items
        this.startPointId = startPointId
    }

    constructor(
        id: String,
        graph: Graph,
        items: HashMap<String, ADialogItem>?,
        startPointId: String,
        isResetToStart: Boolean
    ) : this(id, graph, items, startPointId) {
        this.isResetToStart = isResetToStart;
    }


    public fun getNext(answer: Answer): ADialogItem {
        logger.info("[$id] << intput: $answer")
        val res = getItem(answer.id)
        if (currentPoint != null && !isConnected( currentPoint!!.id, res.id)) {
            logger.error("Items '${currentPoint?.id}' and '${res.id}' not connected")
            throw throw IllegalAccessException("Items '${currentPoint?.id}' and '${res.id}' not connected");
        }
        currentPoint = res;
        logger.info("[$id] >> output item: ${res.id}")
        return res;
    }

    public fun goTo(id: String) : ADialogItem?{
        logger.info("[$this.id] << goTo: $id")
        if(items == null){
            throw IllegalAccessException("Item is null!")
        }
        val res =
            try{
                getItem(id);
            }catch (e: Exception){
                return null;
            }
        currentPoint = res
        logger.info("[$id] >> output item: ${res.id}")
        return res;
    }

    private fun getItem(id: String): ADialogItem {
        logger.info("[${this.id}] >> get: $id")
        if(items == null){
            logger.error("Items is null!")
            throw IllegalAccessException("Items is null!")
        }
        if (items!![id] == null) {
            logger.error("${this.id}] Item id='$id' not found")
            throw IllegalAccessException("${this.id}] Item id='$id' not found")
        }
        if(!contains(id)){
            logger.error("router ${this.id} not containd Item id=$id")
            throw IllegalAccessException("router ${this.id} not containd Item id=$id")
        }
        logger.info("[$this.id] << get: return: ${items!![id]!!}")
        return items!![id]!!
    }

    public fun addItem(item: ADialogItem) {
        logger.info("[${this.id} >> addItem: $item")

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
        return getVertex(id) != null  && items!![id] != null;
    }

    private fun getVertex(itemId: String): Vertex?{
        for (vertex in graph.vertices) {
            if(vertex.getProperty<String>(Indexable.ID_NAME) == itemId){
                return vertex;
            }
        }
        logger.warn("vertex of item $itemId not found")
        return null;
    }

   /* private fun isConnected(source: DialogItem, dest: DialogItem): Boolean {
        for (edge in graph.edges) {
            val v1 = edge.getVertex(Direction.OUT).getProperty<String>(Indexable.ID_NAME)
            val v2 = edge.getVertex(Direction.IN).getProperty<String>(Indexable.ID_NAME)
            if (source.id == v1 && dest.id == v2) return true;
        }
        return false;
    }*/

    private fun isConnected(sourceId: String, destId:String) : Boolean {
        getVertex(sourceId)?.getEdges(Direction.OUT)?.forEach{
            if(it.getVertex(Direction.IN).getProperty(Indexable.ID_NAME) as String == destId) return true;
        }
        return false;
    }

    override fun toString(): String {
        return "{id=$id, startPointId=$startPointId, graph=$graph}"
    }


}