package id.ryandzhunter.contact.ui.addcontact

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.databinding.ActivityAddContactBinding
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.Contact
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by ryandzhunter on 2/10/18.
 */
class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityAddContactBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact)
        var contact = intent.getParcelableExtra<Contact>(INTENT_CONTACT)
        binding.addContactVM = AddContactViewModel(contact)
        setToolbar(contact)
        initView(contact)
    }

    private fun setToolbar(contact: Contact?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (contact == null){
            actionBar?.title = getString(R.string.toolbar_add_title)
        } else {
            actionBar?.title = getString(R.string.toolbar_edit_title)
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat
                .getColor(this, R.color.addContactPrimary)))
    }

    private fun initView(contact: Contact?) {
        GlideApp.with(this)
                .load(contact?.profilePic)
                .placeholder(R.drawable.ic_profile_large)
                .error(R.drawable.ic_profile_large)
                .centerCrop()
                .into(imageAvatar);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.toolbar_check -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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