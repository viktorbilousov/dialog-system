package debug.items.phrase

import models.Answer
import models.items.phrase.*
import tools.AnswersTool

class DebugPhrase(_phrase: SimplePhrase) : ADebugDialogItem(), Phrase {

    public val phrase = _phrase

    override val id: String = _phrase.id

    public var answerChooser: AnswerChooser
        get() = phrase.answerChooser
        set(value) {
            phrase.answerChooser = value
        }

    public var phraseChooser: PhraseChooser
        get() = phrase.phraseChooser
        set(value) {
            phrase.phraseChooser = value
        }

    public var phrasePrinter: PhrasePrinter
        get() = phrase.phrasePrinter
        set(value) {
            phrase.phrasePrinter = value
        }

    override val phrases: Array<String> = _phrase.phrases
    override val answers: Array<Answer> = _phrase.answers

    override fun run(): Answer {
        return phrase.run()
    }

    override val before: () -> Unit = _phrase.before
    override val after: (Answer) -> Unit = _phrase.after
    override val body: () -> Answer = { _phrase.body() }


    public var filterBegin: (inputAnswers: Array<Answer>, inputPhrases: Array<String>) -> Phrase.FilterResult =
        { a, p ->
            Phrase.FilterResult(a, p)
        }

    public var filterEnd: (inputAnswers: Array<Answer>, inputPhrases: Array<String>) -> Phrase.FilterResult = { a, p ->
        Phrase.FilterResult(a, p)
    }

    public fun updatePhraseChooser(lambda: (chooser: PhraseChooser) -> PhraseChooser) {
        phrase.phraseChooser = lambda(phrase.phraseChooser)
    }

    public fun updateAnswerChooser(lambda: (chooser: AnswerChooser) -> AnswerChooser) {
        phrase.answerChooser = lambda(phrase.answerChooser)
    }

    public fun updatePrinter(lambda: (chooser: PhrasePrinter) -> PhrasePrinter) {
        phrase.phrasePrinter = lambda(phrase.phrasePrinter)
    }

    override fun filter(inputAnswers: Array<Answer>, inputPhrases: Array<String>): Phrase.FilterResult {
        var res = filterBegin(inputAnswers, inputPhrases)
        res = phrase.filter(res.answers, res.phrases)
        return filterEnd(res.answers, res.phrases)
    }
}
