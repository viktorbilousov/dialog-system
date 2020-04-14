package models.items.runner

import models.Answer
import models.items.ADialogItem

interface DialogItemRunner {
    fun runItem(item: ADialogItem) : Answer
}