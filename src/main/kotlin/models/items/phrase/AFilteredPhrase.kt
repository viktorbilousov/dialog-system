package models.items.phrase

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.AnswersTool

abstract class AFilteredPhrase : APhrase {
    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

        public fun toDebugFilteredPhrase(filteredPhrase: FilteredPhrase): DebugFilteredPhrase{
            return DebugFilteredPhrase(filteredPhrase)
        }
        public fun toFilteredPhrase(filteredPhrase: DebugFilteredPhrase): FilteredPhrase{
            return FilteredPhrase(filteredPhrase.id, filteredPhrase.phrases, filteredPhrase.answers)
        }
    }

    abstract override fun body(): Answer

    private val firstFiltersAnswerMap = LinkedHashMap< String ,(Array<Answer>, Int) -> Array<Answer> >()
    private val lastFiltersAnswerMap = LinkedHashMap< String ,(Array<Answer>, Int) -> Array<Answer> >()
    private val firstFiltersPhrasesMap = LinkedHashMap< String ,(Array<String>, Int) -> Array<String> >()
    private val lastFiltersPhrasesMap = LinkedHashMap< String ,(Array<String>, Int) -> Array<String> >()

    public fun addAnswerFilter(name: String, filter: (Array<Answer>, Int) -> Array<Answer>){
        addAnswerFilter(name, Order.First, filter)
    }

    public fun addPhrasesFilter(name: String, order: Order, filter: (Array<String>, Int) -> Array<String>){
        when(order){
            Order.Last ->  lastFiltersPhrasesMap[name] = filter;
            Order.First -> firstFiltersPhrasesMap[name] = filter;
        }
    }

    public fun addAnswerFilter(name: String, order: Order, filter: (Array<Answer>, Int) -> Array<Answer>){
        when(order){
            Order.Last ->  lastFiltersAnswerMap[name] = filter;
            Order.First -> firstFiltersAnswerMap[name] = filter;
        }
    }

    public fun addPhrasesFilter(name: String, filter: (Array<String>, Int) -> Array<String>){
        addPhrasesFilter(name, Order.First, filter)
    }

    public fun removePhraseFilter(name: String){
        firstFiltersPhrasesMap.remove(name);
        lastFiltersPhrasesMap.remove(name);
    }

    public fun removeAnswerFilter(name: String){
        firstFiltersAnswerMap.remove(name);
        lastFiltersAnswerMap.remove(name);
    }

    protected fun filter(inputAnswers: Array<Answer>, inputPhrases: Array<String>): FilterResult {
        logger.info("[$id]>> body Filtered Phrase: count = ${count+1}")
        var answers = inputAnswers
        var phrases= inputPhrases
        logger.info("[$id] call high priority answers filters")
        for (entry in firstFiltersAnswerMap) {
            logger.info("call filter: '${entry.key}', input answer  ${answers.contentToString()}")
            answers = entry.value(answers, count);
            logger.info("result answer: ${answers.contentToString()}")
        }
        logger.info("[$id] call low priority answers filters")
        for (entry in lastFiltersAnswerMap) {
            logger.info("call filter: '${entry.key}', input answer  ${answers.contentToString()}")
            answers = entry.value(answers, count);
            logger.info("result answer: ${answers.contentToString()}")
        }

        logger.info("[$id] call high priority phrases filters")
        for (entry in firstFiltersPhrasesMap) {
            logger.info("call filter: '${entry.key}', input phrases=${phrases.contentToString()}")
            phrases = entry.value(phrases, count);
            logger.info("result phrases=${phrases.contentToString()}")
        }
        logger.info("[$id] call low priority phrases filters")
        for (entry in lastFiltersPhrasesMap) {
            logger.info("call filter: '${entry.key}', input phrases=${phrases.contentToString()}")
            phrases = entry.value(phrases, count);
            logger.info("result phrases=${phrases.contentToString()}")
        }

        return FilterResult(answers, phrases)
    }

    enum class Order{
        First, Last
    }

    data class FilterResult(val answers: Array<Answer>, val phrases: Array<String>)

}