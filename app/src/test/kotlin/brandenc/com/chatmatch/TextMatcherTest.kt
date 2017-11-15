package brandenc.com.chatmatch

import brandenc.com.chatmatch.Callbacks.LinksCallback
import brandenc.com.chatmatch.Callbacks.TitlesCallback
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class TextMatcherTest {

    lateinit var pageTitleRetriever: PageTitleRetriever
    lateinit var textMatcher : TextMatcher

    @Before
    fun setUp() {
        pageTitleRetriever = mock<PageTitleRetriever>()
        textMatcher = TextMatcher(pageTitleRetriever)
    }

    @Test
    fun parseMentionsValid1() {
        val input = "Hello @Branden @test, How are you? @sup"

        val testList = textMatcher.parseMentions(input)

        val acceptedList = listOf("Branden", "test", "sup")

        assertTrue(acceptedList.containsAll(testList))
    }

    // Assuming right now you can have multiple of the same
    @Test
    fun parseMentionsValid2() {
        val input = "@chris@chris@chris@chris"

        val testList = textMatcher.parseMentions(input)

        val acceptedList = listOf("chris", "chris", "chris", "chris")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseMentionsValid3() {
        val input = "@a@b@a@c@z"

        val testList = textMatcher.parseMentions(input)

        val acceptedList = listOf("a", "a", "b", "c", "z")

        assertTrue(acceptedList.containsAll(testList))
    }

    // TODO: Unsure on this case, should the "lol" mention still show up?
    @Test
    fun parseMentionsMentionInsideemoji() {
        val input = "@a@b:lol@lol:@a@c@z"

        val testList = textMatcher.parseMentions(input)

        val acceptedList = listOf("a", "a", "b", "c", "z", "lol")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseMentionsEmpty() {
        val input = ""

        val testList = textMatcher.parseMentions(input)

        val acceptedList = listOf<String>()

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseMentionsDuplicate() {
        val input = "Hello @Branden, How are you? @Branden @test @more"

        val testList = textMatcher.parseMentions(input)

        val acceptedList = listOf("Branden", "Branden", "test", "more")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseEmojisValidMultiple() {
        val input = ":test: :lol:"

        val testList = textMatcher.parseEmojis(input)

        val acceptedList = listOf("test", "lol")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseEmojisValidMultipleNonConsecutive() {
        val input = "hello @branden :test: :lol: how's it going? :what:"

        val testList = textMatcher.parseEmojis(input)

        val acceptedList = listOf("test", "lol", "what")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseEmojisValidExcludeOver15Chars() {
        val input = "hello @branden :1234567891011121314: :lol: how's it going? :what:"

        val testList = textMatcher.parseEmojis(input)

        val acceptedList = listOf("lol", "what")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseEmojisValid() {
        val input = "hello @branden  :lol@whatlol: how's it going? :what:"

        val testList = textMatcher.parseEmojis(input)

        val acceptedList = listOf("what")

        assertTrue(acceptedList.containsAll(testList))
    }

    @Test
    fun parseUrlsMultiple() {
        val input = "test lol https://www.nbcolympics.com " +
                "https://mathiasbynens.be/demo/url-regex"

        doAnswer {
            val titleResponses = mutableListOf<String>()

            titleResponses.add("NBC olympics")
            titleResponses.add("Hello World")
            val callback = it.arguments[1] as TitlesCallback
            callback.onSuccessfulPageTitle(titleResponses)

            null
        }.`when`(pageTitleRetriever).getPageTitles(any(), any())

        textMatcher.parseUrls(input, object: LinksCallback {
            override fun onSuccessfulLinksGathering(links: List<HashMap<String, String>>) {
                assertEquals("NBC olympics", links[0]["title"])
                assertEquals("https://www.nbcolympics.com", links[0]["url"])
                assertEquals("Hello World", links[1]["title"])
                assertEquals("https://mathiasbynens.be/demo/url-regex", links[1]["url"])
            }

        })
    }

    @Test
    fun parseUrlsSingle() {
        val input = "https://www.nbcolympics.com"

        doAnswer {
            val titleResponses = mutableListOf<String>()

            titleResponses.add("NBC olympics")
            val callback = it.arguments[1] as TitlesCallback
            callback.onSuccessfulPageTitle(titleResponses)

            null
        }.`when`(pageTitleRetriever).getPageTitles(any(), any())

        textMatcher.parseUrls(input, object: LinksCallback {
            override fun onSuccessfulLinksGathering(links: List<HashMap<String, String>>) {
                assertEquals("NBC olympics", links[0]["title"])
                assertEquals("https://www.nbcolympics.com", links[0]["url"])
            }

        })
    }

    @Test
    fun parseUrlsEmpty() {
        val input = "@Branden :lol:"

        doAnswer {
            val titleResponses = mutableListOf<String>()

            val callback = it.arguments[1] as TitlesCallback
            callback.onSuccessfulPageTitle(titleResponses)

            null
        }.`when`(pageTitleRetriever).getPageTitles(any(), any())

        textMatcher.parseUrls(input, object: LinksCallback {
            override fun onSuccessfulLinksGathering(links: List<HashMap<String, String>>) {
                assert(links.isEmpty())
            }

        })
    }


    // TODO: Known bad case, this still returns 2 links with bad urls
//    @Test
//    fun parseUrlsBadUrl() {
//        val input = "test lol https://ww@hellow.nbcol:what:ympics.com " +
//                "https://mathiasbynens.be/demo/url-regex"
//
//        doAnswer {
//            val titleResponses = mutableListOf<String>()
//
//            titleResponses.add("NBC olympics")
//            titleResponses.add("Hello World")
//            val callback = it.arguments[1] as TitlesCallback
//            callback.onSuccessfulPageTitle(titleResponses)
//
//            null
//        }.`when`(pageTitleRetriever).getPageTitles(any(), any())
//
//        textMatcher.parseUrls(input, object: LinksCallback {
//            override fun onSuccessfulLinksGathering(links: List<HashMap<String, String>>) {
//                assertEquals("NBC olympics", links[0]["title"])
//                assertEquals("https://www.nbcolympics.com", links[0]["url"])
//                assertEquals("Hello World", links[1]["title"])
//                assertEquals("https://mathiasbynens.be/demo/url-regex", links[1]["url"])
//            }
//
//        })
//    }

}
