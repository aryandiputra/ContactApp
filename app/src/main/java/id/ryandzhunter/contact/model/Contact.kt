package id.ryandzhunter.contact.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ryandzhunter on 1/3/17.
 */
data class Contact(
        @SerializedName("id") val id: Int,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("email") val email: String,
        @SerializedName("phone_number") val phoneNumber: String,
        @SerializedName("profile_pic") val profilePic: String,
        @SerializedName("favorite") val favorite: Boolean,
        @SerializedName("created_at") val createAt: String,
        @SerializedName("updated_at") val updateAt: String
)
