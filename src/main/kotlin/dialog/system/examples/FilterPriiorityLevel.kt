package dialog.system.examples

import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.phrase.AFilteredPhrase
import dialog.system.models.items.phrase.FilteredPhrase

class FilterPriiorityLevel {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val filtered = FilteredPhrase(
                "filtered.phrase",
                "init phrase",
                arrayOf(Answer("exit", "exit", AnswerType.EXIT))
            )


            filtered.addPhrasesFilter("3", AFilteredPhrase.Order.Last){
                    phrases, _ ->
                phrases[0]+="three\n"
                return@addPhrasesFilter phrases
            }

            filtered.addPhrasesFilter("4", AFilteredPhrase.Order.Last){
                    phrases, _ ->
                phrases[0]+="four\n"
                return@addPhrasesFilter phrases
            }


            filtered.addPhrasesFilter("1", AFilteredPhrase.Order.First){
                phrases, _ ->
                phrases[0]+="\none\n"
                return@addPhrasesFilter phrases
            }

            filtered.addPhrasesFilter("2", AFilteredPhrase.Order.First){
                    phrases, _ ->
                phrases[0]+="two\n"
                return@addPhrasesFilter phrases
            }

            filtered.run()

        }

    }

}