package id.ryandzhunter.contact.ui.contactdetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.base.BaseActivity
import id.ryandzhunter.contact.model.Contact
import kotlinx.android.synthetic.main.activity_contact_detail.*
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * Created by ryandzhunter on 2/1/18.
 */
class ContactDetailActivity : BaseActivity(), ContactDetailView {

    @Inject
    lateinit var presenter: ContactDetailPresenter

    lateinit var myClip: ClipData
    lateinit var myClipboard: ClipboardManager

    companion object {

        private val INTENT_USER_ID = "id"

        fun newIntent(context: Context, id: Long?): Intent {
            val intent = Intent(context, ContactDetailActivity::class.java)
            intent.putExtra(INTENT_USER_ID, id)
            return intent
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_contact_detail
    }

    override fun onActivityInject() {
        presenter.attachView(this)
        val id = intent.getLongExtra(INTENT_USER_ID, -1)
        toast("${id} Clicked")
        presenter.getContactDetail(id)
    }

    override fun onResponse(contact: Contact) {
        textName.text = contact.firstName + " " + contact.lastName
        textMobile.text = contact.phoneNumber
        textEmail.text = contact.email
        buttonCall.setOnClickListener { view -> onButtonCallClick(contact.phoneNumber) }
        textMobile.setOnClickListener { view -> onPhoneNumberClick(contact.phoneNumber) }
        buttonMessage.setOnClickListener { view -> onButtonMessageClick(contact.phoneNumber) }
        buttonEmail.setOnClickListener { view -> onButtonEmailClick(contact.email) }
        textEmail.setOnClickListener { view -> onEmailClick(contact.email) }
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    fun onButtonCallClick(phoneNumber: String?) {
        val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    fun onPhoneNumberClick(phoneNumber: String?) {
        myClip = ClipData.newPlainText("text", phoneNumber)
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.setPrimaryClip(myClip)
        toast("${phoneNumber}")
    }

    fun onEmailClick(email: String?) {
        myClip = ClipData.newPlainText("text", email)
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.setPrimaryClip(myClip)
        toast("${email}")
    }

    fun onButtonMessageClick(phoneNumber: String?) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    fun onButtonEmailClick(email: String?) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:" + email)
        startActivity(Intent.createChooser(emailIntent, "Send email"))
    }

}