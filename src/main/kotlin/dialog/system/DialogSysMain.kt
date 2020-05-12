package dialog.system

import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.Answer
import dialog.system.models.items.ADialogItem
import dialog.system.models.items.phrase.APhrase
import dialog.system.models.items.phrase.DebugFilteredPhrase
import dialog.system.models.items.phrase.FilteredPhrase
import dialog.system.models.items.phrase.SimplePhrase
import dialog.system.models.items.runner.RunnerConfigurator
import dialog.system.models.router.Router


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
            filtered.addAnswersFilter("test") { a, b -> a}
            filtered.addAnswersFilter("tes2") { a, b -> a}
            val debugged = APhrase.convertTo<DebugFilteredPhrase>(filtered)
            println(simple)
            println(filtered)
            println(debugged)
            RunnerConfigurator.setDebugRunner(filtered)

            val map = HashMap<String, ADialogItem>();
            val router = Router("sad", TinkerGraph(), map)
        }
    }
}
