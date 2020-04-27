import models.Answer
import models.items.phrase.APhrase
import models.items.phrase.DebugFilteredPhrase
import models.items.phrase.FilteredPhrase
import models.items.phrase.SimplePhrase
import models.items.runner.RunnerConfigurator


class DialogSysMain {
    companion object {
        @JvmStatic
        public fun main(args: Array<String>) {
            val simple = SimplePhrase("test", "hello world", arrayOf(Answer("answer 1", "annswer1")))
            val filtered =  APhrase.convertTo<FilteredPhrase>(simple)
            filtered.addAnswerFilter("test") {a,b -> a}
            filtered.addAnswerFilter("tes2") {a,b -> a}
            val debugged = APhrase.convertTo<DebugFilteredPhrase>(filtered)
            println(simple)
            println(filtered)
            println(debugged)
            RunnerConfigurator.setDebugRunner(filtered)

        }
    }
}
