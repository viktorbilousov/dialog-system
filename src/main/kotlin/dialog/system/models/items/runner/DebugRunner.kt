package dialog.system.models.items.runner

import dialog.system.models.Answer
import dialog.system.models.items.ADialogItem
import java.lang.IllegalStateException

public class DebugRunner : DialogItemRunner {
    override fun runItem(item: ADialogItem): Answer {
       try {
           enter(item);
           item.before()
           preBody(item)
           val res = item.body()
           postBody(item, res)
           item.after(res);
           exit(item, res)
           return res
       }catch (e: IllegalStateException){
           if(e.message == "restart"){
             return runItem(item);
           }
           throw e;
       }
    }

    public var enter : (item: ADialogItem) -> Unit = {}
    public var exit : (item: ADialogItem, Answer) -> Unit = { _, _-> {} }
    public var preBody : (item: ADialogItem) -> Unit = {}
    public var postBody : (item: ADialogItem, Answer) -> Unit = { _, _ ->{}}

    public val restartFun : () -> Unit = {
        throw IllegalStateException("restart")

    }

    public fun restart(){
        throw IllegalStateException("restart")
    }

    public fun test(){
        println("test")
    }
}