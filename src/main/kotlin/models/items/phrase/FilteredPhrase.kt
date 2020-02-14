package models.items.phrase

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FilteredPhrase : Phrase {
    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private val count = 0;

    private val filtesAnswerMap = LinkedHashMap< String ,(Array<Answer>, Int) -> Array<Answer> >()
    private val filtesPhrasesMap = LinkedHashMap< String ,(Array<String>, Int) -> Array<String> >()

    public fun <T: Phrase> addAnswerFilter(name: String, filter: (Array<Answer>, Int) -> Array<Answer>){
        filtesAnswerMap[name] = filter;
    }

    public fun <T: Phrase> addPhrasesFilter(name: String, filter: (Array<String>, Int) -> Array<String>){
        filtesPhrasesMap[name] = filter;
    }

    public fun removePhraseFilter(name: String){
        filtesPhrasesMap.remove(name);
    }

    public fun removeAnswerFilter(name: String){
        filtesAnswerMap.remove(name)
    }



    override fun body(inputAnswer: Answer): Answer {
        logger.info("[$id]>> body SIMPLE Phrase: input = $inputAnswer")
        var answers = this.answers.clone()
        var phrases = this.phrases.clone();
        for (value in filtesAnswerMap.values) {
            answers = value(answers, count);
        }
        for (value in filtesPhrasesMap.values) {
            phrases = value(phrases, count);
        }
        val res =  phrasePrinter.printTextDialog(phrases, answers)
        logger.info("[$id]<< body SIMPLE Phrase: output = $res")
        return res;
    }
}