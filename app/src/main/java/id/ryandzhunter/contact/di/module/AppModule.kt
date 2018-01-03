package id.ryandzhunter.contact.di.module

import android.app.Application
import android.content.Context
import id.ryandzhunter.contact.helper.SpHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.google.gson.Gson;
import com.google.gson.GsonBuilder
import id.ryandzhunter.contact.util.AppSchedulerProvider
import id.ryandzhunter.contact.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ryandzhunter on 12/25/17.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesGson() = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

    @Provides
    @Singleton
    fun providesResources(application: Application) = application.resources

    @Provides
    @Singleton
    fun providesSharedPref(application: Application, gson: Gson) = SpHelper(application.getSharedPreferences("Sp", Context.MODE_PRIVATE), gson)

    @Provides
    @Singleton
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun provideSchedulerProvider() : SchedulerProvider = AppSchedulerProvider()

}