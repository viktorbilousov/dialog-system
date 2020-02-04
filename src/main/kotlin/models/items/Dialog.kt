package models.items
import models.Answer;
import models.items.IDialogItem
import models.items.Router

class Dialog(private val router: Router) : IDialogItem {

    override fun beforeRun(inputAnswer: Answer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterRun(outputAnswer: Answer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun run(inputAnswer: Answer): Answer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): String {
        return router.getId();
    }
}