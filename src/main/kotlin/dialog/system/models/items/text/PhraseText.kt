package dialog.system.models.items.text

import com.beust.klaxon.Json
import dialog.system.models.Answer
import dialog.system.models.Indexable
import dialog.system.models.items.phrase.SimplePhrase

class PhraseText: Indexable {

    @Json(name = "texts")
    public val text: Array<String>
    @Json(name = "answers")
    public val answers: Array<Answer>
    @Json(name = "id")
    override val id: String

    @Json(name="class")
    public val clazz : String 


    companion object{
        private val DEF_CLASS = SimplePhrase::class.java.name.toString()
    }


    constructor(id: String, text: Array<String>, answers: Array<Answer>): this(id, text, answers,
        DEF_CLASS
    ) {}

    constructor(id: String, text: String, answers: Array<Answer>): this(id, text, answers,
        DEF_CLASS
    ) {}


    constructor(id: String, text: Array<String>, answers: Array<Answer>, clazz: String)  {
        this.id = id
        this.text = text
        this.answers = answers
        this.clazz = clazz
    }


    constructor(id: String, text: String, answers: Array<Answer>, clazz: String)  {
        this.id = id
        this.text = arrayOf(text)
        this.answers = answers
        this.clazz = clazz
    }

    override fun toString(): String {
        return "{phrase text: class=${this.clazz}, id=$id, phrases=${text.contentToString()}, answers=${answers.contentToString()}}"
    }
}