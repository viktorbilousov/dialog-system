package models.items

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ADialogItem : DialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    abstract val answers: Array<Answer>
    get;

    override fun before(inputAnswer: Answer){
        logger.info("[${id}] >> before (empty)")
        logger.info("[${id}] << before (empty)")
    }
    override fun after(outputAnswer: Answer){
        logger.info("[${id}] >> after (empty)")
        logger.info("[${id}] << after (empty)")
    }
   abstract override fun body(inputAnswer: Answer) : Answer

    override fun run(inputAnswer: Answer): Answer{
        logger.info("[${id}] >> run")
        logger.info("[${id}] call before")
        before(inputAnswer)
        logger.info("[${id}] end before")
        logger.info("[${id}] call body")
        val res = body(inputAnswer)
        logger.info("[${id}]  end body")
        logger.info("[${id}] call after")
        after(res);
        logger.info("[${id}]  end body")
        return res
    }

}