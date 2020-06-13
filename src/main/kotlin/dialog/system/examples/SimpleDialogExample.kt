package dialog.system.examples

import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.dialog.Dialog
import dialog.system.models.items.phrase.SimplePhrase


class SimpleDialogExample {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {

            val dialog = getSimpleDialog()
            dialog.run();

        }
       private fun getSimpleDialog(): Dialog {
            val dialog = Dialog("dialog");
            val phrase1 = SimplePhrase("phrase.1", "phrase 1: 2+2*2=?",
                arrayOf(
                    Answer("phrase.2", "answer is 8"),
                    Answer("phrase.3", "answer is 6")
                ))
            val phrase2 = SimplePhrase("phrase.2", "false", arrayOf(
                Answer(
                    "exit",
                    "exit",
                    AnswerType.EXIT
                )
            ))
            val phrase3 = SimplePhrase("phrase.3", "correct", arrayOf(
                Answer(
                    "exit",
                    "exit",
                    AnswerType.EXIT
                )
            ))

            dialog.addAll(listOf(phrase1, phrase2, phrase3))
            dialog.startDialogItem = phrase1

            return dialog
        }
    }

}