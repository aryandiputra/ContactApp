package id.ryandzhunter.contact.ui.contactdetail

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.base.BaseActivity
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.Contact
import id.ryandzhunter.contact.ui.addcontact.AddContactActivity
import kotlinx.android.synthetic.main.activity_contact_detail.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject

/**
 * Created by ryandzhunter on 2/1/18.
 */
class ContactDetailActivity : BaseActivity(), ContactDetailView {

    @Inject
    lateinit var presenter: ContactDetailPresenter

    lateinit var myClip: ClipData
    lateinit var myClipboard: ClipboardManager

    private var isFavorite: Boolean = false
    private val REQ_STORAGE_PERMISSION: Int = 313

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
        presenter.getContactDetail(id)
    }

    override fun onResponse(contact: Contact) {
        isFavorite = contact.favorite;
        GlideApp.with(this)
                .load(contact.profilePic)
                .placeholder(R.drawable.ic_betty_allen)
                .fitCenter()
                .into(imageAvatar);
        textName.text = contact.firstName + " " + contact.lastName
        textMobile.text = contact.phoneNumber
        textEmail.text = contact.email
        updateFavoriteIcon(contact.favorite)
        setOnClick(contact)
    }

    private fun setOnClick( contact: Contact) {
        buttonFavorite.setOnClickListener {
            presenter.onFavoriteButtonClicked()
        }
        buttonEdit.setOnClickListener {
            presenter.onEditButtonClicked()
        }
        buttonCall.setOnClickListener {
            presenter.onCallButtonClicked()
        }
        textMobile.setOnClickListener { presenter.onPhoneNumberClicked() }
        buttonMessage.setOnClickListener { presenter.onMessageButtonClicked() }
        buttonEmail.setOnClickListener { presenter.onEmailButtonClicked() }
        textEmail.setOnClickListener { presenter.onEmailClicked() }
        buttonShare.setOnClickListener { presenter.onShareButtonClicked() }
    }

    override fun openContactDetailActivity(contact: Contact) {
        val intent = AddContactActivity.newIntent(this, contact)
        startActivity(intent)
    }

    override fun showProgress() {
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.cancel()
    }

    override fun updateFavoriteIcon(favorite:Boolean){
        isFavorite = favorite;
        if (favorite) {
            buttonFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favourite_filled))
        } else {
            buttonFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favourite))
        }
    }

    override fun openPhoneCall(phoneNumber: String?) {
        val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    override fun copyPhoneNumber(phoneNumber: String?) {
        myClip = ClipData.newPlainText("text", phoneNumber)
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.setPrimaryClip(myClip)
        toast("${phoneNumber}")
    }

    override fun copyEmail(email: String?) {
        myClip = ClipData.newPlainText("text", email)
        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.setPrimaryClip(myClip)
        toast("${email}")
    }

    override fun sendMessage(phoneNumber: String?) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    override fun sendEmail(email: String?) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:" + email)
        startActivity(Intent.createChooser(emailIntent, "Send email"))
    }

    override fun openShareMenuDialog(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.share_contact)
                .setItems(R.array.array_share_contact, { dialog, which ->
                    when (which) {
                        0 -> shareAsText(contact)
                        1 -> shareAsContact(contact)
                    }
                }).create().show()
    }

    private fun shareAsText(contact: Contact) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + contact.firstName + " " + contact.lastName + " \n" +
                "Phone Number: " + contact.phoneNumber + " \n" +
                "Email: " + contact.email + "\n")
        shareIntent.type = "text/plain"
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(shareIntent)
    }

    private fun shareAsContact(contact: Contact) {

        if (ContextCompat.checkSelfPermission(this@ContactDetailActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQ_STORAGE_PERMISSION
            )
            return
        }

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(generateVCF(contact)))
        shareIntent.type = "text/x-vcard"
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(shareIntent)
    }

    private fun generateVCF(contact: Contact): File {

        val filename = Environment.getExternalStorageDirectory().toString() + "/generated.vcf"
        Log.e("DetailActivity", filename)

        val vcfFile = File(filename)
        try {
            val fw = FileWriter(vcfFile)
            fw.run {
                write("BEGIN:VCARD\r\n")
                write("VERSION:3.0\r\n")
                write("N:" + contact.firstName + ";" + contact.lastName + "\r\n")
                write("FN:" + contact.firstName + " " + contact.lastName + "\r\n")
                write("TEL;TYPE=HOME,VOICE:" + contact.phoneNumber + "\r\n")
                write("EMAIL;TYPE=PREF,INTERNET:" + contact.email + "\r\n")
                write("END:VCARD\r\n")
                close()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return vcfFile
    }

}