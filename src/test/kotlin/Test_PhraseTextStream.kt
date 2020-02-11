import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import models.Answer
import models.AnswerType
import models.items.text.PhraseText
import models.items.text.PhraseTextStream
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.FileReader

class Test_PhraseTextStream {

    @Test fun readManyFromFile(){}
    @Test fun readOneFromFile(){}
    @Test fun writeManyToFile(){
        val pathToFile = "src\\test\\resources\\writeMultiplyPhraseText.json"
        val expectedIdArray = arrayOf("test_id1", "test_id2", "test_id3")
        val expectedTextsArray = arrayOf(
            createArrayOfString(15).copyOfRange(0,4),
            createArrayOfString(15).copyOfRange(5,9),
            createArrayOfString(15).copyOfRange(10,14)
        )
        val expectedAnswersArray = arrayOf(
            createArrayOfanswers(15).copyOfRange(0,4),
            createArrayOfString(15).copyOfRange(5,9),
            createArrayOfString(15).copyOfRange(10,14)
        )


       val phraseTexts = arrayListOf<PhraseText>()
        for(i in expectedIdArray.indices){
            phraseTexts.add(
                PhraseText(
                    expectedIdArray[i],
                    expectedTextsArray[i],
                    expectedAnswersArray[i]
                )
            )
        }


        PhraseTextStream.write(phrase, pathToFile);

        val input = Klaxon().parseJsonObject(FileReader(pathToFile));
        val actualID = input["id"] as String;
        val actualAnswers = (input["answers"] as JsonArray<JsonObject>).toTypedArray()
        val actualTexts = (input["texts"] as JsonArray<String>).toTypedArray()
        val actualClass = input["class"] as String

        assertEquals(actualID, expectedId);
        assertNotNull(actualClass);

        assertEquals(actualAnswers.size, expectedAnswers.size )
        assertEquals(actualTexts.size, expectedTexts.size )

        for (i in actualTexts.indices){
            assertEquals(actualTexts[i], expectedTexts[i]);
        }

        for (i in actualAnswers.indices){
            assertEquals( actualAnswers[i]["answ_id"],  expectedAnswers[i].id );
            assertEquals( actualAnswers[i]["text"],  expectedAnswers[i].text );
            assertEquals( actualAnswers[i]["type"],  expectedAnswers[i].type.name );
        }

    }
    @Test fun writeOneToFile(){

        val pathToFile = "src\\test\\resources\\writeSinglePhraseText.json"
        val expectedId = "test_id"
        val expectedTexts = arrayOf("text1", "text2", "text3");
        val expectedAnswers = arrayOf(
            Answer("answer1", "answer text 1", AnswerType.SIMPLE),
            Answer("answer2", "answer text 2", AnswerType.ENTER),
            Answer("answer3", "answer text 2", AnswerType.EXIT)
        )
        val phrase = PhraseText(
            expectedId,
            expectedTexts,
            expectedAnswers
        )

        PhraseTextStream.write(phrase, pathToFile);

        val input = Klaxon().parseJsonObject(FileReader(pathToFile));
        val actualID = input["id"] as String;
        val actualAnswers = (input["answers"] as JsonArray<JsonObject>).toTypedArray()
        val actualTexts = (input["texts"] as JsonArray<String>).toTypedArray()
        val actualClass = input["class"] as String

        assertEquals(actualID, expectedId);
        assertNotNull(actualClass);

        assertEquals(actualAnswers.size, expectedAnswers.size )
        assertEquals(actualTexts.size, expectedTexts.size )

        for (i in actualTexts.indices){
            assertEquals(actualTexts[i], expectedTexts[i]);
        }

        for (i in actualAnswers.indices){
            assertEquals( actualAnswers[i]["answ_id"],  expectedAnswers[i].id );
            assertEquals( actualAnswers[i]["text"],  expectedAnswers[i].text );
            assertEquals( actualAnswers[i]["type"],  expectedAnswers[i].type.name );
        }
    }


    private fun createTestPhraseText(): PhraseText{
       return createTestPhraseText(20,50);
    }

    private fun createTestPhraseText(numTexts: Int, numAnswers : Int): PhraseText{
        return PhraseText(
            "test_phrase",
            createArrayOfString(numTexts),
            createArrayOfanswers(numAnswers)
        )
    }

    private fun createArrayOfanswers(size: Int) : Array<Answer>{
        val list=  arrayListOf<Answer>()
        for (i in 0 until size){
            list.add(
                Answer("answer.$i", "ANSWER TEXT $i")
            )
        }
        return list.toTypedArray()
    }

    private fun createArrayOfString(size: Int):Array<String>{
        val list=  arrayListOf<String>()
        for(i in 0 until size){
            list.add("text $i")
        }
        return list.toTypedArray();
    }
}