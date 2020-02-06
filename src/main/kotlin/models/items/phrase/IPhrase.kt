package models.items.phrase

import models.Answer
import models.items.IDialogItem
import models.items.text.IPhraseText

interface IPhrase<P : IPhraseText>: IDialogItem {
    fun addText(phrase: P)

    //fun getText() : String
    //fun getAnswers() : Array<Answer>?

}