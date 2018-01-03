package id.ryandzhunter.contact.base

/**
 * Created by ryandzhunter on 1/3/17.
 */
interface BaseView {
    fun onError()
    fun setPresenter(presenter: BasePresenter<*>)
}