package models.items

import models.Answer
import models.Indexable;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface IDialogItem : Indexable {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    fun clone() : IDialogItem;

    open fun before(inputAnswer: Answer){
        logger.info("[${getId()}] >> before (empty)")
        logger.info("[${getId()}] << before (empty)")
    }
    open fun after(outputAnswer: Answer){
        logger.info("[${getId()}] >> after (empty)")
        logger.info("[${getId()}] << after (empty)")
    }
    fun body(inputAnswer: Answer) : Answer

    fun run(inputAnswer: Answer): Answer{
        logger.info("[${getId()}] >> run")
        logger.info("[${getId()}] call before")
        before(inputAnswer)
        logger.info("[${getId()}] end before")
        logger.info("[${getId()}] call body")
        val res = body(inputAnswer)
        logger.info("[${getId()}]  end body")
        logger.info("[${getId()}] call after")
        after(res);
        logger.info("[${getId()}]  end body")
        return res
    }
}