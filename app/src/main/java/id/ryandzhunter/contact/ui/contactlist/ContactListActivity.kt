package id.ryandzhunter.contact.ui.contactlist

import android.content.Intent
import android.view.View
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.base.BaseActivity
import id.ryandzhunter.contact.model.ContactRealm
import id.ryandzhunter.contact.ui.addcontact.AddContactActivity
import id.ryandzhunter.contact.ui.contactdetail.ContactDetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
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

        buttonAdd.setOnClickListener { startActivity(Intent(
                this, AddContactActivity::class.java)) }
    }

    override fun onResponse(list: List<ContactRealm>?) {
        rvContact.adapter = list?.let { ContactListAdapter (it) {
//                toast("${it.id} Clicked")
            val intent = ContactDetailActivity.newIntent(this, it.id)
            startActivity(intent)
        }};
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun showNoResult() {
        textNoResult.visibility = View.VISIBLE
    }

    override fun hideNoResult() {
        textNoResult.visibility = View.INVISIBLE
    }
}
