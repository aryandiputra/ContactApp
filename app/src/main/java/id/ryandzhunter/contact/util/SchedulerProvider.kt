package id.ryandzhunter.contact.util

import io.reactivex.Scheduler

/**
 * Created by ryandzhunter on 1/3/17.
 */
interface SchedulerProvider {
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun io(): Scheduler
}