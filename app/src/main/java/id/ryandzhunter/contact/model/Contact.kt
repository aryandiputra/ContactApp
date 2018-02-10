package id.ryandzhunter.contact.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by ryandzhunter on 1/3/17.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Contact(
        @SerializedName("id") val id: Long,
        @SerializedName("first_name") var firstName: String? = "",
        @SerializedName("last_name") var lastName: String? = "",
        @SerializedName("email") var email: String? = "",
        @SerializedName("phone_number") var phoneNumber: String? = "",
        @SerializedName("profile_pic") var profilePic: String? = "",
        @SerializedName("favorite") var favorite: Boolean,
        @SerializedName("created_at") val createAt: String,
        @SerializedName("updated_at") val updateAt: String
) : Parcelable
