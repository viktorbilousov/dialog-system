package dialog.system.models.items.phrase

interface PhraseChooser {
    public fun choose(phrases: Array<String>): String
}