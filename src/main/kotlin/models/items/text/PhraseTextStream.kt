package models.items.text

import com.beust.klaxon.Klaxon
import java.io.FileReader
import java.io.FileWriter

class PhraseTextStream {
    companion object {
        public fun write(text: Array<PhraseText>, pathToFile: String) {
            val result = Klaxon().toJsonString(text);
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
        }

        public fun write(phrase: PhraseText, pathToFile: String) {
            val result = Klaxon().toJsonString(phrase);
          //  println(result)
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
        }

        public fun readOne(pathToFile: String): PhraseText? {
            FileReader(pathToFile).use {
                return Klaxon().parse<PhraseText>(it.readText())
            }
        }

        public fun readMany(pathToFile: String): Array<PhraseText>? {
            FileReader(pathToFile).use {
                return (Klaxon().parseArray<PhraseText>(it.readText()) as ArrayList<PhraseText>).toTypedArray()
            }
        }
    }
}