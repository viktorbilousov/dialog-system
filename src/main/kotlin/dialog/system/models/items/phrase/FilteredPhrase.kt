package dialog.system.models.items.phrase

import dialog.system.models.Answer
import dialog.system.tools.AnswersTool

open class FilteredPhrase : AFilteredPhrase {

    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)


    final override fun body(): Answer {
        incrementCounter()
        val answers = AnswersTool.copyArrayOrAnswers(this.answers)
        val phrases = this.phrases.clone()

        val bodyAnswer = filter(answers, phrases)

        val phrase = phraseChooser.choose(bodyAnswer.phrases)
        phrasePrinter.printTextDialog(phrase, bodyAnswer.answers)
        return answerChooser.chooseAnswer(bodyAnswer.answers)
    }
}