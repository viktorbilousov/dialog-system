package dialog.system.models.items.runner

import dialog.system.models.answer.Answer
import dialog.system.models.items.ADialogItem

interface DialogItemRunner {
    fun runItem(item: ADialogItem) : Answer
}