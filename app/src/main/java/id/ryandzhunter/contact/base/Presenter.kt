package id.ryandzhunter.contact.base

/**
 * Created by ryandzhunter on 1/3/17.
 */
interface Presenter<V : BaseView> {

    fun attachView(view: V)

    fun detachView()
}