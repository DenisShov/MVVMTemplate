package com.dshovhenia.mvvm.template.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * This should be used for listening single events in [LiveData].
 */
data class ConsumableEvent<out T>(val content: T) {
    private var consumed = false

    fun consume(consumer: (T) -> Unit) {
        if (not(consumed)) {
            consumer(content)
        }
        consumed = true
    }

    fun not(condition: Boolean) = !condition
}

fun <T : Any?, L : LiveData<ConsumableEvent<T>>> LifecycleOwner.observeEvents(
    liveData: L,
    body: (T) -> Unit
) =
    liveData.observe(this, EventObserver { body(it) })

fun <T : Any?, L : LiveData<T>> LifecycleOwner.observe(
    liveData: L,
    body: (T) -> Unit
) =
    liveData.observe(this, Observer(body))

class EventObserver<T>(private val onEventUnconsumedContent: (T) -> Unit) :
    Observer<ConsumableEvent<T>> {
    override fun onChanged(event: ConsumableEvent<T>) {
        event.consume(onEventUnconsumedContent)
    }
}

class ConsumableLiveData<T> : LiveData<ConsumableEvent<T>> {

    constructor() : super()

    constructor(value: T) : super(ConsumableEvent(value))

    fun setConsumableValue(value: T) {
        super.setValue(ConsumableEvent(value))
    }
}
