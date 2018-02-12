package id.ryandzhunter.contact.ui.addcontact

import android.databinding.BaseObservable
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.Contact

/**
 * Created by ryandzhunter on 2/10/18.
 */
class AddContactViewModel constructor (var contact: Contact?) : BaseObservable() {

    var isValidFirstName = ObservableField<Boolean>()
    var isValidLastName = ObservableField<Boolean>()
    var isValidPhoneNumber = ObservableField<Boolean>()
    var isValidEmail = ObservableField<Boolean>()

    init {
        contact?.let { setValidityFlag(it) }
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
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches() && phone.length >= 10
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getFirstName(): String? {
        return contact?.firstName
    }

    fun setFirstName(firstName: String) {
        contact?.firstName = firstName
        isValidFirstName.set(isValidName(firstName))
    }

    fun getLastName(): String? {
        return contact?.lastName
    }

    fun setLastName(lastName: String) {
        contact?.lastName = lastName
        isValidLastName.set(isValidName(lastName))
    }

    fun getEmail(): String? {
        return contact?.email
    }

    fun setEmail(email: String) {
        contact?.email = email
        isValidEmail.set(isValidEmail(email))
    }

    fun getPhoneNumber(): String? {
        return contact?.phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String) {
        contact?.phoneNumber = phoneNumber
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

    fun getProfilePic() : String ? {
        return contact?.profilePic
    }

    fun setProfilePic(profilePic : String) {
        contact?.profilePic = profilePic
    }
}