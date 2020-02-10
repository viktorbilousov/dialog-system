package models.items

import models.Answer
import models.Indexable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class DialogItem : Indexable {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    abstract val answers: Array<Answer>
    get;


    public var before: (inputAnswer: Answer) -> Unit = {
        logger.info("[${id}] >> before (empty)")
        logger.info("[${id}] << before (empty)")
    }

    public var after: (outputAnswer: Answer) -> Unit = {
        logger.info("[${id}] >> after (empty)")
        logger.info("[${id}] << after (empty)")
    }


   abstract fun body(inputAnswer: Answer) : Answer

    fun run(inputAnswer: Answer): Answer{
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