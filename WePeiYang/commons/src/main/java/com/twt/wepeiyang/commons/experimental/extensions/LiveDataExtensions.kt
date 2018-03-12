package com.twt.wepeiyang.commons.experimental.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import kotlinx.coroutines.experimental.Deferred

/**
 * Created by rickygao on 2017/11/14.
 */
fun <T, U> LiveData<T>.map(func: (T) -> U): LiveData<U> = Transformations.map(this, func)

fun <T, U> LiveData<T>.switchMap(func: (T) -> LiveData<U>): LiveData<U> = Transformations.switchMap(this, func)

fun <T> LiveData<T>.bind(lifecycleOwner: LifecycleOwner, block: (T?) -> Unit) = observe(lifecycleOwner, Observer(block))

fun <T> LiveData<ConsumableMessage<T>>.consume(lifecycleOwner: LifecycleOwner, from: Int = ConsumableMessage.ANY, block: (T?) -> Unit) =
        observe(lifecycleOwner, Observer {
            if (it?.consumed == false && (ConsumableMessage.ANY == from || it.from == from)) {
                it.consumed = true
                block(it.message)
            }
        })

data class ConsumableMessage<out T>(val message: T, val from: Int = ANY, var consumed: Boolean = false) {
    companion object {
        const val ANY = -1
    }
}

suspend fun <T> Deferred<T>.awaitOrHandle(handler: (Exception) -> Unit) {
    this.invokeOnCompletion { }
}