package tools
import models.Answer
import models.items.phrase.PhrasePrinter
import java.lang.StringBuilder
import java.util.*

class SimplePhrasePrinter : PhrasePrinter {

        public fun createMessage(text: String, answer: Array<Answer>): String{
            val builder = StringBuilder();
            builder.append(text);
            builder.append("\n\n\n")
            for(i in 0 until answer.size){
                builder.append("[${i+1}] ${answer[i].text}\n")
            }
            return builder.toString()
        }


        public override fun printTextDialog(text: String, answer: Array<Answer>){
            println(createMessage(text, answer));
        }

}