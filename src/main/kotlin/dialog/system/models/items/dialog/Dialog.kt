package dialog.system.models.items.dialog

import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

 open class Dialog(id: String, router: Router) : ADialog(id, router) {

    companion object{
        private val logger = LoggerFactory.getLogger(Dialog::class.java) as Logger
    }

     constructor(id: String): this(id, Router(id))


    override fun body(): Answer {
        logger.info("[DIALOG] [$id] >> body")
        if(currentItem == null) {
            currentItem = router.startPoint
        }
        var answer: Answer
        while (true) {
            logger.info("[$id] run item: ${currentItem!!.id} ")
            answer =  currentItem!!.run()
            logger.info("[$id] answer is $answer")
            if(answer.type == AnswerType.EXIT || answer.type == AnswerType.ENTER){
                logger.info("[$id] << body DIALOG return ${answer}")
                return answer
            }else {
                currentItem = router.getNext(answer)
            }
        }
    }

}