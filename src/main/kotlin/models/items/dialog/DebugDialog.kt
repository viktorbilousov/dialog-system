package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.ADialogItem
import models.items.phrase.APhrase
import models.items.runner.DefaultRunner
import models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

 public open class DebugDialog(id: String, router: Router) : ADialog(id, router) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    }

     init {
         this.runner = DefaultRunner();
     }
    override fun body(): Answer {
        logger.info("[DIALOG] [$id] >> body")
        if(currentItem == null) {
            currentItem = router.startPoint
        };
        var answer: Answer
        while (true) {
            logger.info("[$id] run item: ${currentItem!!.id} ")
            currentItem = transformCurrentItem(currentItem as ADialogItem)
            if(currentItem is APhrase) currentItem = transformIfCurrentItemIsPhrase(currentItem as APhrase)
            if(currentItem is ADialog) currentItem = transformIfCurrentItemIsDialog(currentItem as ADialog)
            answer =  currentItem!!.run()
            answer = transformResultAnswer(answer);
            logger.info("[$id] answer is $answer")
            if(answer.type == AnswerType.EXIT || answer.type == AnswerType.ENTER){
                onExit(answer)
                changeResult(answer)
                logger.info("[$id] << body DIALOG return ${answer}")
                return answer;
            }else {
                currentItem = router.getNext(answer)
            }
        }
    }

     public var transformCurrentItem : (ADialogItem) -> ADialogItem = {it}
     public var transformIfCurrentItemIsPhrase : (APhrase) -> APhrase = {it}
     public fun <T : APhrase> transformIfCurrentItemIsPhrase()  {
         transformIfCurrentItemIsPhrase = {
             APhrase.createFrom<T>(it)
         }
     }
     public fun <T: ADialog> transformIfCurrentItemIsDialog() {
         transformIfCurrentItemIsDialog = {
             ADialog.createFrom<T>(it)
         }
     }
     public var transformIfCurrentItemIsDialog : (ADialog) -> ADialog = {it}
     public var transformResultAnswer : (Answer) -> Answer = {it}
     public var onExit: (Answer) -> Unit = {}
     public var changeResult: (Answer) -> Answer = {it}

}