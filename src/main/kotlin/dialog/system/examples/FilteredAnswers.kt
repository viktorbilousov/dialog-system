package dialog.system.examples

import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.dialog.Dialog
import dialog.system.models.items.phrase.FilteredPhrase


class FilteredAnswers {
    companion object {
        @JvmStatic
        public fun main(args: Array<String>) {
            val filtered = FilteredPhrase(
                "filtered.phrase",
                arrayOf(
                    "this phrase is run first time",
                    "this phrase is run second time ",
                    "this phrase is run %CNT% time "
                ),
                arrayOf(
                    Answer("filtered.phrase", "try one more time"),
                    Answer("exit", "exit", AnswerType.EXIT)
                )
            )

            filtered.addAnswersFilter("answer.filter") { answers, cnt ->
                return@addAnswersFilter if(cnt < 3)  {
                        arrayOf(answers[0])
                    }  else {
                        answers
                    }
            }

            filtered.addPhrasesFilter("phrase.filter") { phrases, cnt ->
                return@addPhrasesFilter when(cnt){
                    1 -> arrayOf(phrases[0])
                    2 -> arrayOf(phrases[1])
                    else -> arrayOf(phrases[2].replace("%CNT%", cnt.toString()))
                }
            }
            val dialog = Dialog("dialog");

            dialog.addItem(filtered)
            dialog.startDialogItem = filtered
            dialog.run()
        }
    }
}
