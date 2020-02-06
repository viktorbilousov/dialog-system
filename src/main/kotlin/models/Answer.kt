package models

class Answer : Indexable {

    private var id : String;
    public var type = AnswerType.SIMPLE
        get set;
    public var text: String
        get set;

    constructor(id: String, text: String){
        this.id = id;
        this.text = text;
    }

    constructor(id: String, text: String, answerType: AnswerType) : this(id, text) {
        this.type = answerType;
    }


    override fun getId(): String {
        return id;
    }

    override fun toString(): String {
        return "{id=$id, type=$type}"
    }
}