package phrases

import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.AnswerChooser
import dialog.system.models.items.phrase.PhraseChooser
import dialog.system.models.items.phrase.PhrasePrinter
import dialog.system.models.items.phrase.SimplePhrase

class EmptyPhrase(id: String) : SimplePhrase(id, arrayOf(""), arrayOf(
    Answer("", "")
)) {
    init {
        this.phrasePrinter = object : PhrasePrinter {
            override fun printTextDialog(text: String, answer: Array<Answer>) {
            }
        }
        this.answerChooser = object : AnswerChooser {
            override fun chooseAnswer(answers: Array<Answer>): Answer {
                return Answer.empty()
            }
        }
        this.phraseChooser = object : PhraseChooser {
            override fun choose(phrases: Array<String>): String {
                return ""
            }
        }
    }
}