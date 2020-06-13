package dialog.system.models.router

import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import dialog.system.models.Indexable
import java.lang.IllegalArgumentException
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.ADialogItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class RouterGraph(public val graph: Graph) {


    companion object {
        private val logger = LoggerFactory.getLogger(RouterGraph::class.java) as Logger
    }


    constructor() : this(TinkerGraph())

    public fun addAll(itemsId: Collection<ADialogItem>) {
        itemsId.forEach { addItem(it) }
    }

    public fun addItem(item: ADialogItem) {
        val v = graph.addVertex(item.id)
        v.setProperty(Indexable.ID_NAME, item.id)
        item.answers.forEach {
            if (it.type == AnswerType.SIMPLE) connectItems(item.id, it.id)
        }
    }

    public fun removeAll(itemsId: Collection<String>) {
        itemsId.forEach { removeItem(it) }
    }

    public fun removeItem(itemId: String): Vertex? {

        val res: Vertex = getVertexByPropId(itemId) ?: return null
        res.let { graph.removeVertex(it) }

        graph.edges
            .filter {
                it.getVertex(Direction.IN).getProperty<String>(Indexable.ID_NAME) == itemId
                        || it.getVertex(Direction.OUT).getProperty<String>(Indexable.ID_NAME) == itemId
            }
            .forEach { graph.removeEdge(it) }

        return res
    }

    public fun removeItem(item: ADialogItem): Vertex? {
        return removeItem(item.id)
    }

    public fun contains(id: String): Boolean {
        return getVertexByPropId(id) != null
    }

    public fun isConnected(sourceId: String, destId: String): Boolean {
        return getEdge(sourceId, destId) != null
    }

    public fun getEdge(sourceId: String, destId: String): List<Edge>? {
        val res = getVertexByPropId(sourceId)?.getEdges(Direction.OUT)
            ?.filter { it.getVertex(Direction.IN).getProperty(Indexable.ID_NAME) as String == destId }
        if (res != null && res.isEmpty()) return null
        return res
    }


    override fun toString(): String {
        return "{graph=$graph}"
    }


    public fun connectItems(fromId: String, toId: String) {
        val fromV = getVertexByPropId(fromId) ?: throw IllegalArgumentException("$fromId not found")
        val toV = getVertexByPropId(fromId) ?: throw IllegalArgumentException("$toId not found")
        graph.addEdge("$fromId->$toId", fromV, toV, "$fromId->$toId")
    }

    private fun getVertexByPropId(id: String): Vertex? {
        graph.vertices.forEach {
            if (it.getProperty<String>(Indexable.ID_NAME) == id) return it
        }
        return null
    }
}