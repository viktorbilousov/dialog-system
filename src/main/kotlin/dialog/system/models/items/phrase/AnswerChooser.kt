package dialog.system.models.items.phrase

import dialog.system.models.answer.Answer

interface AnswerChooser {
    public fun chooseAnswer(answers: Array<Answer>) : Answer
}