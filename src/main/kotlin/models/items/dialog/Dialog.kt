package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.IDialogItem
import models.items.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Dialog : IDialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    private val id: String
    private val router: Router

    constructor(id: String, router: Router){
        this.id = id;
        this.router = router;
    }

    override fun getId(): String {
        return id;
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info("[DIALOG] [$id] >> body DIALOG ")
        var currentItem = router.startPoint;
        var answer = inputAnswer;
        while (true) {
            logger.info("[$id] Run item: ${currentItem.getId()} ")
            answer =  currentItem.run(answer)
            logger.info("[$id] answer is ${answer.getId()}, Type: ${answer.type}")
            if(answer.type == AnswerType.EXIT){
                logger.info("return ${answer.getId()}")
                logger.info("[$id] << body DIALOG: $id")
                answer.type=AnswerType.SIMPLE;
                return answer;
            }else if (answer.type == AnswerType.ENTER) {
                currentItem = router.get(answer) // todo ?
            }else {
                currentItem = router.get(answer)
            }
        }
    }
    public fun addItem(item: IDialogItem){
        router.addItem(item);
    }

    override fun clone(): IDialogItem {
        return Dialog(
            id,
            router
        );
    }
}