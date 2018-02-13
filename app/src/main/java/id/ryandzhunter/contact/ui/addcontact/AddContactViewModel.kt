package id.ryandzhunter.contact.ui.addcontact

import android.databinding.BaseObservable
import android.databinding.BindingAdapter
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.api.Endpoints
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.Contact
import id.ryandzhunter.contact.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Created by ryandzhunter on 2/10/18.
 */
class AddContactViewModel @Inject constructor(var api: Endpoints, var disposable: CompositeDisposable,
                                              var scheduler: SchedulerProvider) : BaseObservable() {

    var isValidFirstName = ObservableField<Boolean>()
    var isValidLastName = ObservableField<Boolean>()
    var isValidPhoneNumber = ObservableField<Boolean>()
    var isValidEmail = ObservableField<Boolean>()

    internal var isLoading = ObservableBoolean()
    internal var obsError = ObservableField<Throwable>()

    private var contact: Contact = Contact()
    private lateinit var view: AddContactView
    private var photoBitmap: Bitmap? = null

    fun initContact(contact: Contact) {
        this.contact = contact
        contact.let { setValidityFlag(it) }
    }

    fun initView(view: AddContactView) {
        this.view = view
    }

    private fun setValidityFlag(contact: Contact) {
        isValidFirstName.set(contact.firstName?.let { isValidName(it) })
        isValidLastName.set(contact.lastName?.let { isValidName(it) })
        isValidPhoneNumber.set(contact.phoneNumber?.let { isValidPhoneNumber(it) })
        isValidEmail.set(contact.email?.let { isValidEmail(it) })
    }

    fun isValidName(name: String): Boolean {
        return !TextUtils.isEmpty(name) && name.length > 3
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches()
                && phone.length >= 10
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getFirstName(): String? {
        return contact.firstName
    }

    fun setFirstName(firstName: String) {
        contact.firstName = firstName
        isValidFirstName.set(isValidName(firstName))
    }

    fun getLastName(): String? {
        return contact.lastName
    }

    fun setLastName(lastName: String) {
        contact.lastName = lastName
        isValidLastName.set(isValidName(lastName))
    }

    fun getEmail(): String? {
        return contact.email
    }

    fun setEmail(email: String) {
        contact.email = email
        isValidEmail.set(isValidEmail(email))
    }

    fun getPhoneNumber(): String? {
        return contact.phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String) {
        contact.phoneNumber = phoneNumber
        isValidPhoneNumber.set(isValidPhoneNumber(phoneNumber))
    }

    companion object {
        @JvmStatic
        @BindingAdapter(value = arrayOf("bind:imageUrl"), requireAll = false)
        fun loadImage(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null)
                GlideApp.with(imageView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_profile_large)
                        .error(R.drawable.ic_profile_large)
                        .fitCenter()
                        .into(imageView)
        }
    }

    fun getProfilePic() : String? {
        return contact.profilePic
    }

    fun setProfilePic(profilePic : String) {
        contact.profilePic = profilePic
    }

    fun onImageButtonClicked() {
        view.openChoosePhotoDialog()
    }

    fun onChecklistClicked() {
        if (getAllValidation())
            if (contact.id == null) {
                if (photoBitmap == null) {
                    contact.let { addNewContact(it) }
                } else {
                    addNewContactWithPhoto(photoBitmap)
                }
            } else {
                contact.let { updateContact(it) }
            }
    }

    fun addNewContact(contact: Contact) {
        disposable.add(api.addNewContact(contact)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe({ disposable -> isLoading.set(true) })
                .doOnTerminate({ isLoading.set(false) })
                .subscribe({ contacts -> }, { throwable -> obsError.set(throwable) }))
    }

    private fun addNewContactWithPhoto(photo: Bitmap?) {
        val file = createImageFile(photo)
        val imageBody = RequestBody.create(MediaType.parse("image/jpg"), file)
        val imagePart = MultipartBody.Part.createFormData("contact[profile_pic]", file.getName(), imageBody)
        val firstNameBody = RequestBody.create(MediaType.parse("text/plain"), contact.firstName)
        val lastNameBody = RequestBody.create(MediaType.parse("text/plain"), contact.lastName)
        val emailBody = RequestBody.create(MediaType.parse("text/plain"), contact.email)
        val phoneBody = RequestBody.create(MediaType.parse("text/plain"), contact.phoneNumber)

        disposable.add(api.addContactWithImage(imagePart, firstNameBody, lastNameBody, emailBody, phoneBody)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe({ disposable -> isLoading.set(true) })
                .doOnTerminate({ isLoading.set(false) })
                .subscribe({ contacts -> }, { throwable -> obsError.set(throwable) }))
    }

    fun createImageFile(image: Bitmap?): File {
        val filename = Environment.getExternalStorageDirectory().toString() + "/profile.jpg"
        val file = File(filename)
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            image?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }

    fun updateContact(contact: Contact) {
        disposable.add(api.updateContact(contact.id, contact)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .doOnSubscribe({ disposable -> isLoading.set(true) })
                .doOnTerminate({ isLoading.set(false) })
                .subscribe({ contacts -> }, { throwable -> obsError.set(throwable) }))
    }

    fun setPhotoBitmap(photoBitmap: Bitmap) {
        this.photoBitmap = photoBitmap
    }

    private fun getAllValidation(): Boolean {
        return isValidLastName.get() && isValidLastName.get() && isValidEmail.get() && isValidPhoneNumber.get()
    }

}