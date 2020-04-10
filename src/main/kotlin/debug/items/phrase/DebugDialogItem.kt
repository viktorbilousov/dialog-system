package debug.items.phrase

import models.Answer
import models.items.ADialogItem
import models.items.DialogItem

import org.slf4j.Logger
import org.slf4j.LoggerFactory


open class DebugDialogItem (item: ADialogItem): ADebugDialogItem() {


    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    override val id = item.id

    override val answers: Array<Answer> = item.answers

    public override val before = item.before
    public override val after = item.after
    public override val body = item::body

}