import dialog.system.io.DialogStream
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.TestItemsFabric
import java.io.File

class Test_DialogStream {

    @Test fun read_one(){
        val pathToInput = "./src/test/resources/dialog_actual"


        val expectedDialog = TestItemsFabric.createDialog()
        val actualDialogs = DialogStream.read(pathToInput)

        Assertions.assertEquals(actualDialogs.size, 1)

        val actualDialog = actualDialogs[0];

        Assertions.assertEquals(actualDialog.router.items, expectedDialog.router.items)
        Assertions.assertEquals(actualDialog.router.graph.graph.vertices, expectedDialog.router.graph.graph.vertices)
        Assertions.assertEquals(actualDialog.router.graph.graph.edges, expectedDialog.router.graph.graph.edges)
    }

    @Test fun write_to_files(){

        val pathToOutput = "./src/test/resources/dialog_actual"
        val pathToInput = "./src/test/resources/dialog_expected"

        val routerFile = File(File(pathToInput, "routers"), "dialog.json" )
        val graphsDir = File(pathToInput, "graphs")
        val phrasesDir = File(pathToInput, "phrases")


        File(pathToOutput).delete()
        val dialogStream = TestItemsFabric.createDialog()
        DialogStream.write(dialogStream, routerFile.absolutePath, graphsDir.absolutePath, phrasesDir.absolutePath)


        val expectedFiles = readFiles(File(pathToInput))
        val actualFiles = readFiles(File(pathToOutput))

        actualFiles.forEach{ Assertions.assertNotNull(it) }

        Assertions.assertEquals(expectedFiles["router"], actualFiles["router"])
        Assertions.assertEquals(expectedFiles["graph"], actualFiles["graph"])
        Assertions.assertEquals(expectedFiles["phrase"], actualFiles["phrase"])

    }

    @Test fun write_to_dir(){

        val pathToOutput = "./src/test/resources/dialog_actual"
        val pathToInput = "./src/test/resources/dialog_expected"
        File(pathToOutput).delete()
        val dialogStream = TestItemsFabric.createDialog()
        DialogStream.write(dialogStream, pathToOutput)

        val expectedFiles = readFiles(File(pathToInput))
        val actualFiles = readFiles(File(pathToOutput))

        actualFiles.forEach{ Assertions.assertNotNull(it) }

        Assertions.assertEquals(expectedFiles["router"], actualFiles["router"])
        Assertions.assertEquals(expectedFiles["graph"], actualFiles["graph"])
        Assertions.assertEquals(expectedFiles["phrase"], actualFiles["phrase"])

    }

    private fun readFiles(dirFile: File): Map<String, String?>{
        val routerFile = File(File(dirFile, "routers"), "dialog.json" )
        val graphFile = File(File(dirFile, "graphs"), "dialog.graphml" )
        val phraseFile = File(File(dirFile, "phrases"), "dialog.json" )
        return readFiles(routerFile, phraseFile, graphFile)
    }

    private fun readFiles(routerFile: File, phraseFile : File, graphFile: File): Map<String, String?>{
        val res = mutableMapOf<String, String?>()
        res["router"] = readTextFromFile(routerFile)
        res["phrase"] = readTextFromFile(phraseFile)
        res["graph"] = readTextFromFile(graphFile)
        return res;
    }

    private fun readTextFromFile(file: File) : String?{
        if(file.exists() && file.isFile){
            return file.readText()
        }
        return null
    }




}