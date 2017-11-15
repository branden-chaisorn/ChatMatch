package brandenc.com.chatmatch

import android.app.Application

class MainApplication : Application() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule())
                .build()
    }
}
