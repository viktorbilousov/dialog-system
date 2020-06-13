package dialog.system.models.items.runner

import dialog.system.models.answer.Answer
import dialog.system.models.items.ADialogItem

class DefaultRunner : DialogItemRunner {



    override fun runItem(item: ADialogItem): Answer {
        item.before()
        val res = item.body()
        item.after(res);
        return res
    }

}