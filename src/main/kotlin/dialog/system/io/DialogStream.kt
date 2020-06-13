package dialog.system.io

import dialog.system.models.Indexable
import dialog.system.models.items.dialog.Dialog
import dialog.system.models.items.phrase.APhrase
import dialog.system.models.items.text.PhraseText
import dialog.system.models.items.text.PhraseTextFabric
import dialog.system.models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

class DialogStream{
    companion object {

        private val logger = LoggerFactory.getLogger(DialogStream::class.java) as Logger


        private const val routerDirName = "routers"
        private const val graphsDirName = "graphs"
        private const val phrasesDirName = "phrases"


        public fun write(dialog: Dialog, rootDirPath: String) {
            val router = dialog.router

            val routersDir = File(rootDirPath, routerDirName  )
            val graphsDir = File(rootDirPath, graphsDirName )
            val phrasesDir = File(rootDirPath, phrasesDirName )

            if(!routersDir.exists()) routersDir.mkdirs()
            if(!graphsDir.exists()) graphsDir.mkdirs()
            if(!phrasesDir.exists()) phrasesDir.mkdirs()

            val  routerFile = File(routersDir , "${dialog.id}.json")
            RouterStream.write(router, routerFile.absolutePath, graphsDir.absolutePath )

            val arr =  router.items.values.filterIsInstance<APhrase>().map { PhraseText(it)}.toTypedArray()
            val  phrasesFile = File(phrasesDir, "${dialog.id}.json")
            PhraseTextStream.write( arr, phrasesFile.absolutePath)
        }

        public fun write(dialog: Dialog, routerPropertyFilePath: String, grapthDirPath: String, phrasesDir: String) {
            val router = dialog.router

            val routerFile = File(routerPropertyFilePath)
            val graphsDir = File(grapthDirPath)
            val phrasesDir = File(phrasesDir)

            if(!routerFile.exists()) routerFile.createNewFile()
            if(!graphsDir.exists()) graphsDir.mkdirs()
            if(!phrasesDir.exists()) phrasesDir.mkdirs()

            RouterStream.write(router, routerFile.absolutePath, graphsDir.absolutePath )

            val arr =  router.items.values.filterIsInstance<APhrase>().map { PhraseText(it)}.toTypedArray()
            PhraseTextStream.write( arr, File(phrasesDirName , "${dialog.id}.json").absolutePath)
        }

        public fun read(pathToRouters: String, pathToGraphs: String, pathToPhrases: String) : Array<Dialog> {
            logger.info("reading dialog pathToRouter:$pathToRouters , pathToGraphs:$pathToGraphs , pathToPhrases:$pathToPhrases )")

            val phrasesText = arrayListOf<PhraseText>()
            val routers = arrayListOf<Router>()
            Files
                .walk(Paths.get(pathToRouters))
                .filter { Files.isRegularFile(it)}
                .map { File(pathToRouters, it.fileName.toString()).absolutePath }
                .forEach {
                    logger.info("read router $it")
                    routers.addAll(RouterStream.read(it, pathToGraphs))
                }


            Files
                .walk(Paths.get(pathToPhrases))
                .filter { Files.isRegularFile(it)}
                .map { File(pathToPhrases, it.fileName.toString()).absolutePath }
                .forEach {
                    logger.info("read phrase $it")
                    phrasesText.addAll(PhraseTextStream.read(it)!!)
                }

            val phrases = phrasesText.map { PhraseTextFabric.toPhrase(it) }

            routers.forEach { router->
                phrases.filter { router.graph.contains(it.id) }.forEach { router.items[it.id] = it }
            }

            return routers.map { Dialog(it.id, it) }.toTypedArray()
        }

        public fun read(rootDirPath: String) : Array<Dialog> {
            logger.info("reading dialog pathToRouter:$rootDirPath")

            val rootDir = rootDirPath.removeSuffix("//").removeSuffix("/")
            val routersDir = File(rootDir, routerDirName  )
            val graphsDir = File(rootDir, graphsDirName   )
            val phrasesDir = File(rootDir, phrasesDirName )

            if(!routersDir.exists()) throw FileNotFoundException("dir $routersDir not exist")
            if(!graphsDir.exists()) throw FileNotFoundException("dir $graphsDir not exist")
            if(!phrasesDir.exists()) throw FileNotFoundException("dir $phrasesDir exist")

           return read(
               routersDir.absolutePath,
               graphsDir.absolutePath,
               phrasesDir.absolutePath
           )
        }

    }
}