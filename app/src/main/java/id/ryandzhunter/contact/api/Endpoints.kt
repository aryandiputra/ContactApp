package id.ryandzhunter.contact.api

import id.ryandzhunter.contact.model.Contact
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by ryandzhunter on 1/3/17.
 */
interface Endpoints {

    @GET("/contacts.json")
    abstract fun getAllContacts(): Observable<List<Contact>>

    @GET("/contacts/{id}.json")
    abstract fun getContact(@Path("id") id: Long): Observable<Contact>

    @POST("/contacts.json")
    abstract fun addNewContact(@Body contact: Contact): Observable<Contact>

    @PUT("/contacts/{id}.json")
    abstract fun updateContact(@Path("id") id: Long?, @Body contact: Contact)
            : Observable<Contact>

    @DELETE("/contacts/{id}.json")
    abstract fun deleteContact(@Path("id") id: Int): Completable

    @Multipart
    @POST("/contacts.json")
    abstract fun addContactWithImage(@Part image: MultipartBody.Part,
                                     @Part("contact[first_name]") firstName: RequestBody,
                                     @Part("contact[last_name]") lastName: RequestBody,
                                     @Part("contact[email]") email: RequestBody,
                                     @Part("contact[phone_number]") phoneNumber: RequestBody)
            : Observable<Contact>
}