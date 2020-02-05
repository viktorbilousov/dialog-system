package models.items.text

import models.Answer
import models.Indexable

interface IPhraseText : Indexable{
    fun getTexts() : Array<String>
    fun getAnswers() : Array<Answer>
}