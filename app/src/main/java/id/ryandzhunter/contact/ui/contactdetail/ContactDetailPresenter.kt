package id.ryandzhunter.contact.ui.contactdetail

import id.ryandzhunter.contact.api.Endpoints
import id.ryandzhunter.contact.base.BasePresenter
import id.ryandzhunter.contact.model.Contact
import id.ryandzhunter.contact.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ryandzhunter on 2/1/18.
 */
class ContactDetailPresenter @Inject constructor(var api: Endpoints, disposable: CompositeDisposable,
                                                 scheduler: SchedulerProvider)
    : BasePresenter<ContactDetailView>(disposable, scheduler) {

    fun getContactDetail(id: Long) {

        view?.showProgress()
        disposable.add(api.getContact(id)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                        { contact ->
                            view?.hideProgress()
                            view?.onResponse(contact)
                        },
                        { _ ->
                            view?.hideProgress()
                        })
        )
    }

    fun updateFavorite(id: Long, contact: Contact) {
        // tp update favorite contact
        disposable.add(api.updateContact(id, contact)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe({ disposable -> view?.showProgress() })
                .doOnTerminate({ view?.hideProgress() })
                .subscribe({ contactResult -> view?.updateFavoriteIcon(contactResult.favorite) },
                        { throwable -> }))
    }
}