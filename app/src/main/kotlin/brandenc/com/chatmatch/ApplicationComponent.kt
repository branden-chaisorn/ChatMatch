package brandenc.com.chatmatch

import brandenc.com.chatmatch.Activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun jsonCreator(): JsonCreator

    fun inject(mainActivity: MainActivity)
}
