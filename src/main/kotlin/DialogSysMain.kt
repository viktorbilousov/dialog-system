import models.Answer
import models.items.phrase.FilteredPhrase
import models.items.phrase.SimplePhrase


class DialogSysMain {
    companion object {
        @JvmStatic
        public fun main(args: Array<String>) {

            val filtred = SimplePhrase("test", "hello world", arrayOf(Answer("answer 1", "annswer1")))
            val simplePhrase = filtred as FilteredPhrase

        }
    }
}
