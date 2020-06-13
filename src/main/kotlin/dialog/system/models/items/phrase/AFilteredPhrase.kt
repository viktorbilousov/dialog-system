package dialog.system.models.items.phrase

import dialog.system.models.answer.Answer
import dialog.system.models.items.ADialogItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AFilteredPhrase : APhrase {
    constructor(id: String, phrases: Array<String>,  answers: Array<Answer>) : super(id, phrases, answers)
    constructor(id: String, phrase: String,  answers: Array<Answer>) : super(id, arrayOf(phrase), answers)

    override fun initFrom(source: ADialogItem) {
        super.initFrom(source)
        if(source !is AFilteredPhrase) return

        this.firstFiltersAnswerMap.clear()
        this.firstFiltersPhrasesMap.clear()
        this.lastFiltersAnswerMap.clear()
        this.lastFiltersPhrasesMap.clear()

        this.firstFiltersAnswerMap.putAll(source.firstFiltersAnswerMap)
        this.firstFiltersPhrasesMap.putAll(source.firstFiltersPhrasesMap)
        this.lastFiltersPhrasesMap.putAll(source.lastFiltersPhrasesMap)
        this.lastFiltersAnswerMap.putAll(source.lastFiltersAnswerMap)

    }

    companion object{
        private val logger = LoggerFactory.getLogger(AFilteredPhrase::class.java) as Logger
    }

    abstract override fun body(): Answer

    private val firstFiltersAnswerMap = LinkedHashMap< String ,(Array<Answer>, Int) -> Array<Answer> >()
    private val lastFiltersAnswerMap = LinkedHashMap< String ,(Array<Answer>, Int) -> Array<Answer> >()
    private val firstFiltersPhrasesMap = LinkedHashMap< String ,(Array<String>, Int) -> Array<String> >()
    private val lastFiltersPhrasesMap = LinkedHashMap< String ,(Array<String>, Int) -> Array<String> >()

    private val firstResultAnswerFilterMap = LinkedHashMap< String, (Answer) -> Answer>()
    private val lastResultAnswerFilterMap =  LinkedHashMap<String,  (Answer) -> Answer>()

    public fun addAnswersFilter(name: String, filter: (Array<Answer>, Int) -> Array<Answer>){
        addAnswersFilter(name, Order.First, filter)
    }

    public fun addResultAnswerFilter(name: String, filter: (Answer) -> Answer){
        addResultAnswerFilter(name, Order.First, filter)
    }

    public fun addResultAnswerFilter(name: String, order: Order, filter: (Answer) -> Answer){
        when(order){
            Order.Last ->  lastResultAnswerFilterMap[name] = filter
            Order.First -> firstResultAnswerFilterMap[name] = filter
        }
    }

    public fun addPhrasesFilter(name: String, order: Order, filter: (Array<String>, Int) -> Array<String>){
        when(order){
            Order.Last ->  lastFiltersPhrasesMap[name] = filter
            Order.First -> firstFiltersPhrasesMap[name] = filter
        }
    }

    public fun addAnswersFilter(name: String, order: Order, filter: (Array<Answer>, Int) -> Array<Answer>){
        when(order){
            Order.Last ->  lastFiltersAnswerMap[name] = filter
            Order.First -> firstFiltersAnswerMap[name] = filter
        }
    }

    public fun addPhrasesFilter(name: String, filter: (Array<String>, Int) -> Array<String>){
        addPhrasesFilter(name, Order.First, filter)
    }

    public fun removePhraseFilter(name: String){
        firstFiltersPhrasesMap.remove(name)
        lastFiltersPhrasesMap.remove(name)
    }

    public fun removeAnswerFilter(name: String){
        firstFiltersAnswerMap.remove(name)
        lastFiltersAnswerMap.remove(name)
        firstResultAnswerFilterMap.remove(name)
        lastResultAnswerFilterMap.remove(name)
    }

    protected fun filter(inputAnswers: Array<Answer>, inputPhrases: Array<String>): FilterResult {
        logger.info("[$id]>> body Filtered Phrase: count = $count")
        var answers = inputAnswers
        var phrases= inputPhrases
        logger.info("[$id] -------- answers filters --------")
        logger.info("[$id] call high priority answers filters")
        for (entry in firstFiltersAnswerMap) {
            logger.info("call filter: '${entry.key}', input answer  ${answers.contentToString()}")
            answers = entry.value(answers, count)
            logger.info("result answer: ${answers.contentToString()}")
        }
        logger.info("[$id] call low priority answers filters")
        for (entry in lastFiltersAnswerMap) {
            logger.info("call filter: '${entry.key}', input answer  ${answers.contentToString()}")
            answers = entry.value(answers, count)
            logger.info("result answer: ${answers.contentToString()}")
        }

        logger.info("[$id] -------- phrases filters --------")
        logger.info("[$id] call high priority phrases filters")
        for (entry in firstFiltersPhrasesMap) {
            logger.info("call filter: '${entry.key}', input phrases=${phrases.contentToString()}")
            phrases = entry.value(phrases, count)
            logger.info("result phrases=${phrases.contentToString()}")
        }
        logger.info("[$id] call low priority phrases filters")
        for (entry in lastFiltersPhrasesMap) {
            logger.info("call filter: '${entry.key}', input phrases=${phrases.contentToString()}")
            phrases = entry.value(phrases, count)
            logger.info("result phrases=${phrases.contentToString()}")
        }
        logger.info("[$id]<< body Filtered Phrase")
        return FilterResult(answers, phrases)
    }

    fun answerFilter(_answer: Answer): Answer {
        var answer = _answer
        logger.info("[$id] -------- result answers filters --------")
        logger.info("[$id] call high priority phrases filters")
        for (entry in firstResultAnswerFilterMap) {
            logger.info("call filter: '${entry.key}', input answer=${answer}")
            answer = entry.value(answer)
            logger.info("result answer=${answer}")
        }
        logger.info("[$id] call low priority phrases filters")
        for (entry in lastResultAnswerFilterMap) {
            logger.info("call filter: '${entry.key}', input answer=${answer}")
            answer = entry.value(answer)
            logger.info("result answer=${answer}")
        }
        return answer
    }


    enum class Order{
        First, Last
    }







    override fun toString(): String {


        return "{${this.javaClass.simpleName}: id=$id, phrases=${texts}, " +
                "\nfirstFiltersAnswerMap = {${firstFiltersAnswerMap.map { "${it.key}:${it.value.javaClass.simpleName}" }.joinToString()}} " +
                "\nlastFiltersAnswerMap  = {${lastFiltersAnswerMap.map { "${it.key}:${it.value.javaClass.simpleName}" }.joinToString()}} " +
                "\nfirstFiltersPhrasesMap = {${firstFiltersPhrasesMap.map { "${it.key}:${it.value.javaClass.simpleName}" }.joinToString()}} " +
                "\nlastFiltersPhrasesMap = {${lastFiltersPhrasesMap.map { "${it.key}:${it.value.javaClass.simpleName}" }.joinToString()}} "
    }


    data class FilterResult(val answers: Array<Answer>, val phrases: Array<String>)

}