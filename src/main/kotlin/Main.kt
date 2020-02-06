import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter
import com.tinkerpop.blueprints.util.io.graphml.GraphMLTokens
import models.Answer
import models.AnswerType
import models.items.IDialogItem
import models.items.Router
import models.items.dialog.Dialog
import models.items.phrase.SimplePhrase
import models.items.text.SinglePhraseText
import java.io.FileOutputStream
import java.util.HashMap





class Main {
    companion object{
        @JvmStatic
        public fun main(args: Array<String>) {

            val graph: Graph = TinkerGraph();
            val out = FileOutputStream("examples/testFile.xml");

            // val graph = TinkerGraph()
            val vertexes = HashMap<String, Vertex>();
            for (i in 1..5) {
                vertexes[i.toString()] = graph.addVertex(i.toString());
                vertexes[i.toString()]?.setProperty("vId", "phrase.$i");
            }

            graph.addEdge(null, vertexes["1"], vertexes["2"], "[edge 1-2]")
            graph.addEdge(null, vertexes["2"], vertexes["3"], "[edge 2->3]")
            graph.addEdge(null, vertexes["2"], vertexes["4"], "[edge 2->4]")
            graph.addEdge(null, vertexes["4"], vertexes["5"], "[edge 4->5]")
            graph.addEdge(null, vertexes["3"], vertexes["5"], "[edge 3->5]")

            val writer = GraphMLWriter(graph)
            writer.outputGraph(out)

            val itemsList = ArrayList<IDialogItem>();
            val phrase1  = SimplePhrase("phrase.1", SinglePhraseText(
                "text.phrase.1",
                "im the first phrase",
                arrayOf(Answer("phrase.2" ,"go to 2") )
                )
            )

            val phrase2  = SimplePhrase("phrase.2", SinglePhraseText(
                "text.phrase.2",
                "im the second phrase",
                arrayOf(Answer("phrase.3","go to 3" ), Answer("phrase.4" ,"go to 4"))
            )
            )

            val phrase3  = SimplePhrase("phrase.3", SinglePhraseText(
                "text.phrase.3",
                "im the third phrase",
                arrayOf(Answer("phrase.5","go to 5" ))
            )
            )

            val phrase4  = SimplePhrase("phrase.4", SinglePhraseText(
                "text.phrase.4",
                "im the fourth phrase",
                arrayOf(Answer("phrase.5","go to 5"  ))
            )
            )

            val phrase5  = SimplePhrase("phrase.5", SinglePhraseText(
                "text.phrase.5",
                "im the fifth phrase",
                arrayOf(Answer("exit" ,"exit", AnswerType.EXIT))
            )
            )
            itemsList.add(phrase1);
            itemsList.add(phrase2);
            itemsList.add(phrase3);
            itemsList.add(phrase4);
            itemsList.add(phrase5);

            val router = Router("id.router", graph, itemsList.toTypedArray(),"phrase.1");
            val dialog = Dialog("id.dialog", router);

            dialog.run(Answer("start","Start", AnswerType.ENTER));

            println("Vertices of $graph")
            for (vertex in graph.vertices) {
                println(vertex)
            }
            println("Edges of $graph")
            for (edge in graph.edges) {
                println(edge)
            }

            val a = graph.getVertex("1")
            println("vertex " + a.id + " has name " + a.getProperty("name"))
            for (e in a.getEdges(Direction.OUT)) {
                println(e)
            }
        }
    }
}