package dialog.system

import dialog.system.models.Answer
import dialog.system.models.items.phrase.APhrase
import dialog.system.models.items.phrase.DebugFilteredPhrase
import dialog.system.models.items.phrase.FilteredPhrase
import dialog.system.models.items.phrase.SimplePhrase
import dialog.system.models.items.runner.RunnerConfigurator


class DialogSysMain {
    companion object {
        @JvmStatic
        public fun main(args: Array<String>) {
            val simple = SimplePhrase(
                "test",
                "hello world",
                arrayOf(Answer("answer 1", "annswer1"))
            )
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
