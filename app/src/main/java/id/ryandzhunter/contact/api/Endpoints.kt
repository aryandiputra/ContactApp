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
    fun getAllContacts(): Observable<List<Contact>>

    @GET("/contacts/{id}.json")
    fun getContact(@Path("id") id: Long): Observable<Contact>

    @POST("/contacts.json")
    fun addNewContact(@Body contact: Contact): Observable<Contact>

    @PUT("/contacts/{id}.json")
    fun updateContact(@Path("id") id: Long?, @Body contact: Contact)
            : Observable<Contact>

    @DELETE("/contacts/{id}.json")
    fun deleteContact(@Path("id") id: Int): Completable

    @Multipart
    @POST("/contacts.json")
    fun addContactWithImage(@Part image: MultipartBody.Part,
                            @Part("contact[first_name]") firstName: RequestBody,
                            @Part("contact[last_name]") lastName: RequestBody,
                            @Part("contact[email]") email: RequestBody,
                            @Part("contact[phone_number]") phoneNumber: RequestBody)
            : Observable<Contact>
}