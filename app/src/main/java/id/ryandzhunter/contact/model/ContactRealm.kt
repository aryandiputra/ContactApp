package id.ryandzhunter.contact.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by ryandzhunter on 1/8/18.
 */

@RealmClass
open class ContactRealm(
        @PrimaryKey open var id: Long? = 0,
        open var firstName: String? = "",
        open var lastName: String? = "",
        open var email: String? = "",
        open var phoneNumber: String? = "",
        open var profilePic: String? = "",
        open var favorite: Boolean? = false,
        open var createAt: String? = "",
        open var updateAt: String? = "") : RealmModel