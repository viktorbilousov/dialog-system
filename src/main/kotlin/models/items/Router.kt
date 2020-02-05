package models.items

import models.Indexable;
import com.tinkerpop.blueprints.Graph
import models.Answer;

class Router(private val id: String, private val graph: Graph, private val startPoint: String) : Indexable {

    private var isResetToStart = false;
    private var lastPoint : IDialogItem? = null
    get

    public fun get(answer: Answer) : IDialogItem {
        return get(answer.getId())
    }

    private fun get(id: String) : IDialogItem {
        //return graph.getEdge(id);
    }

    override fun getId(): String {
        return id;
    }
}