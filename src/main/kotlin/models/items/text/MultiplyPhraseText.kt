package models.items.text

import models.Answer

class MultiplyPhraseText : IPhraseText{
    private val id: String
    private val text: Array<String>
    private val answers: Array<Answer>

    constructor(id: String, text: Array<String>, answers: Array<Answer>) {
        this.id = id
        this.text = text
        this.answers = answers
    }


    override fun getTexts(): Array<String> {
        return text;
    }

    override fun getAnswers(): Array<Answer> {
        return answers;
    }

    override fun getId(): String {
        return id;
    }
}