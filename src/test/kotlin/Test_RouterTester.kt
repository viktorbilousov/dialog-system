import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.answer.Answer
import dialog.system.models.answer.AnswerType
import dialog.system.models.items.ADialogItem
import dialog.system.models.router.Router
import phrases.EmptyPhrase
import dialog.system.models.items.phrase.SimplePhrase
import dialog.system.models.items.text.PhraseText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import dialog.system.models.router.RouterTester
import utils.TestItemsFabric
import java.lang.NullPointerException
import kotlin.streams.toList

class Test_RouterTester {
    @Test
    fun isGraphRelated_good(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = hashMapOf( Pair("id",
            EmptyPhrase("id") as ADialogItem
        ))
        val router = Router("id", graph, items)
        router.startPointId = "1"
        try{
            RouterTester.test(router).isGraphRelated()
        }catch (e: IllegalAccessException){
            assert(false)
        }
        assert(true)
    }

    @Test
     fun isGraphRelated_bad(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = hashMapOf( Pair("id", EmptyPhrase(
            "id"
        ) as ADialogItem
        ))
        val router = Router("id", graph, items)
        assertThrows<NullPointerException> {  RouterTester.test(router).isGraphRelated()}

    }

    @Test
     fun isAllItemsHasVertexes_good(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(5);
        val router = Router("id", graph, items);
        try{
            RouterTester.test(router).isAllItemsHasVertex()
        }catch (e: IllegalAccessException){
            assert(false)
        }
        assert(true);
    }

    @Test
    fun isAllItemsHasVertexes_bad(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(20)
        val router = Router("id", graph, items);

        assertThrows<IllegalAccessException> { RouterTester.test(router).isAllItemsHasVertex()}

    }

    @Test
     fun isAllVertexHasItems_good(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(20)
        val router = Router("id", graph, items);
        try{
            RouterTester.test(router).isAllVertexHasItems()
        }catch (e: IllegalAccessException){
            assert(false)
        }
        assert(true);
    }

    @Test
     fun isAllVertexHasItems_bad(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(10)
        val router = Router("id", graph, items);
        assertThrows<IllegalAccessException> { RouterTester.test(router).isAllVertexHasItems()}
    }

    @Test
    fun emptyVertexList(){
        val graph: Graph = TinkerGraph()
        val items = createMapWithSimplePhrase(10)
        val router = Router("id", graph, items);
        assertThrows<IllegalAccessException> { RouterTester.test(router).isAllVertexHasItems()}
    }

    @Test
     fun emptyItemsList(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = hashMapOf<String, ADialogItem>()
        val router = Router("id", graph, items);
        assertThrows<IllegalAccessException> { RouterTester.test(router).isAllVertexHasItems()}
    }

    @Test
    fun startPoint_good(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(10)
        val router = Router("id", graph, items);
        router.startPointId = "1";
        try{
            RouterTester.test(router).checkStartPoint()
        }catch (e: IllegalAccessException){
            assert(false)
        }
        assert(true);
    }

    @Test
    fun startPoint_bad(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(10)
        val router = Router("id", graph, items);
        router.startPointId = "112445";
        assertThrows<NullPointerException> { RouterTester.test(router).checkStartPoint()}
    }

    @Test
   fun startPointNull(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(10)
        val router = Router("id", graph, items);
        assertThrows<NullPointerException> { RouterTester.test(router).checkStartPoint()}
    }

    @Test
     fun startPointSetNull(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createMapWithSimplePhrase(10)
        val router = Router("id", graph, items);
        router.startPointId = null;
        assertThrows<NullPointerException> { RouterTester.test(router).checkStartPoint()}
    }

    @Test
     fun isItemsLinkedRight_good(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createItemsListToRelatedGraph();
        val router = Router("id", graph, items);
        try{
            RouterTester.test(router).isItemsLinkedCorrectly()
        }catch (e: IllegalAccessException){
            println(e)
            assert(false)
        }
        assert(true);
    }

    @Test
    fun isItemsLinkedRight_bad(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createItemsListToRelatedGraph()
        val router = Router("id", graph, items);
        router.removeItem(items["2"]!!, true)
        assertThrows<IllegalAccessException> {  RouterTester.test(router).isItemsLinkedCorrectly()}
    }

    @Test
    fun isAllTypeCorrect_good(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createItemsListToRelatedGraph()
        val router = Router("id", graph, items);
       try{
           RouterTester.test(router).checkTypesOfPhases()
       }catch (e: Exception){
           println(e)
           assert(false);
       }
        assert(true)
    }

    @Test
    fun isAllTypeCorrect_bad(){
        val graph: Graph = TestItemsFabric.createRelatedTestGraph()
        val items = createItemsListToRelatedGraph()
        items["6"]!!.answers[0].type = AnswerType.EXIT
        val router = Router("id", graph, items)
        assertThrows<IllegalAccessException> {  RouterTester.test(router).checkTypesOfPhases()}
    }


    private fun createMapWithSimplePhrase(maxId: Int): HashMap<String, ADialogItem>{
        val list = hashMapOf<String, ADialogItem>();
        for( i in 1..maxId){
            list[i.toString()] = EmptyPhrase(i.toString()) as ADialogItem
        }
        return list;
    }


    private fun createItemsListToRelatedGraph(): HashMap<String, ADialogItem>{
        val a = hashMapOf<Int, PhraseText>();

        a[1] = PhraseText(
            "1",
            "text 1 ",
            arrayOf(Answer("2", "answ 1"))
        )
        a[2] = PhraseText(
            "2",
            "text 2 ",
            arrayOf(
                Answer("3", "answ 2.1"),
                Answer("4", "answ 2.2")
            )
        )
        a[3] = PhraseText(
            "3",
            "text 3 ",
            arrayOf(
                Answer("5", "answ 3.1"),
                Answer("6", "answ 6.1")
            )
        )
        a[4] = PhraseText(
            "4",
            "text 4 ",
            arrayOf(
                Answer("6", "answ 4.1"),
                Answer("7", "answ 4.2")
            )
        )
        a[5] = PhraseText(
            "5",
            "text 5 ",
            arrayOf(Answer("9", "answ 5"))
        )
        a[6] = PhraseText(
            "6",
            "text 6 ",
            arrayOf(Answer("9", "answ 6"))
        )
        a[7] = PhraseText(
            "7",
            "text 7 ",
            arrayOf(Answer("8", "answ 1"))
        )
        a[8] = PhraseText(
            "8",
            "text 8 ",
            arrayOf(
                Answer(
                    "end 8",
                    "answ 8",
                    AnswerType.EXIT
                )
            )
        )
        a[9] = PhraseText(
            "9",
            "text 9 ",
            arrayOf(Answer("10", "answ 9"))
        )
        a[10] = PhraseText(
            "10",
            "text 10 ",
            arrayOf(
                Answer(
                    "edn 10",
                    "answ 1",
                    AnswerType.EXIT
                )
            )
        )
        a[11] = PhraseText(
            "11",
            "text 11 ",
            arrayOf(Answer("2", "answ 11"))
        )


        return a.values.stream()
            .map{ SimplePhrase(it.id, it.text[0], it.answers) }
            .toList().associate { Pair(it.id, it ) } as HashMap<String, ADialogItem>

    }

}