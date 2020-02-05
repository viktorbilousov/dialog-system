package models.items.phrase

import models.Answer
import models.items.IDialogItem
import models.items.text.IPhraseText

abstract class IPhrase<P : IPhraseText>: IDialogItem {
    abstract fun addText(phrase: P)
    abstract fun addAnswer(answer: Answer)


    //fun getText() : String
    //fun getAnswers() : Array<Answer>?

}