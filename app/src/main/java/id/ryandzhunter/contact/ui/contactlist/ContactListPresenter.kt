package id.ryandzhunter.contact.ui.contactlist

import com.vicpin.krealmextensions.*
import id.ryandzhunter.contact.api.Endpoints
import id.ryandzhunter.contact.base.BasePresenter
import id.ryandzhunter.contact.model.Contact
import id.ryandzhunter.contact.model.ContactRealm
import id.ryandzhunter.contact.util.ContactSortComparator
import id.ryandzhunter.contact.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

/**
 * Created by ryandzhunter on 1/3/17.
 */
class ContactListPresenter @Inject constructor(var api: Endpoints, disposable: CompositeDisposable,
                                               scheduler: SchedulerProvider) :
        BasePresenter<ContactListView>(disposable, scheduler) {

    fun getAllContact() {
        view?.showProgress()
        view?.hideNoResult()
        disposable.add(api.getAllContacts()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                        { contactList ->
                            view?.hideProgress()
                            saveToDatabase(contactList)
                            if (contactList == null || contactList.isEmpty()) {
                                view?.showNoResult()
                            }
                            getLocalDatabase()
                        },
                        { _ ->
                            view?.hideProgress()
                            view?.onResponse(ContactRealm().queryAll())
                            view?.onError()
                        })
        )
    }

    private fun saveToDatabase(contactList: List<Contact>) {
        ContactRealm().deleteAll()
        val contactRealms: MutableList<ContactRealm> = mutableListOf<ContactRealm>()
        for (contact in contactList) {
            contactRealms.add(ContactRealm(contact.id, contact.firstName, contact.lastName,
                    contact.email, contact.phoneNumber, contact.profilePic, contact.favorite,
                    contact.createAt, contact.updateAt))
        }
        contactRealms.saveAll()
    }

    private fun getLocalDatabase() {
        ContactRealm().queryAllAsFlowable()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({contacts ->
                    Collections.sort(contacts, ContactSortComparator())
                    view?.onResponse(contacts)
                })
    }

}