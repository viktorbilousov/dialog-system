package tools

import models.items.phrase.PhraseChooser

class FirstPhraseChooser : PhraseChooser{
    override fun choose(phrases: Array<String>): String {
        return phrases[0]
    }
}