package brandenc.com.chatmatch

/**
 * An interface to retrieve titles for urls
 */
interface TitleRetriever {

    /**
     * Retrieves the page title for the given url
     *
     * @param url the given url a title is requested for
     */
    fun getTitleForUrl(url: String): String
}
