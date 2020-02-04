package models

class Answer : Indexable {

    private var id : String;
    private var type = AnswerType.SIMPLE
    get set
    constructor(){
        id = "";
    }
    constructor(id: String){
        this.id = id;
    }


    constructor(id: String, answerType: AnswerType){
        this.id = id
        this.type = answerType;
    }


    override fun getId(): String {
        return id;
    }
}