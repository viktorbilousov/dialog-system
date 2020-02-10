package models.items.text

import com.beust.klaxon.Klaxon
import models.Answer
import models.items.phrase.Phrase
import java.io.FileReader
import java.io.FileWriter

class FPhraseText {
    companion object {
        public fun write(text: Array<PhraseText>, pathToFile: String) {
            val result = Klaxon().toJsonString(text);
            println(result)
            val fw = FileWriter(pathToFile)
            fw.use {
                it.write(result)
            }
        }

        public fun write(phrase: PhraseText, pathToFile: String) {
            val result = Klaxon().toJsonString(phrase);
            println(result)
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

        public fun readMany(pathToFile: String): List<PhraseText>? {
            FileReader(pathToFile).use {
                return Klaxon().parseArray(it.readText())
            }
        }

        public fun toPhrase(pt: PhraseText): Phrase {
            val clazz = Class.forName(pt.clazz);
            clazz.constructors.forEach {
                //id: String, phrases: Array<String>,  answers: Array<Answer>
                if (it.parameterCount == 3
                    && it.parameters[0].type == String::class.java
                    && it.parameters[1].type == Array<String>::class.java
                    && it.parameters[2].type == Array<Answer>::class.java
                ) {
                    return it.newInstance(pt.id, pt.text, pt.answers) as Phrase;
                }
            }
            throw ClassNotFoundException()
        }

    }
}
