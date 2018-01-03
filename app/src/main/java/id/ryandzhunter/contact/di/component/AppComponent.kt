package id.ryandzhunter.contact.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import id.ryandzhunter.contact.App
import id.ryandzhunter.contact.di.module.*
import javax.inject.Singleton

/**
 * Created by ryandzhunter on 1/3/17.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class, ActivityInjector::class,
        AppModule::class, RetrofitModule::class, ApiModule::class, OkHttpModule::class))
interface AppComponent{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}