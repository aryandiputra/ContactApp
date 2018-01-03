package id.ryandzhunter.contact.ui.contactlist

import id.ryandzhunter.contact.base.BaseView
import id.ryandzhunter.contact.model.Contact

/**
 * Created by ryandzhunter on 1/3/17.
 */
interface ContactListView : BaseView {
    fun onSearchResponse(list: List<Contact>?)
    fun showProgress()
    fun hideProgress()
    fun noResult()
}
