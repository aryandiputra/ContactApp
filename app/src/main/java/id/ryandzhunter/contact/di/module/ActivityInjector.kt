package id.ryandzhunter.contact.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.ryandzhunter.contact.ui.contactlist.ContactListActivity
import id.ryandzhunter.contact.ui.contactlist.ContactListModule
import id.ryandzhunter.contact.di.ActivityScope
import id.ryandzhunter.contact.ui.addcontact.AddContactActivity
import id.ryandzhunter.contact.ui.addcontact.AddContactModule
import id.ryandzhunter.contact.ui.contactdetail.ContactDetailActivity
import id.ryandzhunter.contact.ui.contactdetail.ContactDetailModule

/**
 * Created by ryandzhunter on 12/25/17.
 */
@Module
abstract class ActivityInjector {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(ContactListModule::class))
    abstract fun bindContactListInjector(): ContactListActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(ContactDetailModule::class))
    abstract fun bindContactDetailInjector(): ContactDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(AddContactModule::class))
    abstract fun bindAddContactInjector(): AddContactActivity

}