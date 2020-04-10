package models.items.phrase

import models.Answer
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RandomPhrase(id: String, phrases: Array<String>,  answers: Array<Answer> ) : FilteredPhrase(id, phrases, answers) {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    init {
        this.phraseChooser = object : PhraseChooser{
            private val random = Random();
            override fun choose(phrases: Array<String>): String {
                return phrases[random.nextInt(phrases.size)]
            }
        }

    }
}