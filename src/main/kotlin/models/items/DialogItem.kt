package models.items

import models.Answer
import models.Indexable

interface DialogItem : Indexable{
    val answers: Array<Answer>
    fun run(): Answer
}