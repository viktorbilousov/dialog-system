package models.items.dialog
import models.Answer;
import models.items.IDialogItem
import models.items.Router

class Dialog(private val id: String, private val router: Router) : IDialogItem {

    private val itemsList =  ArrayList<IDialogItem>();

    override fun getId(): String {
        return id;
    }

    override fun body(inputAnswer: Answer): Answer {

    }
}