package models.items.phrase

import models.Answer
import models.items.ADialogItem

class EmptyPhrase(id: String) : Phrase(id, arrayOf(""), arrayOf(Answer("", ""))) {
    override fun body(inputAnswer: Answer): Answer {
        return inputAnswer;
    }

    override fun clone(): ADialogItem {
        return EmptyPhrase(id);
    }
}