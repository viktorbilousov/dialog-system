package models.items.phrase

import models.Answer

interface PhrasePrinter {
    public fun printTextDialog(text: String , answer: Array<Answer>)
}