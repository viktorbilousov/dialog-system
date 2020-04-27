package models.items.phrase

import models.Answer
import models.items.ADialogItem
import models.items.runner.DefaultRunner
import models.items.runner.DialogItemRunner
import models.items.text.PhraseText
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.ConsoleAnswerChooser
import tools.FirstPhraseChooser
import tools.SimplePhrasePrinter
import java.lang.Exception

abstract class APhrase  : ADialogItem {

    companion object{

        public inline fun <reified T : APhrase> convertTo(phrase: APhrase): T{
            try {
                val res = T::class.java
                    .getConstructor(String::class.java, Array<String>::class.java, Array<Answer>::class.java)
                    .newInstance(phrase.id, phrase.phrases, phrase.answers) as T
                res.initFrom(phrase);
                return res;
            }catch (e: Exception){
                val logger = LoggerFactory.getLogger(APhrase::class.java) as Logger
                logger.error("class not has constructor (id: String, phrases: Array<String>,  answers: Array<Answer>)", e)
                throw InstantiationError("class not has constructor (id: String, phrases: Array<String>,  answers: Array<Answer>)")
            }
        }
    }

    override fun initFrom(source: ADialogItem) {
        super.initFrom(source)
        if (source !is APhrase) return;
        this.phraseChooser = source.phraseChooser;
        this.answerChooser = source.answerChooser;
        this.phrasePrinter = source.phrasePrinter

    }

    public var texts: PhraseText
        private set

    final override val id: String

    protected var count = 0
        private set;

    protected fun incrementCounter(){
        count++;
    }
    protected fun resetCount(){
        count = 0
    }

    public var phrasePrinter : PhrasePrinter  = SimplePhrasePrinter()
    public var phraseChooser : PhraseChooser  = FirstPhraseChooser()
    public var answerChooser : AnswerChooser  = ConsoleAnswerChooser()

    public fun updatePrinter(newPrinter: (oldPrinter: PhrasePrinter) -> PhrasePrinter){
        this.phrasePrinter = newPrinter(this.phrasePrinter);
    }

    public fun updateAnswerChooser(newAnswerChooser: (oldAnswerChooser: AnswerChooser) -> AnswerChooser){
        this.answerChooser = newAnswerChooser(this.answerChooser);
    }

    public fun updatePhraseChooser(newPhraseChooser: (oldPhraseChooser: PhraseChooser) -> PhraseChooser){
        this.phraseChooser= newPhraseChooser(this.phraseChooser);
    }

    public override val answers: Array<Answer>
        get() = texts.answers;

    public val phrases: Array<String>
        get() = texts.text;

    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>){
        this.id = id
        this.texts = PhraseText(id, phrases, answers, APhrase::class.java.name)
    }

    constructor(id: String, phrases: PhraseText){
        this.id = id
        this.texts = PhraseText(phrases.id, phrases.text, phrases.answers, APhrase::class.java.name)
    }

    override var runner: DialogItemRunner = DefaultRunner();

    override fun toString(): String {
        return "{${this.javaClass.simpleName}: id=$id, phrases=${texts}}"
    }

}