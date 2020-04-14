package debug.items

import models.Answer
import models.items.DialogItem
import java.lang.IllegalStateException


abstract class ADebugDialogItem: DialogItem {

    public abstract val before : () -> Unit
    public abstract val after: (Answer) -> Unit
    public abstract val body: () -> Answer

    public var init: (ADebugDialogItem) -> Unit = {}
    public var beforeBodyRun : (ADebugDialogItem) -> Unit = { }
    public var afterBodyRun : (inputAnswer: Answer, it: ADebugDialogItem) -> Answer = { inputAnswer, _ -> inputAnswer  }
    public var exit : (inputAnswer: Answer, it: ADebugDialogItem) -> Answer = { inputAnswer, _ -> inputAnswer  }


    override fun run(): Answer {
        try {
            init(this)
            before()
            beforeBodyRun(this)
            var res = body()
            res = afterBodyRun(res, this)
            after(res);
            res = exit(res, this)
            return res
        }catch (e: IllegalStateException){
            if(e.message == "need restart") return run();
            throw e;
        }
    }
    fun restart(): Answer{
        throw IllegalStateException("need restart")
    }
}