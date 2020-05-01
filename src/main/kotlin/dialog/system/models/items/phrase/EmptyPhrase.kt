package dialog.system.models.items.phrase

import dialog.system.models.Answer

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