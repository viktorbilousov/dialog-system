package models.items.phrase

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.AnswersTool

open class FilteredPhrase : Phrase {
    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private var count = 0;

    private val filtersAnswerMap = LinkedHashMap< String ,(Array<Answer>, Int) -> Array<Answer> >()
    private val filtersPhrasesMap = LinkedHashMap< String ,(Array<String>, Int) -> Array<String> >()

    public fun addAnswerFilter(name: String, filter: (Array<Answer>, Int) -> Array<Answer>){
        filtersAnswerMap[name] = filter;
    }

    public fun addPhrasesFilter(name: String, filter: (Array<String>, Int) -> Array<String>){
        filtersPhrasesMap[name] = filter;
    }

    public fun removePhraseFilter(name: String){
        filtersPhrasesMap.remove(name);
    }

    public fun removeAnswerFilter(name: String){
        filtersAnswerMap.remove(name)
    }



    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body Filtered Phrase: input = $inputAnswer")
        count++;
        var answers = AnswersTool.copyArrayOrAnswers(this.answers)
        var phrases = this.phrases.clone();
        for (value in filtersAnswerMap.values) {
            answers = value(answers, count);
        }
        for (value in filtersPhrasesMap.values) {
            phrases = value(phrases, count);
        }
        val phrase = phraseChooser.choose(phrases)
        phrasePrinter.printTextDialog(phrase, answers)
        val res =  answerChooser.chooseAnswer(answers)
        logger.info("[$id]<< body Filtered  Phrase: output = $res")
        return res;
    }
}