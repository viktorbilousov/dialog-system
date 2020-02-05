package models.items

import models.Answer
import models.Indexable;

interface IDialogItem : Indexable {
    open fun before(inputAnswer: Answer){}
    open fun after(outputAnswer: Answer){}
    fun body(inputAnswer: Answer) : Answer

    fun run(inputAnswer: Answer): Answer{
        before(inputAnswer)
        val res = body(inputAnswer)
        after(res);
        return res
    }
}