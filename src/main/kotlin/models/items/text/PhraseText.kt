package models.items.text

import com.beust.klaxon.Json
import models.Answer
import models.Indexable
import models.items.phrase.SimplePhrase

class PhraseText: Indexable {

    @Json(name = "texts")
    public val text: Array<String>
    @Json(name = "answers")
    public val answers: Array<Answer>
    @Json(name = "id")
    override val id: String

    @Json(name="class")
    public val clazz : String = SimplePhrase::class.java.name;

    constructor(id: String, text: Array<String>, answers: Array<Answer>)  {
        this.id = id
        this.text = text
        this.answers = answers
    }

    constructor(id: String, text: String, answers: Array<Answer>)  {
        this.id = id
        this.text = arrayOf(text)
        this.answers = answers
    }

    override fun toString(): String {
        return "{phrase text: id=$id, phrases=${text.contentToString()}, answers=${answers.contentToString()}}"
    }
}