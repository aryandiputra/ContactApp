package id.ryandzhunter.contact.base

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import id.ryandzhunter.contact.App
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.di.component.AppComponent
import id.ryandzhunter.contact.event.DefaultEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

/**
 * Created by ryandzhunter on 1/3/17.
 */
abstract class BaseActivity: AppCompatActivity(), BaseView {

    private var presenter: BasePresenter<*>? = null
    private lateinit var mAlertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(getLayoutId())
        onActivityInject()
    }

    protected abstract fun getLayoutId() : Int

    protected abstract fun onActivityInject()

    override fun setPresenter(presenter: BasePresenter<*>) {
        this.presenter = presenter
    }

    override fun onError() {
        mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog.setTitle(getString(R.string.network_error_title))
        mAlertDialog.setMessage(getString(R.string.network_error_message))
        mAlertDialog.setPositiveButton(getString(R.string.ok), { dialog, which -> dialog.cancel() })
        mAlertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        presenter = null
    }

    @Subscribe
    fun defaultSubscribe(event: DefaultEvent){}
}
