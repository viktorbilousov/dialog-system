package models.items.phrase

import models.Answer
import models.items.DialogItem

interface Phrase: DialogItem {
    val phrases: Array<String>
    fun filter(inputAnswers: Array<Answer>, inputPhrases: Array<String>): APhrase.FilterResult
}