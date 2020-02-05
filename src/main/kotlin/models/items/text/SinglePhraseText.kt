package models.items.text

import models.Answer

class SinglePhraseText : IPhraseText{

    private val id: String
    private val text: String
    private val answers: Array<Answer>

    constructor(id: String, text: String, answers: Array<Answer>) {
        this.id = id
        this.text = text
        this.answers = answers
    }

    override fun getTexts(): Array<String> {
        return arrayOf(text);
    }

    override fun getAnswers(): Array<Answer> {
        return answers;
    }

    override fun getId(): String {
        return id;
    }
}