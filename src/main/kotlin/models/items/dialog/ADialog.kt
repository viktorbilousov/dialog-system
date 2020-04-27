package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.ADialogItem
import models.items.phrase.APhrase
import models.items.runner.DefaultRunner
import models.items.runner.DialogItemRunner
import models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

abstract class ADialog : ADialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(ADialog::class.java) as Logger

        public inline fun <reified T : ADialog> convertTo(dialog: ADialog): T{
            try {
                val res = T::class.java
                    .getConstructor(String::class.java, Router::class.java)
                    .newInstance(dialog.id, dialog.router) as T;
                res.initFrom(dialog);
                return res;
            }catch (e: Exception){
                val logger = LoggerFactory.getLogger(ADialog::class.java) as Logger
                logger.error("class not has constructor (String::class.java, Router::class.java)", e)
                throw InstantiationError("class not has constructor (String::class.java, Router::class.java)")
            }
        }
    }


    abstract override fun body(): Answer

    public val router: Router

    override var runner: DialogItemRunner = DefaultRunner();
    override val id: String
    protected var currentItem : ADialogItem? = null

    constructor(id: String, router: Router){
        this.id = id;
        this.router = router;
    }

    constructor(dialog: ADialog): this(dialog.id, dialog.router)

    override val answers: Array<Answer>
        get() {
            val answersList = arrayListOf<Answer>()
            if(router.items == null) {
                throw IllegalAccessException("ItemsSet is null!")
            }
            router.items!!.values.forEach {
                it.answers.forEach { asw ->
                    if (asw.type == AnswerType.EXIT) {
                        answersList.add(asw)
                    }
                }
            }
            return answersList.toTypedArray()
        }


    public fun addItem(item: ADialogItem){
        router.addItem(item);
    }

    public fun startFrom(id: String) : Answer? {
        logger.info("[DIALOG] [$id] >> startFrom")
        val item = router.goTo(id) ?: return null;
        logger.info("[DIALOG] [$id] << startFrom")
        currentItem = item;
        return run();
    }

    public fun containsItem(id: String) : Boolean{
        return router.contains(id);
    }
}