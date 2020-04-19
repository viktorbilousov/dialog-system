package models.items.dialog
import models.Answer;
import models.AnswerType
import models.items.ADialogItem
import models.items.runner.DefaultRunner
import models.items.runner.DialogItemRunner
import models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ADialog : ADialogItem {

    companion object{
        private val logger = LoggerFactory.getLogger(ADialog::class.java) as Logger

        public inline fun <reified T: ADialog> createFrom(dialog: ADialog): T{
            T::class.java.constructors.forEach {
                if (it.parameterCount == 2
                    && it.parameters[0].type == String::class.java
                    && it.parameters[1].type == Router::class.java
                ) {
                    return it.newInstance(dialog.id, dialog.router) as T;
                }
            }
            throw InstantiationError("class not has the constructor (id: String, router: Router)")
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