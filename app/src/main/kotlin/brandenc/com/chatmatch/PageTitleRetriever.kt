package brandenc.com.chatmatch

import brandenc.com.chatmatch.Callbacks.TitlesCallback
import org.jetbrains.anko.doAsync

/**
 * Auxiliary class used to grab the titles from the provided urls
 *
 * @constructor Creates a PageTitleRetriever
 */
class PageTitleRetriever(private val titleRetriever: TitleRetriever) {

    /**
     * Retrieves the page titles for the given urls
     *
     * @param urls the list of urls to gather the titles from
     * @param titlesCallback the callback where the successful information will be returned
     */
    fun getPageTitles(urls: List<String>, titlesCallback: TitlesCallback) {
        doAsync {
            val titles = urls.map { titleRetriever.getTitleForUrl(it) }
            titlesCallback.onSuccessfulPageTitle(titles)
        }
    }
}
