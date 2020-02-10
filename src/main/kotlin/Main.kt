import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter
import models.Answer
import models.AnswerType
import models.items.ADialogItem
import models.items.Router
import models.items.dialog.Dialog
import models.items.phrase.SimplePhrase
import models.items.text.PhraseText
import java.io.FileOutputStream
import java.util.HashMap


class Main {
    companion object {
        @JvmStatic
        public fun main(args: Array<String>) {


            val mainGraph: Graph = TinkerGraph();
            val graph_3: Graph = TinkerGraph();
            val graph_4: Graph = TinkerGraph();
            val out = FileOutputStream("examples/testFile.xml");


            // val graph = TinkerGraph()
            val vertexes = HashMap<String, Vertex>();
            for (i in 1..5) {
                vertexes[i.toString()] = mainGraph.addVertex(i.toString());
                vertexes[i.toString()]?.setProperty("vId", "phrase.$i");
            }
            for (i in 1..2) {
                vertexes["3.$i"] = mainGraph.addVertex("3.$i");
                vertexes["3.$i"]?.setProperty("vId", "phrase.3.$i");
            }

            for (i in 1..2) {
                vertexes["4.$i"] = mainGraph.addVertex("4.$i");
                vertexes["4.$i"]?.setProperty("vId", "phrase.4.$i");
            }
            vertexes["3->3.1"] = mainGraph.addVertex("3->3.1")
            vertexes["3->3.1"]?.setProperty("vId", "dialog.3");

            vertexes["4->4.1"] = mainGraph.addVertex("4->4.1")
            vertexes["4->4.1"]?.setProperty("vId", "dialog.4");

            vertexes["3.2->3"] = mainGraph.addVertex("3.2->3")
            vertexes["3.2->3"]?.setProperty("vId", "phrase.3");

            vertexes["4.2->1"] = mainGraph.addVertex("4.2->1")
            vertexes["4.2->1"]?.setProperty("vId", "phrase.1");

            mainGraph.addEdge(null, vertexes["1"], vertexes["2"], "[edge 1-2]")
            mainGraph.addEdge(null, vertexes["2"], vertexes["3"], "[edge 2->3]")
            mainGraph.addEdge(null, vertexes["2"], vertexes["4"], "[edge 2->4]")
            mainGraph.addEdge(null, vertexes["4"], vertexes["5"], "[edge 4->5]")
            mainGraph.addEdge(null, vertexes["3"], vertexes["5"], "[edge 3->5]")
            mainGraph.addEdge(null, vertexes["3"], vertexes["3->3.1"], "[edge 3->5]")
            mainGraph.addEdge(null, vertexes["4"], vertexes["4->4.1"], "[edge 3->5]")

            graph_3.addEdge(null, vertexes["3.1"], vertexes["3.2"], "[edge 3.1->3.2]")
            graph_4.addEdge(null, vertexes["4.1"], vertexes["4.2"], "[edge 4.1->4.2]")


            val writer = GraphMLWriter(mainGraph)
            writer.outputGraph(out)


            val itemsList = ArrayList<ADialogItem>();
            val phrase1 = SimplePhrase(
                "phrase.1",
                "im the first phrase",
                arrayOf(Answer("phrase.2", "go to 2"))
            )


            val phrase2 = SimplePhrase(
                "phrase.2",
                "im the second phrase",
                arrayOf(Answer("phrase.3", "go to 3"), Answer("phrase.4", "go to 4"))
            )


            val phrase3 = SimplePhrase(
                "phrase.3",
                "im the third phrase",
                arrayOf(Answer("phrase.5", "go to 5"), Answer("dialog.3", "go to dialog 3", AnswerType.ENTER))
            )


            val phrase4 = SimplePhrase(
                "phrase.4",
                "im the fourth phrase",
                arrayOf(Answer("phrase.5", "go to 5"), Answer("dialog.4", "go to dialog 4", AnswerType.ENTER))
            )


            val phrase5 = SimplePhrase(
                "phrase.5",
                "im the fifth phrase",
                arrayOf(Answer("exit", "exit", AnswerType.EXIT))
            )


            val phrase3_1 = SimplePhrase(
                "phrase.3.1",
                "im the 3.1 phrase",
                arrayOf(Answer("phrase.3.2", "go to 3.2"))
            )


            val phrase3_2 = SimplePhrase(
                "phrase.3.2",
                "im the 3.2 phrase",
                arrayOf(Answer("phrase.3", "exit", AnswerType.EXIT))
            )


            val phrase4_1 = SimplePhrase(
                "phrase.4.1",
                "im the 4.1 phrase",
                arrayOf(Answer("phrase4.2", "go to 4.2"))
            )

            val phrase4_2 = SimplePhrase(
                "phrase.4.2",
                "im the 4.2 phrase",
                arrayOf(Answer("phrase.1", "exit", AnswerType.EXIT))
            )



            itemsList.add(phrase1);
            itemsList.add(phrase2);
            itemsList.add(phrase3);
            itemsList.add(phrase4);
            itemsList.add(phrase5);
            itemsList.add(phrase3_1);
            itemsList.add(phrase3_2);
            itemsList.add(phrase4_1);
            itemsList.add(phrase4_2);

            val routerMain = Router("router", mainGraph, itemsList.toTypedArray(), "phrase.1");
            val router_3 = Router("router.3", mainGraph, itemsList.toTypedArray(), "phrase.3.1");
            val router_4 = Router("router.4", mainGraph, itemsList.toTypedArray(), "phrase.4.1");

            val dialog_3 = Dialog("dialog.3", router_3);
            val dialog_4 = Dialog("dialog.4", router_4);

            val dialog = Dialog("dialog", routerMain);
            dialog.addItem(dialog_3)
            dialog.addItem(dialog_4)

            dialog.run(Answer("start", "Start", AnswerType.ENTER));



            println("Vertices of $mainGraph")
            for (vertex in mainGraph.vertices) {
                println(vertex)
            }
            println("Edges of $mainGraph")
            for (edge in mainGraph.edges) {
                println(edge)
            }

            val a = mainGraph.getVertex("1")
            println("vertex " + a.id + " has name " + a.getProperty("name"))
            for (e in a.getEdges(Direction.OUT)) {
                println(e)
            }
        }
    }
}
