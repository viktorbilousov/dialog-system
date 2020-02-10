package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.ADialogItem
import models.items.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Dialog : ADialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    private val router: Router
    override val id: String

    override val answers: Array<Answer>
        get() = answersList.toTypedArray()

    private val answersList = ArrayList<Answer>()

    constructor(id: String, router: Router){
        this.id = id;
        this.router = router;
        router.items.values.forEach{
            it.answers.forEach {
                    asw -> if( asw.type == AnswerType.EXIT) {
                        this.answersList.add(asw)
                    }
            }
        }

    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[DIALOG] [$id] >> body DIALOG ")
        var currentItem = router.startPoint;
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
                currentItem = router.get(answer)
            }
        }
    }
    public fun addItem(item: ADialogItem){
        router.addItem(item);
        item.answers.forEach { if(it.type == AnswerType.EXIT) this.answersList.add(it) }
    }

    //todo delete?
    override fun clone(): ADialogItem {
        return Dialog(
            id,
            router
        );
    }

}