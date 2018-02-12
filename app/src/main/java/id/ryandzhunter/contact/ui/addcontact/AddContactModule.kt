package id.ryandzhunter.contact.ui.addcontact

import dagger.Module
import dagger.Provides
import id.ryandzhunter.contact.di.ActivityScope

/**
 * Created by ryandzhunter on 2/10/18.
 */

@Module
class AddContactModule {

    @Provides
    @ActivityScope
    internal fun provideAddContactActivity(addContactActivity: AddContactActivity): AddContactView {
        return addContactActivity
    }
}