package dialog.system.models.items.phrase

import dialog.system.models.Answer

interface AnswerChooser {
    public fun chooseAnswer(answers: Array<Answer>) : Answer
}