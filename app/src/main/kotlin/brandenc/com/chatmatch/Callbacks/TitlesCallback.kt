package brandenc.com.chatmatch.Callbacks

/**
 * Callback used by the calling user to get the result from successfully retrieving the titles from
 * the provided urls
 **/
interface TitlesCallback {
    /**
     * Callback called when titles were successfully retrieved
     *
     * @param titles list of titles gathered from provided links
     */
    fun onSuccessfulPageTitle(titles: List<String>)
}
