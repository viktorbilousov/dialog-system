import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import models.Answer
import models.AnswerType
import models.items.text.PhraseText
import models.items.text.PhraseTextStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.FileNotFoundException
import java.io.FileReader
import java.lang.IllegalArgumentException

class Test_SimplePhraseTextStream {

    @Test
    fun readManyFromFile() {

        val pathToFile = "./src/test/resources/test_readMultiplyPhraseText.json"
        val expectedInput = Klaxon().parseJsonArray(FileReader(pathToFile))
        val actualInput = PhraseTextStream.readMany(pathToFile) as Array<PhraseText>

        assertEquals(expectedInput.size, actualInput.size)

        for (i in actualInput.indices) {
            testPhraseTextWithJsonObject(actualInput[i], expectedInput[i] as JsonObject)
        }
    }

    @Test
    fun readOneFromFile() {

        val pathToFile = "./src/test/resources/test_readSinglePhraseText.json"
        val input = Klaxon().parseJsonObject(FileReader(pathToFile))
        val actualPhraseText = PhraseTextStream.readOne(pathToFile) as PhraseText

        testPhraseTextWithJsonObject(actualPhraseText, input)

    }

    @Test fun readOne_EmptyFile(){
        val pathToFile = "./src/test/resources/writeSinglePhraseText1234.json"
        assertThrows<FileNotFoundException>{ PhraseTextStream.readOne(pathToFile)}
    }
    @Test fun readMany_EmptyFile(){
        val pathToFile = "./src/test/resources/writeSinglePhraseText1234.json"
        assertThrows<FileNotFoundException>{ PhraseTextStream.readMany(pathToFile)}
    }

    @Test fun readMany_objectFile(){
        val pathToFile = "./src/test/resources/test_readSinglePhraseText.json"
        assertThrows<IllegalArgumentException>{PhraseTextStream.readMany(pathToFile)}
    }

    @Test fun readOne_arrayFile(){
        val pathToFile = "./src/test/resources/test_readMultiplyPhraseText.json"
        assertThrows<IllegalArgumentException>{PhraseTextStream.readOne(pathToFile)}
    }

    @Test fun readOne_badFile(){
        val pathToFile = "./src/test/resources/test_readSinglePhraseText_bad.json"
        assertThrows<IllegalArgumentException>{PhraseTextStream.readOne(pathToFile)}
    }

    @Test fun readMany_badFile(){
        val pathToFile = "./src/test/resources/test_readMultiplyPhraseText_bad.json"
        assertThrows<IllegalArgumentException>{PhraseTextStream.readOne(pathToFile)}
    }

    @Test
    fun writeManyToFile() {
        val pathToFile = "./src/test/resources/writeMultiplyPhraseText.json"
        val expectedIdArray = arrayOf("test_id1", "test_id2", "test_id3")
        val expectedTextsArray = arrayOf(
            createArrayOfString(15).copyOfRange(0, 4),
            createArrayOfString(15).copyOfRange(5, 9),
            createArrayOfString(15).copyOfRange(10, 14)
        )
        val expectedAnswersArray = arrayOf(
            createArrayOfAnswers(15).copyOfRange(0, 4),
            createArrayOfAnswers(15).copyOfRange(5, 9),
            createArrayOfAnswers(15).copyOfRange(10, 14)
        )


        val phraseTexts = arrayListOf<PhraseText>()
        for (i in expectedIdArray.indices) {
            phraseTexts.add(
                PhraseText(
                    expectedIdArray[i],
                    expectedTextsArray[i],
                    expectedAnswersArray[i]
                )
            )
        }


        PhraseTextStream.write(phraseTexts.toTypedArray(), pathToFile);

        val input = Klaxon().parseJsonArray(FileReader(pathToFile));
        assertEquals(input.size, phraseTexts.size)

        for (i in 0 until phraseTexts.size) {
            testPhraseTextWithJsonObject(
                expectedIdArray[i],
                expectedTextsArray[i],
                expectedAnswersArray[i],
                input[i] as JsonObject)
        }
    }

    @Test
    fun writeOneToFile() {

        val pathToFile = "./src/test/resources/writeSinglePhraseText.json"
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
        testPhraseTextWithJsonObject(expectedId, expectedTexts, expectedAnswers, input)
    }




    private fun createTestPhraseText(): PhraseText {
        return createTestPhraseText(20, 50);
    }

    private fun createTestPhraseText(numTexts: Int, numAnswers: Int): PhraseText {
        return PhraseText(
            "test_phrase",
            createArrayOfString(numTexts),
            createArrayOfAnswers(numAnswers)
        )
    }

    private fun createArrayOfAnswers(size: Int): Array<Answer> {
        val list = arrayListOf<Answer>()
        for (i in 0 until size) {
            list.add(
                Answer("answer.$i", "ANSWER TEXT $i")
            )
        }
        return list.toTypedArray()
    }

    private fun createArrayOfString(size: Int): Array<String> {
        val list = arrayListOf<String>()
        for (i in 0 until size) {
            list.add("text $i")
        }
        return list.toTypedArray();
    }

    private fun testPhraseTextWithJsonObject(phraseText: PhraseText, phraseTextJson: JsonObject) {
        testPhraseTextWithJsonObject(phraseText.id, phraseText.text, phraseText.answers, phraseTextJson)
    }

    private fun testPhraseTextWithJsonObject(
        phraseId: String,
        phraseTexts: Array<String>,
        phraseAnswers: Array<Answer>,
        phraseTextJson: JsonObject
    ) {

        val jsonId = phraseTextJson["id"] as String;
        val jsonAnswers = (phraseTextJson["answers"] as JsonArray<JsonObject>).toTypedArray()
        val jsonTexts = (phraseTextJson["texts"] as JsonArray<String>).toTypedArray()

        assertEquals(jsonId, phraseId);

        assertEquals(phraseAnswers.size, jsonAnswers.size)
        assertEquals(phraseTexts.size, jsonTexts.size)

        for (i in phraseTexts.indices) {
            assertEquals(phraseTexts[i], jsonTexts[i]);
        }

        for (i in phraseAnswers.indices) {
            assertEquals(jsonAnswers[i]["answ_id"], phraseAnswers[i].id);
            assertEquals(jsonAnswers[i]["text"], phraseAnswers[i].text);
            assertEquals(jsonAnswers[i]["type"], phraseAnswers[i].type.name);
        }
    }
}