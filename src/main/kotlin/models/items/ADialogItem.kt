package models.items

import models.Answer
import models.Indexable
import models.items.runner.DialogItemRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ADialogItem : Indexable{

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    abstract var runner: DialogItemRunner
    public abstract val answers: Array<Answer>

    public var before: () -> Unit = {}
    public var after: (outputAnswer: Answer) -> Unit = {}

    abstract fun body(): Answer

    fun run(): Answer{
        logger.info("[${id}] >> run")
        val res = runner.runItem(this)
        logger.info("[${id}] << run")
        return res;
    }

}