package models.items

import models.Answer
import models.Indexable
import models.items.runner.DialogItemRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ADialogItem : Indexable{

    companion object{
        private val logger = LoggerFactory.getLogger(ADialogItem::class.java) as Logger
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

    override fun equals(other: Any?): Boolean {
        if (other == null ) return false;
        else if (other !is ADialogItem) return false

        return other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode() * 32
    }

}