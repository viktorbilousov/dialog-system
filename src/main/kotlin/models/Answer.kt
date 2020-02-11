package models

import com.beust.klaxon.Json

 class Answer : Indexable {

     @Json(name = "answ_id")
     override val id: String

     @Json(name = "text")
     public var text: String

     @Json(name = "type")
     public var type = AnswerType.SIMPLE

     companion object{
         public fun empty () : Answer{
             return Answer("", "")
         }
         public fun enter() : Answer{
             return Answer("enter", "", AnswerType.ENTER)
         }
         public fun enter(id: String) : Answer{
             return Answer(id, "", AnswerType.ENTER)
         }
         public fun exit() : Answer{
             return Answer("exit", "", AnswerType.EXIT)
         }
     }

     constructor(id: String, text: String) {
         this.id = id;
         this.text = text;
         this.type = AnswerType.SIMPLE;
     }

     constructor(id: String, text: String, answerType: AnswerType) : this(id, text) {
         this.type = answerType;
     }

     override fun toString(): String {
         return "{id:$id, text:'$text', type=$type}"
     }
 }