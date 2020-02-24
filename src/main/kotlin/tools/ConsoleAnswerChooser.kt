package tools

import models.Answer
import models.items.phrase.AnswerChooser
import java.util.*

class ConsoleAnswerChooser : AnswerChooser{

    override fun chooseAnswer(answers: Array<Answer>): Answer {
        return input(answers);
    }

    public fun input(answers: Array<Answer>): Answer{
        while (true) {
            println("Enter the number:\n>")
            val input = Scanner(System.`in`)
            val stringInput = input.nextLine()
            val num = stringInput.toIntOrNull();
            if (num == null || num > answers.size || num < 1) {
                println("InputError: please enter number")
                continue
            }
            return answers[num-1]
        }
    }
}