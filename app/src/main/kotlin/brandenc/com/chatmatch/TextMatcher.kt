package brandenc.com.chatmatch

import brandenc.com.chatmatch.Callbacks.LinksCallback
import brandenc.com.chatmatch.Callbacks.TitlesCallback
import java.util.regex.Pattern

/**
 * A parser used to parse emoji's, mentions, and urls
 *
 * @param pageTitleRetriever the class used to grab the page titles from the provided links.
 * @constructor Creates a TextMatcher
 */
class TextMatcher(var pageTitleRetriever: PageTitleRetriever) {

    private val mentionRegex = "^*@[a-zA-Z]+"
    private val emojiRegex = "^*:[a-zA-Z0-9]{0,15}+:"
    private val urlRegex = "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?(?:(([a-zA-Z0-9 -\uD7FF豈-\uFDCFﷰ-\uFFEF]([a-zA-Z0-9 -\uD7FF豈-\uFDCFﷰ-\uFFEF\\-]{0,61}[a-zA-Z0-9 -\uD7FF豈-\uFDCFﷰ-\uFFEF]){0,1}\\.)+[a-zA-Z -\uD7FF豈-\uFDCFﷰ-\uFFEF]{2,63}|((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9 -\uD7FF豈-\uFDCFﷰ-\uFFEF\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|\$)"

    /**
     * A way to mention a user. Always starts with an '@' and ends when hitting a non-word
     * character.
     *
     * Parses a string of input for all contained mentions of the form '@whatever'
     *
     * @param input the type of a member in this group.
     * @return the list of users mentioned
     */
    fun parseMentions(input: String): List<String> {
        val mentionPattern = Pattern.compile(mentionRegex)
        val mentionMatcher = mentionPattern.matcher(input)

        val matchList = mutableListOf<String>()

        while (mentionMatcher.find()) {
            matchList.add(mentionMatcher.group().replace("@",""))
        }

        return matchList
    }

    /**
     * only need to consider 'custom' emoji which are alphanumeric strings, no longer than 15
     *
     * Parses a string of input for all contained emojis of the form ':input:'.
     *
     * @param input the type of a member in this group.
     * @return the list of emoji's found
     */
    fun parseEmojis(input: String): List<String> {
        val mentionPattern = Pattern.compile(emojiRegex)
        val mentionMatcher = mentionPattern.matcher(input)

        val matchList = mutableListOf<String>()

        while (mentionMatcher.find()) {
            matchList.add(mentionMatcher.group().replace(":",""))
        }

        return matchList
    }

    /**
     * Links - Any URLs contained in the message, along with the page's title.
     *
     * Parses a string of input for all contained urls
     *
     * @param input the type of a member in this group.
     * @param callback the callback where the resulting found links and titles will be returned to
     */
    fun parseUrls(input: String, callback: LinksCallback) {
        val urlPattern = Pattern.compile(urlRegex)
        val urlMatcher = urlPattern.matcher(input)
        val linkList = mutableListOf<HashMap<String,String>>()

        val urls = mutableListOf<String>()

        while (urlMatcher.find()) {
            urls.add(urlMatcher.group())
        }

        pageTitleRetriever.getPageTitles(urls, object: TitlesCallback {
            override fun onSuccessfulPageTitle(titles: List<String>) {
                for (i in titles.indices) {
                    val link = hashMapOf<String,String>()
                    link.put("url", urls[i])
                    link.put("title", titles[i])
                    linkList.add(link)
                }
                callback.onSuccessfulLinksGathering(linkList)
            }
        })
    }


}
