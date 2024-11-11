package app.test.demochat

import android.app.Application
import app.test.demochat.di.appModule
import app.test.demochat.di.networkModule
import app.test.demochat.di.repositoryModule
import app.test.demochat.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(listOf(appModule, networkModule, repositoryModule, viewModelModule))
        }
    }
}