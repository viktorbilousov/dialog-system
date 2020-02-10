package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.IDialogItem
import models.items.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.streams.toList

class Dialog : IDialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    private val id: String
    private val router: Router
    private val answers = mutableListOf<Answer>();

    constructor(id: String, router: Router){
        this.id = id;
        this.router = router;
        router.items.values.forEach{
            it.getAnswers().forEach {
                    asw -> if( asw.type == AnswerType.EXIT) {
                        answers.add(asw)
                    }
            }
        }
    }

    override fun getId(): String {
        return id;
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[DIALOG] [$id] >> body DIALOG ")
        var currentItem = router.startPoint;
        var answer = inputAnswer;
        while (true) {
            logger.info("[$id] run item: ${currentItem.getId()} ")
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
    public fun addItem(item: IDialogItem){
        router.addItem(item);
        item.getAnswers().forEach { if(it.type == AnswerType.EXIT) answers.add(it) }
    }

    //todo delete?
    override fun clone(): IDialogItem {
        return Dialog(
            id,
            router
        );
    }

    override fun getAnswers(): Array<Answer> {
        return answers.toTypedArray();
    }
}