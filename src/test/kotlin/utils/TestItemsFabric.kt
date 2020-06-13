package utils

import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.Indexable
import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.dialog.Dialog
import dialog.system.models.items.phrase.SimplePhrase

class TestItemsFabric {

    companion object {

        public fun createDialog(): Dialog {
            val dialog = Dialog("dialog");
            val phrase1 = SimplePhrase(
                "phrase.1", "phrase 1: 2+2*2=?",
                arrayOf(
                    Answer("phrase.2", "answer is 8"),
                    Answer("phrase.3", "answer is 6")
                )
            )
            val phrase2 = SimplePhrase(
                "phrase.2", "false", arrayOf(
                    Answer(
                        "exit",
                        "exit",
                        AnswerType.EXIT
                    )
                )
            )
            val phrase3 = SimplePhrase(
                "phrase.3", "correct", arrayOf(
                    Answer(
                        "exit",
                        "exit",
                        AnswerType.EXIT
                    )
                )
            )

            dialog.addAll(listOf(phrase1, phrase2, phrase3))
            dialog.startDialogItem = phrase1

            return dialog
        }

        public fun createRelatedTestGraph(): Graph {
            val graph: Graph = TinkerGraph();
            val v = hashMapOf<Int, Vertex>()
            for (i in 1..11) {
                v[i] = graph.addVertex(i.toString())
                v[i]!!.setProperty(Indexable.ID_NAME, i.toString())
            }

            graph.addEdge(null, v[11], v[2], "11->2")
            graph.addEdge(null, v[1], v[2], "1->2")
            graph.addEdge(null, v[2], v[3], "2->3")
            graph.addEdge(null, v[2], v[4], "2->4")
            graph.addEdge(null, v[3], v[5], "3->5")
            graph.addEdge(null, v[3], v[6], "3->6")
            graph.addEdge(null, v[4], v[7], "4->7")
            graph.addEdge(null, v[4], v[6], "4->6")
            graph.addEdge(null, v[7], v[8], "7->8")
            graph.addEdge(null, v[5], v[9], "5->9")
            graph.addEdge(null, v[6], v[9], "6->9")
            graph.addEdge(null, v[9], v[10], "9->10")

            return graph;
        }
    }
}