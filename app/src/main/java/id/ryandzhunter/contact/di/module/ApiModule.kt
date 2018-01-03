package id.ryandzhunter.contact.di.module

import dagger.Module
import dagger.Provides
import id.ryandzhunter.contact.api.Endpoints
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by ryandzhunter on 12/25/17.
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun providesEndpoints(retrofit: Retrofit): Endpoints = retrofit.create(Endpoints::class.java)
}