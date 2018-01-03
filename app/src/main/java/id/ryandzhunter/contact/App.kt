package id.ryandzhunter.contact

import android.app.Activity
import android.app.Application
import id.ryandzhunter.contact.di.component.AppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import id.ryandzhunter.contact.di.component.DaggerAppComponent
import javax.inject.Inject

/**
 * Created by ryandzhunter on 1/3/17.
 */
class App: Application(), HasActivityInjector {
    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    @Inject
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        appComponent = createComponent()
        appComponent.inject(this)
        Fresco.initialize(this)
    }

    fun createComponent(): AppComponent {
        val appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
        return appComponent
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return mActivityDispatchingAndroidInjector
    }

}