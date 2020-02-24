package models.items.phrase

import models.Answer

interface PhraseChooser {
    public fun choose(phrases: Array<String>): String
}