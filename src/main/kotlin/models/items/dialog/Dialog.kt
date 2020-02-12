package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.DialogItem
import models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class Dialog : DialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    public val router: Router

    override val id: String
    private var currentItem : DialogItem;

    override val answers: Array<Answer>
        get() {
            val answersList = arrayListOf<Answer>()
            if(router.items == null) {
                throw IllegalAccessException("ItemsSet is null!")
            }
            router.items!!.values.forEach {
                it.answers.forEach { asw ->
                    if (asw.type == AnswerType.EXIT) {
                        answersList.add(asw)
                    }
                }
            }
            return answersList.toTypedArray()
        }


    constructor(id: String, router: Router){
        this.id = id;
        this.router = router;
        currentItem = router.startPoint;
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[DIALOG] [$id] >> body")
        var answer = inputAnswer;
        while (true) {
            logger.info("[$id] run item: ${currentItem.id} ")
            answer =  currentItem.run(answer)
            logger.info("[$id] answer is $answer")
            if(answer.type == AnswerType.EXIT || answer.type == AnswerType.ENTER){
                logger.info("return ${answer}")
                logger.info("[$id] << body DIALOG")
                answer.type=AnswerType.SIMPLE;
                return answer;
            }else {
                currentItem = router.getNext(answer)
            }
        }
    }
    public fun addItem(item: DialogItem){
        router.addItem(item);
    }

    public fun startFrom(id: String, inputAnswer: Answer) : Answer? {
        logger.info("[DIALOG] [$id] >> startFrom")
        val item = router.goTo(id) ?: return null;
        logger.info("[DIALOG] [$id] << startFrom")
        currentItem = item;
        return run(inputAnswer);
    }

    public fun containsItem(id: String) : Boolean{
        return router.contains(id);
    }
}