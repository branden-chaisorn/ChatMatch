package brandenc.com.chatmatch

import org.jsoup.Jsoup
import java.io.IOException

/**
 * Implementation of TitleRetreiver class utilizing Jsoup
 *
 * @constructor Creates a TitleRetriever
 */
class JsoupTitleRetriever : TitleRetriever {

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
            return "badURL"
        }
    }
}
