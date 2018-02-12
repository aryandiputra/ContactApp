package id.ryandzhunter.contact.ui.addcontact

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.databinding.ActivityAddContactBinding
import id.ryandzhunter.contact.model.Contact
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by ryandzhunter on 2/10/18.
 */
class AddContactActivity : AppCompatActivity(), AddContactView {

    private val REQUEST_CODE_GALLERY: Int = 133
    private val REQUEST_CODE_CAPTURE_IMAGE: Int = 134

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityAddContactBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact)
        var contact = intent.getParcelableExtra<Contact>(INTENT_CONTACT)
        binding.addContactVM = AddContactViewModel(contact, this)
        setToolbar(contact)
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

    override fun openChoosePhotoDialog() {
        val builder = AlertDialog.Builder(this@AddContactActivity)
        builder.setTitle(getString(R.string.choose_image))
                .setItems(R.array.array_choose_image, { dialogInterface, i ->
                    when (i) {
                        0 ->
                            //Checking Permssion
                            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    captureImage()
                                } else {
                                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                                }
                            } else {
                                captureImage()
                            }
                        1 -> if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                selectImage()
                            } else {
                                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                            }
                        } else {
                            selectImage()
                        }
                    }
                }).create().show()
    }

    fun closeActivity() {
        finish()
    }

    private fun selectImage() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
    }

    private fun captureImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE)
    }
}