package id.ryandzhunter.contact.ui.addcontact

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.Contact
import kotlinx.android.synthetic.main.activity_add_contact.*

/**
 * Created by ryandzhunter on 2/10/18.
 */
class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        var contact = intent.getParcelableExtra<Contact>(INTENT_CONTACT)
        initView(contact)
    }

    private fun initView(contact: Contact?) {
        GlideApp.with(this)
                .load(contact?.profilePic)
                .placeholder(R.drawable.ic_profile_large)
                .error(R.drawable.ic_profile_large)
                .centerCrop()
                .into(imageAvatar);
        editFirstName.setText(contact?.firstName)
        editLastName.setText(contact?.lastName)
        editPhone.setText(contact?.phoneNumber)
        editEmail.setText(contact?.email)
    }

    companion object {

        private val INTENT_CONTACT = "contact"

        fun newIntent(context: Context, contact: Contact): Intent {
            val intent = Intent(context, AddContactActivity::class.java)
            intent.putExtra(INTENT_CONTACT, contact)
            return intent
        }
    }
}