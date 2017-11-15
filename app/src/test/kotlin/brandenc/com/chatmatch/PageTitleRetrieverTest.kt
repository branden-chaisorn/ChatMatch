package brandenc.com.chatmatch

import brandenc.com.chatmatch.Callbacks.TitlesCallback
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import org.junit.Assert.*

class PageTitleRetrieverTest {

    lateinit internal var pageTitleRetriever : PageTitleRetriever
    lateinit internal var latch: CountDownLatch
    lateinit internal var jsoupHelper: TitleRetriever

    @Before
    fun setUp() {
        latch = CountDownLatch(1)
        jsoupHelper = mock<TitleRetriever>()
        pageTitleRetriever = PageTitleRetriever(jsoupHelper)
    }

    @Test
    fun getPageTitlesTest() {
        val urls = listOf("https://www.nbcolympics.com")

        Mockito.`when`(jsoupHelper.getTitleForUrl(any())).thenReturn("olympics")

        pageTitleRetriever.getPageTitles(urls, object: TitlesCallback {
            override fun onSuccessfulPageTitle(titles: List<String>) {
                assertEquals("olympics", titles[0])
                latch.countDown()
            }

        })
        latch.await()
    }

    @Test
    fun getPageTitlesTestMultipleUrls() {
        val urls = listOf("https://www.nbcolympics.com", "https://www.google.com")

        Mockito.`when`(jsoupHelper.getTitleForUrl(any())).thenReturn("olympics", "potato")

        pageTitleRetriever.getPageTitles(urls, object: TitlesCallback {
            override fun onSuccessfulPageTitle(titles: List<String>) {
                assertEquals("olympics", titles[0])
                assertEquals("potato", titles[1])
                latch.countDown()
            }

        })
        latch.await()
    }

    @Test
    fun getPageTitlesTestNoUrls() {
        val urls = listOf<String>()

        pageTitleRetriever.getPageTitles(urls, object: TitlesCallback {
            override fun onSuccessfulPageTitle(titles: List<String>) {
                assert(titles.isEmpty())
                latch.countDown()
            }

        })
        latch.await()
    }

}
