package id.ryandzhunter.contact.ui.contactlist

import dagger.Module
import dagger.Provides
import id.ryandzhunter.contact.di.ActivityScope

/**
 * Created by ryandzhunter on 1/3/17.
 */
@Module
class ContactListModule {

    @Provides
    @ActivityScope
    internal fun provideContactListActivity(contactListActivity: ContactListActivity): ContactListView {
        return contactListActivity
    }

}