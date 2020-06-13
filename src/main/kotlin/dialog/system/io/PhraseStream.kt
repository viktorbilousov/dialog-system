package dialog.system.io

import dialog.system.models.items.phrase.APhrase
import dialog.system.models.items.text.PhraseText
import dialog.system.models.items.text.PhraseTextFabric

class PhraseStream {
    companion object{
        fun write(phrase: APhrase, file: String){
            PhraseTextStream.write(PhraseText(phrase), file)
        }
        fun read(file: String): List<APhrase>{
            var phrases =  PhraseTextStream.read(file)
            if(phrases == null) {
                phrases = PhraseTextStream.read(file)!!
            }
            return phrases.map { PhraseTextFabric.toPhrase(it) }
        }
    }
}