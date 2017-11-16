package brandenc.com.chatmatch

import brandenc.com.chatmatch.Callbacks.LinksCallback
import org.json.JSONObject

/**
 * Auxiliary class used to create the final json output gathered from parsing the input string
 *
 * @constructor Creates a JsonCreator
 * @param textMatcher TextMatcher object that is used to parse the input string
 */
class JsonCreator(private var textMatcher: TextMatcher) {


    /**
     * Callback used by the calling user to get the result from the json creation
     **/
    interface JsonConstructionCallback {

        /**
         * Callback called when json is successfully created
         *
         * @param json the json object generated
         */
        fun onJsonCreatedSuccessful(json: JSONObject)
    }

    /**
     * Creates json objects from the parsed items found by parsing mentions, emojis, and urls
     *
     * @param input the input string from the edit text
     * @param callback the callback where the resulting json is returned
     */
    fun createJson(input: String, callback: JsonConstructionCallback) {
        val mentions = textMatcher.parseMentions(input)
        val emojis = textMatcher.parseEmojis(input)
        textMatcher.parseUrls(input, object: LinksCallback {
            override fun onSuccessfulLinksGathering(links: List<HashMap<String,String>>) {
                val json = JSONObject()

                json.put("mentions", mentions)
                json.put("emojis", emojis)
                json.put("links", links)

                callback.onJsonCreatedSuccessful(json)
            }
        })
    }
}
