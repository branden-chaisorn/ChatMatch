package brandenc.com.chatmatch

import brandenc.com.chatmatch.Callbacks.LinksCallback
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.junit.Assert.*

class JsonCreatorTest {

    private lateinit var textMatcher: TextMatcher
    private lateinit var jsonCreator: JsonCreator

    @Before
    fun setUp() {
        textMatcher = mock()
        jsonCreator = JsonCreator(textMatcher)
    }

    @Test
    fun createJsonSingleLink() {
        Mockito.`when`(textMatcher.parseEmojis(any())).thenReturn(listOf("hello", "lol"))
        Mockito.`when`(textMatcher.parseMentions(any())).thenReturn(listOf("branden", "jason"))
        doAnswer {
            val links = mutableListOf<HashMap<String, String>>()

            val link1 = hashMapOf<String, String>()
            link1.put("url", "https://www.nbcolympics.com")
            link1.put("title", "Olympics")

            links.add(link1)

            val callback = it.arguments[1] as LinksCallback
            callback.onSuccessfulLinksGathering(links)

            null
        }.`when`(textMatcher).parseUrls(any(), any())

        val expectedJson = JSONObject()
        expectedJson.put("emojis", listOf("hello", "lol"))
        expectedJson.put("mentions", listOf("branden", "jason"))

        val expectedLinkMap = hashMapOf<String, String>()
        expectedLinkMap.put("url", "https://www.nbcolympics.com")
        expectedLinkMap.put("title", "Olympics")
        expectedJson.put("links", listOf(expectedLinkMap))


        jsonCreator.createJson(":hello: @branden :lol: this is @jason from " +
                "https://www.nbcolympics.com", object : JsonCreator.JsonConstructionCallback {
            override fun onJsonCreatedSuccessful(json: JSONObject) {
                assertEquals(expectedJson.toString(), json.toString())
            }

        })
    }

    @Test
    fun createJsonempty() {
        Mockito.`when`(textMatcher.parseEmojis(any())).thenReturn(listOf())
        Mockito.`when`(textMatcher.parseMentions(any())).thenReturn(listOf())
        doAnswer {
            val links = mutableListOf<HashMap<String, String>>()
            val callback = it.arguments[1] as LinksCallback
            callback.onSuccessfulLinksGathering(links)

            null
        }.`when`(textMatcher).parseUrls(any(), any())

        val expectedJson = JSONObject()
        expectedJson.put("emojis", listOf<String>())
        expectedJson.put("mentions", listOf<String>())
        expectedJson.put("links", listOf<String>())

        jsonCreator.createJson("empty", object : JsonCreator.JsonConstructionCallback {
            override fun onJsonCreatedSuccessful(json: JSONObject) {
                assertEquals(expectedJson.toString(), json.toString())
            }

        })
    }

    @Test
    fun createJsonMultipleLink() {
        Mockito.`when`(textMatcher.parseEmojis(any())).thenReturn(listOf("hello", "lol", "what"))
        Mockito.`when`(textMatcher.parseMentions(any())).thenReturn(listOf("branden", "jason", "b"))
        doAnswer {
            val links = mutableListOf<HashMap<String, String>>()

            val link1 = hashMapOf<String, String>()
            link1.put("url", "https://www.nbcolympics.com")
            link1.put("title", "Olympics")

            val link2 = hashMapOf<String, String>()
            link2.put("url", "https://www.google.com")
            link2.put("title", "Google")

            links.add(link1)
            links.add(link2)

            val callback = it.arguments[1] as LinksCallback
            callback.onSuccessfulLinksGathering(links)

            null
        }.`when`(textMatcher).parseUrls(any(), any())

        val expectedJson = JSONObject()
        expectedJson.put("emojis", listOf("hello", "lol", "what"))
        expectedJson.put("mentions", listOf("branden", "jason", "b"))

        val expectedLinkMap1 = hashMapOf<String, String>()
        expectedLinkMap1.put("url", "https://www.nbcolympics.com")
        expectedLinkMap1.put("title", "Olympics")

        val expectedLinkMap2 = hashMapOf<String, String>()
        expectedLinkMap2.put("url", "https://www.google.com")
        expectedLinkMap2.put("title", "Google")


        expectedJson.put("links", listOf(expectedLinkMap1, expectedLinkMap2))

        jsonCreator.createJson(":hello: @branden :lol: :what: this is @jason from " +
                "https://www.nbcolympics.com @b https://wwww.google.com ",
                object : JsonCreator.JsonConstructionCallback {
                    override fun onJsonCreatedSuccessful(json: JSONObject) {
                        assertEquals(expectedJson.toString(), json.toString())
                    }

        })
    }
}
