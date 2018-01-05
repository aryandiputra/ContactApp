package id.ryandzhunter.contact.ui.contactlist

import android.view.View
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.base.BaseActivity
import id.ryandzhunter.contact.model.Contact
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Created by ryandzhunter on 1/3/17.
 */
class ContactListActivity : BaseActivity(), ContactListView {

    @Inject
    lateinit var presenter: ContactListPresenter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onActivityInject() {
        presenter.attachView(this)
        presenter.getAllContact()

        with(rvContact) {
            layoutManager = android.support.v7.widget.LinearLayoutManager(this@ContactListActivity)
        }
    }

    override fun onSearchResponse(list: List<Contact>?) {
        rvContact.adapter = list?.let { ContactListAdapter (it) };
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun noResult() {
    }
}
