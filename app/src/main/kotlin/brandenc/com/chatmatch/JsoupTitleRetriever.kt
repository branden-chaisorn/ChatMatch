package brandenc.com.chatmatch

import org.jsoup.Jsoup
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Implementation of TitleRetreiver class utilizing Jsoup
 *
 * @constructor Creates a TitleRetriever
 */
class JsoupTitleRetriever : TitleRetriever {

    private val badRequest = "badRequest"
    private val socketTimeout = "socketTimeout"

    /**
     * Retrieves the page title for the given url
     *
     * Note: If a IOException occurs, the string "badRequest" is returned as the title
     * Note: If a SocketTimeoutException occurs, the string "socketTimeout" is returned as the title
     *
     * @param url the given url
     */
    override fun getTitleForUrl(url: String): String {
        try {
            return Jsoup.connect(url).get().title()
        } catch (e: IOException) {
            return badRequest
        } catch (e: SocketTimeoutException) {
            return socketTimeout
        }
    }
}
