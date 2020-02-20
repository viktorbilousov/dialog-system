package models.items.phrase

import models.Answer
import tools.SimplePhrasePrinter
import java.util.*
import kotlin.math.abs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RandomPhrase(id: String, phrases: Array<String>,  answers: Array<Answer> ) : FilteredPhrase(id, phrases, answers) {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    init {
        this.phrasePrinter = object : PhrasePrinter {
            private val random = Random();
            override fun printTextDialog(text: Array<String>, answer: Array<Answer>): Answer {
                val printer = SimplePhrasePrinter()
                println(printer.createMessage(text[random.nextInt(text.size)], answer));
                return printer.input(answer);
            }
        }
    }
}