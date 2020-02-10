package tools
import models.Answer
import java.lang.StringBuilder
import java.util.*

class PhrasePrinter {
    companion object{
        public fun createMessage(text: String, answer: Array<Answer>): String{
            val builder = StringBuilder();
            builder.append(text);
            builder.append("\n\n\n")
            for(i in 0 until answer.size){
                builder.append("[${i+1}] ${answer[i].text}\n")
            }
            return builder.toString()
        }
        public fun input(answer: Array<Answer>): Answer{
            while (true) {
                println("Enter the number:\n>")
                val input = Scanner(System.`in`)
                val stringInput = input.nextLine()
                val num = stringInput.toIntOrNull();
                if (num == null || num > answer.size || num < 1) {
                    println("InputError: please enter number")
                    continue
                }
                return answer[num-1]
            }
        }

        public fun printTextDialog(text: String, answer: Array<Answer>): Answer{
            println(createMessage(text, answer));
            return input(answer);
        }
    }
}