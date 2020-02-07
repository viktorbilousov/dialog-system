package models.items.phrase

import models.Answer
import models.items.IDialogItem
import models.items.text.MultiplyPhraseText
import models.items.text.SinglePhraseText
import tools.PhrasePrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SimplePhrase(private val id: String) : IPhrase<SinglePhraseText> {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    private lateinit var text: SinglePhraseText

    constructor(id: String, text: SinglePhraseText) : this(id) {
        this.text = text;
    }

    override fun addText(phrase: SinglePhraseText) {
        this.text = phrase;
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body SIMPLE Phrase: input = $inputAnswer")
        val res =  PhrasePrinter.printTextDialog(text!!.getTexts()[0], text!!.getAnswers())
        logger.info("[$id]<< body SIMPLE Phrase: output = $res")
        return res;
    }


    override fun getId(): String {
        return id;
    }

    override fun clone(): IDialogItem {
        return SimplePhrase(
            id,
            SinglePhraseText(
                text.getId(),
                text.getTexts()[0],
                text.getAnswers()
            )
        )
    }

    override fun getAnswers(): Array<Answer> {
        return text.getAnswers();
    }
}