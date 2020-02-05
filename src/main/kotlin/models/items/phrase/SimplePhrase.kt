package models.items.phrase

import models.Answer
import models.items.text.IPhraseText
import models.items.text.SinglePhraseText
import tools.PhrasePrinter
import kotlin.streams.toList

class SimplePhrase(private val id: String) : IPhrase<SinglePhraseText>() {


    private var text: SinglePhraseText? = null
    private var answers = ArrayList<Answer>()

    constructor(id: String, text: SinglePhraseText, answers: Array<Answer>) : this(id) {
        this.answers.addAll(answers)
        this.text = text;
    }

    override fun addText(phrase: SinglePhraseText) {
        this.text = phrase;
    }

    override fun addAnswer(answer: Answer) {
        this.answers.add(answer);
    }

    override fun body(inputAnswer: Answer): Answer {
        return PhrasePrinter.printTextDialog(text!!.getTexts()[0], answers.toTypedArray())
    }


    override fun getId(): String {
        return id;
    }
}