package models.items.phrase

import models.Answer
import models.items.DialogItem

class EmptyPhrase(id: String) : Phrase(id, arrayOf(""), arrayOf(Answer("", ""))) {
    override fun body(inputAnswer: Answer): Answer {
        return inputAnswer;
    }



}