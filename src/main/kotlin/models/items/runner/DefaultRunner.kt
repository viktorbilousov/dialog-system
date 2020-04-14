package models.items.runner

import models.Answer
import models.items.ADialogItem

class DefaultRunner : DialogItemRunner{



    override fun runItem(item: ADialogItem): Answer {
        item.before()
        val res = item.body()
        item.after(res);
        return res
    }

}