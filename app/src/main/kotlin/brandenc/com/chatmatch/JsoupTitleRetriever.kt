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

    private val badUrl = "badURL"
    private val timeoutException = "timeoutException"

    /**
     * Retrieves the page title for the given url
     *
     * @param url the given url
     * @exception IOException thrown if a bad URL is encountered, returns badURL as string for
     * further error handling
     */
    override fun getTitleForUrl(url: String): String {
        try {
            return Jsoup.connect(url).get().title()
        } catch (e: IOException) {
            return badUrl
        } catch (e: SocketTimeoutException) {
            return timeoutException
        }
    }
}
