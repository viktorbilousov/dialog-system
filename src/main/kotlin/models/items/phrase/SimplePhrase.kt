package models.items.phrase

import models.Answer
import tools.AnswersTool

open class SimplePhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : APhrase(id, phrases, answers)  {

    constructor(id: String, phrase: String, answers: Array<Answer>) : this(id, arrayOf(phrase), answers)


    override fun body(): Answer {
        incrementCounter()
        val answers = AnswersTool.copyArrayOrAnswers(this.answers)
        val phrases = this.phrases.clone()

        val phrase = phraseChooser.choose(phrases)
        phrasePrinter.printTextDialog(phrase, answers)
        return answerChooser.chooseAnswer(answers)
    }

}