package dialog.system.models.items.phrase

import dialog.system.models.answer.Answer

interface PhrasePrinter {
    public fun printTextDialog(text: String , answer: Array<Answer>)
}