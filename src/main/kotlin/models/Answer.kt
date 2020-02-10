package models

import com.beust.klaxon.Json

 class Answer : Indexable {

     @Json(name = "answ_id")
     override val id: String

     @Json(name = "Text")
     public var text: String

     @Json(name = "type")
     public var type = AnswerType.SIMPLE


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