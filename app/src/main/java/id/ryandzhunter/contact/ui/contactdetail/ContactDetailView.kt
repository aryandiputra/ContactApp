package id.ryandzhunter.contact.ui.contactdetail

import id.ryandzhunter.contact.base.BaseView
import id.ryandzhunter.contact.model.Contact

/**
 * Created by ryandzhunter on 2/1/18.
 */
interface ContactDetailView : BaseView {
    fun onResponse(contact: Contact)
    fun showProgress()
    fun hideProgress()
    fun updateFavoriteIcon(favorite:Boolean)
}