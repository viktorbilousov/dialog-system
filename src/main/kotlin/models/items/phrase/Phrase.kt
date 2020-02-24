package models.items.phrase

import models.Answer
import models.items.DialogItem
import models.items.text.PhraseText
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.AnswersTool
import tools.ConsoleAnswerChooser
import tools.FirstPhraseChooser
import tools.SimplePhrasePrinter

abstract class Phrase : DialogItem  {
    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    public var texts: PhraseText
    private set

    final override val id: String

    public var phrasePrinter : PhrasePrinter  = SimplePhrasePrinter()
    public var phraseChooser : PhraseChooser  = FirstPhraseChooser()
    public var answerChooser : AnswerChooser  = ConsoleAnswerChooser()

    override val answers: Array<Answer>
        get() = texts.answers;

    val phrases: Array<String>
        get() = texts.text;

    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>){
        this.id = id
        this.texts = PhraseText(id, phrases, answers)
    }

    constructor(id: String, phrases: PhraseText){
        this.id = id
        this.texts = phrases
    }

    override fun toString(): String {
       return "{${this.javaClass.simpleName}: id=$id, phrases=${texts}}"
    }

}