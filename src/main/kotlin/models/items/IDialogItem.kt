package models.items

import models.Answer
import models.Indexable;

interface IDialogItem : Indexable {
    fun beforeRun(inputAnswer: Answer)
    fun afterRun(outputAnswer: Answer)
    fun run(inputAnswer: Answer) : Answer
}