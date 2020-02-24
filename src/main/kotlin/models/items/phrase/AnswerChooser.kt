package models.items.phrase

import models.Answer

interface AnswerChooser {
    public fun chooseAnswer(answers: Array<Answer>) : Answer
}