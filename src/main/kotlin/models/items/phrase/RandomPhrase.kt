package models.items.phrase

import models.Answer
import models.items.IDialogItem

import models.items.text.MultiplyPhraseText
import tools.PhrasePrinter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RandomPhrase : IPhrase<MultiplyPhraseText> {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private val id: String;
    private var text: MultiplyPhraseText;
    private val random = Random();

    constructor(id: String, text: MultiplyPhraseText) {
        this.text = text;
        this.id = id;
    }

    override fun addText(phrase: MultiplyPhraseText) {
        this.text = phrase;
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body RANDOM Phrase: input = $inputAnswer")
        val res = PhrasePrinter.printTextDialog(text.getTexts()[abs(random.nextInt(text.getTexts().size))], text.getAnswers())
        logger.info("[$id]<< body RANDOM Phrase: input = $res")
        return res;
    }

    override fun getId(): String {
        return id;
    }

    override fun clone(): IDialogItem {
        return RandomPhrase(
            id,
            MultiplyPhraseText(
                text.getId(),
                text.getTexts().clone(),
                text.getAnswers().clone()
            )
        )
    }
}