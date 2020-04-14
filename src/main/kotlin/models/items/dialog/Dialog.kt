package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.ADialogItem
import models.items.DialogItem
import models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Dialog : ADialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    public val router: Router

    override val id: String
    private var currentItem : DialogItem? = null

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
    }

    override fun body(): Answer {
        logger.info("[DIALOG] [$id] >> body")
        if(currentItem == null) {
            currentItem = router.startPoint
        };
        var answer = Answer.empty()
        while (true) {
            logger.info("[$id] run item: ${currentItem!!.id} ")
            answer =  currentItem!!.run()
            logger.info("[$id] answer is $answer")
            if(answer.type == AnswerType.EXIT || answer.type == AnswerType.ENTER){
                logger.info("[$id] << body DIALOG return ${answer}")
                return answer;
            }else {
                currentItem = router.getNext(answer)
            }
        }
    }
    public fun addItem(item: DialogItem){
        router.addItem(item);
    }

    public fun startFrom(id: String) : Answer? {
        logger.info("[DIALOG] [$id] >> startFrom")
        val item = router.goTo(id) ?: return null;
        logger.info("[DIALOG] [$id] << startFrom")
        currentItem = item;
        return run();
    }

    public fun containsItem(id: String) : Boolean{
        return router.contains(id);
    }
}