package id.ryandzhunter.contact.ui.contactlist

import id.ryandzhunter.contact.api.Endpoints
import id.ryandzhunter.contact.base.BasePresenter
import id.ryandzhunter.contact.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ryandzhunter on 1/3/17.
 */
class ContactListPresenter @Inject constructor(var api: Endpoints, disposable: CompositeDisposable, scheduler: SchedulerProvider) : BasePresenter<ContactListView>(disposable, scheduler) {

    fun getAllContact() {
        view?.showProgress()
        disposable.add(api.getAllContacts()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                        { result ->
                            view?.hideProgress()
                            view?.onSearchResponse(result)
                            if (result == null || result.isEmpty()) {
                                view?.noResult()
                            }
                        },
                        { _ ->
                            view?.hideProgress()
                            view?.onError()
                        })
        )
    }
}