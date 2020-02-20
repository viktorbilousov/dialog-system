package models.items.phrase

import models.Answer
import tools.SimplePhrasePrinter
import java.util.*
import kotlin.math.abs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RandomPhrase(id: String, phrases: Array<String>,  answers: Array<Answer> ) : FilteredPhrase(id, phrases, answers) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private val random = Random();

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body RANDOM Phrase: input = $inputAnswer")
        val res = phrasePrinter.printTextDialog(arrayOf(phrases[abs(random.nextInt(phrases.size))]), answers)
        logger.info("[$id]<< body RANDOM Phrase: input = $res")
        return res;
    }


}