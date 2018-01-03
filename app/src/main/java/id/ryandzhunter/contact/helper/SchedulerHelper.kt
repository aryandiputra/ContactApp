package id.ryandzhunter.contact.helper

import io.reactivex.*

/**
 * Created by ryandzhunter on 1/3/17.
 */
class SchedulerHelper(val backgroundScheduler: Scheduler, val foregroundScheduler: Scheduler) {

    fun <T> getSchedulersForObservable(): (Observable<T>) -> Observable<T> {
        return { observable: Observable<T> ->
            observable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun <T> getSchedulersForSingle(): (Single<T>) -> Single<T> {
        return { single: Single<T> ->
            single.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun getSchedulersForCompletable(): (Completable) -> Completable {
        return { completable: Completable ->
            completable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun <T> getSchedulersForFlowable(): (Flowable<T>) -> Flowable<T> {
        return { flowable: Flowable<T> ->
            flowable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }
}