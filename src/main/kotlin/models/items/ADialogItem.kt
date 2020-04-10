package models.items

import models.Answer
import models.Indexable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ADialogItem : DialogItem{

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }


    public var before: () -> Unit = {}
    public var after: (outputAnswer: Answer) -> Unit = {}

    abstract fun body(): Answer

    override fun run(): Answer{
        logger.info("[${id}] >> run")
        before()
        val res = body()
        after(res);
        logger.info("[${id}]  << run")
        return res
    }

}