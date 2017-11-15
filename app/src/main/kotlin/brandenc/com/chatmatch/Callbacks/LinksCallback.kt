package brandenc.com.chatmatch.Callbacks

/**
 * Callback used by the calling user to get the result from successfully gathering link information
 **/
interface LinksCallback {
    /**
     * Callback called when links were successfully created
     *
     * @param links list of HashMap objects that contain the urls and their titles that were found
     * in the original input text
     */
    fun onSuccessfulLinksGathering(links: List<HashMap<String,String>>)
}
