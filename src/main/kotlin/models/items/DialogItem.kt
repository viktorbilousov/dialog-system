package models.items

import models.Answer
import models.Indexable

interface DialogItem  : Indexable {
    fun clone() : ADialogItem;


    fun before(inputAnswer: Answer)
    fun after(outputAnswer: Answer)
    fun body(inputAnswer: Answer) : Answer
    fun run(inputAnswer: Answer): Answer

}