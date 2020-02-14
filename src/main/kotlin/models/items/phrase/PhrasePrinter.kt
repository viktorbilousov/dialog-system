package models.items.phrase

import models.Answer

interface PhrasePrinter {
    public fun printTextDialog(text: Array<String>, answer: Array<Answer>): Answer
}