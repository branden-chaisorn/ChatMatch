package brandenc.com.chatmatch

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun provideJsonCreator(): JsonCreator {
        return JsonCreator(TextMatcher(PageTitleRetriever(JsoupTitleRetriever())))
    }

}
