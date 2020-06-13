import com.beust.klaxon.KlaxonException
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.Indexable
import dialog.system.models.items.ADialogItem
import phrases.EmptyPhrase
import dialog.system.models.router.Router
import dialog.system.io.RouterStream
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException

//TODO finalise tests
class Test_RoterStream{
    @Test fun readManyFromFile() {
        val pathToFile = File("src/test/resources/routers/multiplyRouter_test.json").absolutePath
        val pathToGraphFolder = File("src/test/resources/graphs/test").absolutePath
        val actualRouter = RouterStream.read(pathToFile, pathToGraphFolder)
        println(actualRouter.contentToString())
    }
    @Test fun readOneFromFile() {
        val pathToFile = File("src/test/resources/routers/singleRouter_test.json").absolutePath
        val pathToGraphFolder = File("src/test/resources/graphs/test").absolutePath
        val actualRouter = RouterStream.read(pathToFile, pathToGraphFolder)
      //  println(actualRouter)
    }

    @Test fun readManyFromFile_notFound() {
        val pathToFile = "./src/test/resources/routers/singleRouter_test123.json"
        val pathToGraphFolder = "./src/test/resources/graphs/test"
        assertThrows<FileNotFoundException>{ RouterStream.read(pathToFile, pathToGraphFolder)}
    }
    @Test fun readOneFromFile_notFound()  {
        val pathToFile = "./src/test/resources/routers/singleRouter_test123.json"
        val pathToGraphFolder = "./src/test/resources/graphs/test"
        assertThrows<FileNotFoundException>{ RouterStream.read(pathToFile, pathToGraphFolder)}
    }
    @Test fun readOneFromFile_notFound_routers()  {
        val pathToFile = "./src/test/resources/routers/singleRouter_test.json"
        val pathToGraphFolder = "./src/test/resources"
        assertThrows<FileNotFoundException>{ RouterStream.read(pathToFile, pathToGraphFolder)}
    }

    @Test fun readManyFromFile_notFound_routers()  {
        val pathToFile = "./src/test/resources/routers/multiplyRouter_test.json"
        val pathToGraphFolder = "./src/test/resources"
        assertThrows<FileNotFoundException>{ RouterStream.read(pathToFile, pathToGraphFolder)}
    }

    @Test fun readManyFromFile_badFile() {
        val pathToFile = "./src/test/resources/routers/singleRouter_test_bad.json"
        val pathToGraphFolder = "./src/test/resources/graphs/test"
        assertThrows<KlaxonException>{ RouterStream.read(pathToFile, pathToGraphFolder)}
    }
    @Test fun readOneFromFile_badFile() {
        val pathToFile = "./src/test/resources/routers/multiplyRouter_test_bad.json"
        val pathToGraphFolder = "./src/test/resources/graphs/test"
        assertThrows<KlaxonException>{ RouterStream.read(pathToFile, pathToGraphFolder)}
    }

    @Test fun writeManyToFile(){
        val pathToFile = "./src/test/resources/routers/writeMultiptyRouter.json"
        val pathToGraphFolder = "./src/test/resources/graphs"
        val expectedRouters = arrayListOf<Router>()
        for(i in 1..10) {
           expectedRouters.add(createTestRouter("test.router.$i"))
        }
        RouterStream.write(expectedRouters.toTypedArray(), pathToFile, pathToGraphFolder)
    }

    @Test fun writeOneToFile(){
        val pathToFile = "./src/test/resources/routers/writeSingleRouter.json"
        val pathToGraphFolder = "./src/test/resources/graphs"
        val expectedRouter = createTestRouter("test.router");
        RouterStream.write(expectedRouter, pathToFile, pathToGraphFolder)
    }

    private fun createTestRouter(id: String) : Router {
        return Router(
            id,
            createTestGraph(),
            createTestItems(),
            "1",
            false
        )
    }

    private fun createTestItems(): HashMap<String, ADialogItem>{
        val items = hashMapOf<String, ADialogItem>()
        for(i in 1 .. 10){
            items["$i"] = EmptyPhrase("$i")
        }
        return items;
    }

    private fun createTestGraph(): Graph{
        val graph = TinkerGraph();
        val v = hashMapOf<Int, Vertex>()
        for(i in 1 .. 10){
            v[i] = graph.addVertex("id$i")
            v[i]!!.setProperty(Indexable.ID_NAME, "$i")
        }
        graph.addEdge(null, v[1], v[2], "1->2");
        graph.addEdge(null, v[2], v[3], "2->3");
        graph.addEdge(null, v[2], v[4], "2->4");
        graph.addEdge(null, v[4], v[5], "3->5");
        graph.addEdge(null, v[3], v[5], "4->5");
        graph.addEdge(null, v[5], v[6], "5->6");
        graph.addEdge(null, v[6], v[7], "6->7");
        graph.addEdge(null, v[7], v[8], "7->8");
        graph.addEdge(null, v[7], v[9], "7->9");
        graph.addEdge(null, v[9], v[10], "9->10");
        return graph;
    }
}