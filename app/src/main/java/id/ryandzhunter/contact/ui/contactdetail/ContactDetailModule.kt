package id.ryandzhunter.contact.ui.contactdetail

import dagger.Module
import dagger.Provides
import id.ryandzhunter.contact.di.ActivityScope

/**
 * Created by ryandzhunter on 2/1/18.
 */

@Module
class ContactDetailModule {

    @Provides
    @ActivityScope
    internal fun provideContactDetailActivity(contactDetailActivity: ContactDetailActivity): ContactDetailView {
        return contactDetailActivity
    }

}