package models.items.phrase

import models.Answer
import tools.PhrasePrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SimplePhrase : Phrase {


    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)


    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body SIMPLE Phrase: input = $inputAnswer")
        val res =  PhrasePrinter.printTextDialog(phrases[0], answers)
        logger.info("[$id]<< body SIMPLE Phrase: output = $res")
        return res;
    }

}