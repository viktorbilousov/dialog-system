package dialog.system.models.items.text

import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.APhrase

class PhraseTextFabric {
    companion object {
        public fun toPhrase(pt: PhraseText): APhrase {
            val clazz = Class.forName(pt.clazz)
            clazz.constructors.forEach {
                //id: String, phrases: Array<String>,  answers: Array<Answer>
                if (it.parameterCount == 3
                    && it.parameters[0].type == String::class.java
                    && it.parameters[1].type == Array<String>::class.java
                    && it.parameters[2].type == Array<Answer>::class.java
                ) {
                    return it.newInstance(pt.id, pt.text, pt.answers) as APhrase
                }
            }
            throw ClassNotFoundException()
        }
    }
}
