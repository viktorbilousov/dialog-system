package models.items.phrase

import models.Answer

open class SimplePhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : APhrase(id, phrases, answers)  {

    constructor(id: String, phrase: String, answers: Array<Answer>) : this(id, arrayOf(phrase), answers)

    final override fun filter(inputAnswers: Array<Answer>, inputPhrases: Array<String>): FilterResult {
        return FilterResult(answers, phrases)
    }

}