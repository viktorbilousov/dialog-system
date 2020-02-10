package models.items.phrase

import models.Answer
import models.items.ADialogItem
import tools.PhrasePrinter
import java.util.*
import kotlin.math.abs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RandomPhrase(id: String, phrases: Array<String>,  answers: Array<Answer> ) : Phrase(id, phrases, answers) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private val random = Random();

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body RANDOM Phrase: input = $inputAnswer")
        val res = PhrasePrinter.printTextDialog(phrases[abs(random.nextInt(phrases.size))], answers)
        logger.info("[$id]<< body RANDOM Phrase: input = $res")
        return res;
    }

    override fun clone(): ADialogItem {
        return RandomPhrase(
            id,
            phrases.clone(),
            this.answers.clone()
        )
    }

}