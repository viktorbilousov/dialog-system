package models.items.phrase

import models.Answer
import models.items.runner.DebugRunner
import tools.AnswersTool
import java.lang.invoke.SerializedLambda

open class DebugFilteredPhrase : AFilteredPhrase{

    public constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    public constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)
    constructor(filteredPhrase: FilteredPhrase) : super(filteredPhrase.id, filteredPhrase.phrases, filteredPhrase.answers)

     init {
         this.runner = DebugRunner();
     }

    public fun updatePrinter(lambda: (PhrasePrinter) -> PhrasePrinter){
        this.phrasePrinter = lambda(this.phrasePrinter);
    }

    public fun updateAnswerChooser(lambda: (AnswerChooser) -> AnswerChooser){
        this.answerChooser = lambda(this.answerChooser);
    }

    public fun updatePhraseChooser(lambda: (PhraseChooser) -> PhraseChooser){
        this.phraseChooser= lambda(this.phraseChooser);
    }

    final override fun body(): Answer {
        incrementCounter()
        val answers = AnswersTool.copyArrayOrAnswers(this.answers)
        val phrases = this.phrases.clone()

        var bodyAnswer = beforeFilter(answers, phrases, this)
        bodyAnswer = filter(bodyAnswer.answers, bodyAnswer.phrases)
        bodyAnswer = afterFilter(bodyAnswer.answers, bodyAnswer.phrases, this)

        val phrase = phraseChooser.choose(bodyAnswer.phrases)
        phrasePrinter.printTextDialog(phrase, bodyAnswer.answers)
        return answerChooser.chooseAnswer(bodyAnswer.answers)
    }

    public var beforeFilter : (answers: Array<Answer>, phrases: Array<String>, it: AFilteredPhrase) ->  FilterResult =
        { _, _, _ ->  FilterResult(answers, phrases)}
    public var afterFilter : (answers: Array<Answer>, phrases: Array<String>, it: AFilteredPhrase) ->  FilterResult  =
        { _, _, _ ->  FilterResult(answers, phrases)}
}