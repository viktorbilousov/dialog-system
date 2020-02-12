package models.items.text

import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import models.router.Router
import models.router.RouterStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.lang.ClassCastException
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList

class PhraseTextStream {
    companion object {

        private val logger = LoggerFactory.getLogger(Router::class.java) as Logger

        private fun write(obj: Any, pathToFile: String){
            logger.info(">> write: ${obj} to $pathToFile")
            val result = Klaxon().toJsonString(obj);
            logger.info("json: $result")
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
            logger.info("<< write: ${obj.toString()}")
        }

        public fun write(text: PhraseText, pathToFile: String) {
            write(text as Any, pathToFile);
        }

        public fun write(texts: Array<PhraseText>, pathToFile: String) {
            write(texts as Any, pathToFile);
        }


        @Throws (IllegalArgumentException::class, FileNotFoundException::class )
        public fun readOne(pathToFile: String): PhraseText? {
            logger.info(">> readOne: $pathToFile")
            FileReader(pathToFile).use {
                try {
                    val res = Klaxon().parse<PhraseText>(it.readText());
                    logger.info("<< readOne: $res.id")
                    return res
                } catch (e: ClassCastException){
                    logger.error("Content is not single Object")
                    throw IllegalArgumentException("Content is not single Object")
                } catch (e: KlaxonException){
                    logger.error("Bad file")
                    throw IllegalArgumentException("Bad file")
                }
            }
        }

        @Throws (IllegalArgumentException::class, FileNotFoundException::class )
        public fun readMany(pathToFile: String): Array<PhraseText>? {
            logger.info(">> readMany: $pathToFile")
            FileReader(pathToFile).use {
               try {
                   val res = (Klaxon().parseArray<PhraseText>(it.readText()) as ArrayList<PhraseText>)
                   logger.info("<< readOne: ${res.map { _it-> _it.id }.toTypedArray().contentToString()}")
                   return res.toTypedArray()
               } catch (e: ClassCastException){
                   logger.error("Content is not array")
                   throw IllegalArgumentException("Content is not array")
               }catch (e: KlaxonException){
                   logger.error("Bad file")
                   throw IllegalArgumentException("Bad file")
               }
            }
        }
    }
}