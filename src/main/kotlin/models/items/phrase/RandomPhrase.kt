package models.items.phrase

import models.Answer
import models.items.text.IPhraseText
import models.items.text.MultiplyPhraseText
import models.items.text.SinglePhraseText
import tools.PhrasePrinter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class RandomPhrase(private val id: String) : IPhrase<MultiplyPhraseText>() {


    private var text: MultiplyPhraseText? = null
    private var answers = ArrayList<Answer>()
    private val random = Random();

    constructor(id: String, text: MultiplyPhraseText, answers: Array<Answer>) : this(id) {
        this.answers.addAll(answers)
        this.text = text;
    }

    override fun addText(phrase: MultiplyPhraseText) {
        this.text = phrase;
    }

    override fun addAnswer(answer: Answer) {
        this.answers.add(answer);
    }

   private fun getText(): String {
       if(text == null) throw IllegalAccessException("text is null");
        return text!!.getTexts()[abs(random.nextInt(text!!.getTexts().size))]
    }

    override fun body(inputAnswer: Answer): Answer {
        return PhrasePrinter.printTextDialog(text!!.getTexts()[0], answers.toTypedArray())
    }

    override fun getId(): String {
        return id;
    }
}