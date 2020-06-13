package dialog.system.io

import com.beust.klaxon.*
import dialog.system.models.items.text.PhraseText
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.lang.ClassCastException
import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList


class PhraseTextStream {
    companion object {

        private val logger = LoggerFactory.getLogger(PhraseTextStream::class.java) as Logger

        private fun write(obj: Any, pathToFile: String){
            logger.info(">> write: ${obj} to $pathToFile")
            val result = Klaxon().toJsonString(obj);
            logger.info("json: $result")
            val file = File(pathToFile);
            if(!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
            logger.info("<< write: $obj")
        }

        public fun write(text: PhraseText, pathToFile: String) {
            write(
                text as Any,
                pathToFile
            );
        }

        public fun write(texts: Array<PhraseText>, pathToFile: String) {
            write(
                texts as Any,
                pathToFile
            );
        }


        @Throws (IllegalArgumentException::class, FileNotFoundException::class )
        public fun read(pathToFile: String): List<PhraseText>? {
            logger.info(">> readMany: $pathToFile")
            var res: List<PhraseText>? = null
            FileReader(pathToFile).use { reader ->

                val json: String
                try {
                    json = reader.readText();
                } catch (e: FileNotFoundException) {
                    logger.error("File not found")
                    throw e;
                }

                val parser: Parser = Parser.default()

                val obj: Any
                try {
                    obj = parser.parse(json.byteInputStream())
                } catch (e: Exception) {
                    logger.error("Error by json parsing: $pathToFile")
                    throw e;
                }

                try {
                    if (obj is JsonObject) {
                        res = listOf(Klaxon().maybeParse<PhraseText>(obj)!!)
                    } else if (obj is JsonArray<*>) {
                        res = (obj)
                            .map { Klaxon().maybeParse<PhraseText>(it as JsonObject) }
                            .filterNotNull()
                        if (res == null || res!!.size != obj.size)
                            throw IllegalArgumentException()

                    } else {
                        throw IllegalArgumentException()
                    }
                } catch (e: Exception) {
                    logger.error("Bad file")
                    throw IllegalArgumentException("Bad file")
                }
            }
            return res;
        }
    }
}