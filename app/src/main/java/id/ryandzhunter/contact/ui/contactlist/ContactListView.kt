package id.ryandzhunter.contact.ui.contactlist

import id.ryandzhunter.contact.base.BaseView
import id.ryandzhunter.contact.model.ContactRealm

/**
 * Created by ryandzhunter on 1/3/17.
 */
interface ContactListView : BaseView {
    fun onResponse(list: List<ContactRealm>?)
    fun showProgress()
    fun hideProgress()
    fun showNoResult()
    fun hideNoResult()
}
