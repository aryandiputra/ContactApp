package id.ryandzhunter.contact.ui.addcontact

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.android.AndroidInjection
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.databinding.ActivityAddContactBinding
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.Contact
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Created by ryandzhunter on 2/10/18.
 */
class AddContactActivity : AppCompatActivity(), AddContactView {

    private val REQUEST_CODE_GALLERY: Int = 133
    private val REQUEST_CODE_CAPTURE_IMAGE: Int = 134

    @Inject
    lateinit var viewModel : AddContactViewModel

    lateinit var binding: ActivityAddContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact)
        var contact = intent.getParcelableExtra<Contact>(INTENT_CONTACT)
        binding.addContactVM = viewModel
        contact?.let { binding.addContactVM?.initContact(contact) }
        binding.addContactVM?.initView(this)
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
                binding.addContactVM?.onChecklistClicked()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CAPTURE_IMAGE -> if (resultCode == RESULT_OK) {
                val photo = data?.extras?.get("data") as Bitmap
                var imageUri = getImageUri(this, photo)
                binding.addContactVM?.setPhotoBitmap(photo)
                val stream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
                GlideApp.with(this)
                        .load(stream.toByteArray())
                        .error(R.drawable.ic_profile_large)
                        .fitCenter()
                        .into(imageAvatar)
            }
            REQUEST_CODE_GALLERY -> if (resultCode == RESULT_OK && data != null
                    && data.getData() != null) {
                val imageUri = data.getData()
                val photo = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                binding.addContactVM?.setPhotoBitmap(photo)
                val photoFilePaths = getRealPathFromURI(this, imageUri)
                GlideApp.with(this)
                        .load(photoFilePaths)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(imageAvatar)
            }
        }
    }

    fun getImageUri(context: Context, image: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, image, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            if (cursor == null) {
                return contentUri.path
            } else {
                val column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(column_index)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return null
    }

}