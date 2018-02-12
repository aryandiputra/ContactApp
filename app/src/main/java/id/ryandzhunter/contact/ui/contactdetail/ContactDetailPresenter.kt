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

    lateinit var contact: Contact

    fun getContactDetail(id: Long) {

        view?.showProgress()
        disposable.add(api.getContact(id)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                        { contact ->
                            this.contact = contact
                            view?.hideProgress()
                            view?.onResponse(contact)
                        },
                        { _ ->
                            view?.hideProgress()
                        })
        )
    }

    fun updateFavorite(id: Long?, contact: Contact) {
        // tp update favorite contact
        disposable.add(api.updateContact(id, contact)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe({ disposable -> view?.showProgress() })
                .doOnTerminate({ view?.hideProgress() })
                .subscribe({ contactResult -> view?.updateFavoriteIcon(contactResult.favorite) },
                        { throwable -> }))
    }

    fun onFavoriteButtonClicked(){
        contact.favorite = !contact.favorite
        updateFavorite(contact.id, contact)
    }

    fun onEditButtonClicked(){
        view?.openContactDetailActivity(contact)
    }

    fun onCallButtonClicked(){
        view?.openPhoneCall(contact.phoneNumber)
    }

    fun onPhoneNumberClicked(){
        view?.copyPhoneNumber(contact.phoneNumber)
    }

    fun onMessageButtonClicked(){
        view?.sendMessage(contact.phoneNumber)
    }

    fun onEmailButtonClicked(){
        view?.sendEmail(contact.email)
    }

    fun onEmailClicked(){
        view?.copyEmail(contact.email)
    }

    fun onShareButtonClicked(){
        view?.openShareMenuDialog(contact)
    }
}