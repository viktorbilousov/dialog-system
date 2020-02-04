import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.GraphFactory
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter
import com.tinkerpop.blueprints.util.io.graphml.GraphMLTokens
import java.io.FileOutputStream
import java.io.OutputStream
import javax.swing.UIManager.put
import java.util.HashMap



class Main {
    companion object{
        @JvmStatic
        public fun main(args: Array<String>){

            val graph : Graph = TinkerGraph();
            val out = FileOutputStream("examples/testFile.xml");

            val vertexKeyTypes = HashMap<String, String>()
            vertexKeyTypes["age"] = GraphMLTokens.INT
            vertexKeyTypes["lang"] = GraphMLTokens.STRING
            vertexKeyTypes["name"] = GraphMLTokens.STRING
            val edgeKeyTypes = HashMap<String, String>()
            edgeKeyTypes["weight"] = GraphMLTokens.FLOAT

            val writer = GraphMLWriter(graph)
            writer.setVertexKeyTypes(vertexKeyTypes)
            writer.setEdgeKeyTypes(edgeKeyTypes)
            writer.outputGraph(out)
        }
    }
}