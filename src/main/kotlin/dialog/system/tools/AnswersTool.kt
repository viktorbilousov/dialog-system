package dialog.system.tools

import dialog.system.models.answer.Answer

class AnswersTool {
    companion object{
        public fun copyArrayOrAnswers(arr : Array<Answer>) :Array<Answer>{
            val list = arrayListOf<Answer>()
            arr.forEach { list.add(
                Answer(
                    it.id + "",
                    it.text + "",
                    it.type
                )
            ) }
            return list.toTypedArray()
        }
    }
}