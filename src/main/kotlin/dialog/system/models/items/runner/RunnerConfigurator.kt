package dialog.system.models.items.runner

import dialog.system.models.items.ADialogItem

class RunnerConfigurator {
    companion object{
        public fun <T : DialogItemRunner> setRunner(dialogItem: ADialogItem, runner: T): T{
            dialogItem.runner = runner;
            return dialogItem.runner as T;
        }
        public fun setDebugRunner(dialogItem: ADialogItem): DebugRunner {
            return setRunner(
                dialogItem,
                DebugRunner()
            )
        }
        public fun setDefaultRunner(dialogItem: ADialogItem): DefaultRunner {
            return setRunner(
                dialogItem,
                DefaultRunner()
            )
        }
    }
}