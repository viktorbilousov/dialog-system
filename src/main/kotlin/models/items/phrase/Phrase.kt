package models.items.phrase

import models.Answer
import models.items.DialogItem
import models.items.text.PhraseText
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Phrase : DialogItem {
    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    public var texts: PhraseText
    private set

    final override val id: String

    private val answersList = arrayListOf<Answer>();

    override val answers: Array<Answer>
        get() = texts.answers;

    val phrases: Array<String>
        get() = texts.text;

    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>){
        this.id = id
        this.texts = PhraseText(id, phrases, answers)
        this.answersList.addAll(answers)
    }

    constructor(id: String, phrases: PhraseText){
        this.id = id
        this.texts = phrases
    }

    override fun toString(): String {
       return "{${this.javaClass.simpleName}: id=$id, phrases=${texts}}"
    }
}