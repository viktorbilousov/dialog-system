package models.items

import com.tinkerpop.blueprints.Direction
import models.Indexable;
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Router : Indexable {

    companion object{
        private val logger = LoggerFactory.getLogger(Router::class.java) as Logger
    }

    private val id: String
    private var isResetToStart = false;
    private var currentPoint : IDialogItem? = null;

    public var startPoint : IDialogItem
    get() = startPoint.clone();

    public val graph: Graph
    get

    private val itemsMap = HashMap<String , IDialogItem>()

    public fun getItemsMap () : HashMap<String , IDialogItem> { return this.itemsMap.clone() as HashMap<String , IDialogItem> }


    constructor(id: String, graph: Graph, items : Array<IDialogItem> , startPointId: String){
        this.graph = graph;
        this.id = id;
        items.forEach { itemsMap[it.getId()] = it };
        this.startPoint = get(startPointId);
    }

    constructor(id: String, graph: Graph, items : Array<IDialogItem> , startPointId: String, isResetToStart: Boolean) : this(id, graph, items, startPointId){
        this.isResetToStart = isResetToStart;
    }

    public fun get(answer: Answer) : IDialogItem {
        logger.info("[$id] << intput: $answer")
        val res = get(answer.getId())
        if(currentPoint != null && !isConnected(res, currentPoint!!)){
            throw throw IllegalAccessException("Items '${currentPoint?.getId()}' and '${res.getId()}' not connected");
        }
        currentPoint = res;
        logger.info("[$id] >> output item: ${res.getId()}")
        return res;
    }

    private fun get(id: String) : IDialogItem {
        if(itemsMap[id] == null) {
            throw IllegalAccessException("[${this.id}] Item id='$id' not found")
        };
        return itemsMap[id]!!;
    }

    public fun addItem(item: IDialogItem){
        if(itemsMap[item.getId()] != null){
            logger.warn("$id: Rewrite ${itemsMap[item.getId()]?.getId()} to ${item.getId()}")
        }
        itemsMap[item.getId()] = item;
    }
    // TODO : need to test
    private fun isConnected(source: IDialogItem, dest: IDialogItem) : Boolean{
        for (edge in graph.edges) {
            val v1 = edge.getVertex(Direction.IN).getProperty<String>("vId")
            val v2 = edge.getVertex(Direction.OUT).getProperty<String>("vId")
            if(source.getId() == v1 && dest.getId() == v2 ) return true;
        }
        return false;
    }

    override fun getId(): String {
        return id;
    }

    private fun isAllVerticesFulled(){
        val list = ArrayList<IDialogItem>()
        graph.vertices.forEach {
            if(itemsMap[it.getProperty(Indexable.ID_Property)] == null) {
                list.add(it.getProperty(Indexable.ID_Property))
            }
        }
        if(list.isNotEmpty()){
            throw IllegalAccessException("Vertexes doest have items : ${list.toTypedArray().contentToString()}")
        }
        return;
    }
}