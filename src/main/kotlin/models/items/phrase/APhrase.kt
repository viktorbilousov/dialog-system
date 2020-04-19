package models.items.phrase

import models.Answer
import models.items.ADialogItem
import models.items.runner.DefaultRunner
import models.items.runner.DialogItemRunner
import models.items.text.PhraseText
import tools.ConsoleAnswerChooser
import tools.FirstPhraseChooser
import tools.SimplePhrasePrinter
import javax.print.attribute.standard.Destination

abstract class APhrase  : ADialogItem {

    companion object{


        // todo: dont copies filters
        public inline fun <reified T : APhrase> convertTo(phrase: APhrase): T{
           T::class.java.constructors.forEach {
                if (it.parameterCount == 3
                    && it.parameters[0].type == String::class.java
                    && it.parameters[1].type == Array<String>::class.java
                    && it.parameters[2].type == Array<Answer>::class.java
                ) {
                    val res =  it.newInstance(phrase.id, phrase.phrases, phrase.answers) as T;
                    res.`access$initFrom`(phrase);
                    return res;
                }
            }
            throw InstantiationError("class not has constructor (id: String, phrases: Array<String>,  answers: Array<Answer>)")
        }
    }

    protected open fun initFrom(source: APhrase){
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

    @PublishedApi
    internal fun `access$initFrom`(source: APhrase) = initFrom(source)

}