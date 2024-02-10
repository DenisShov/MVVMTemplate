package com.dshovhenia.mvvm.template.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dshovhenia.mvvm.template.core.extension.ConsumableEvent
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame

class TestObserver<T> : Observer<T> {
    private val observedValues = mutableListOf<T>()

    fun clearLiveDataEvents() {
        observedValues.clear()
    }

    fun printValues() {
        println("Total count = ${observedValues.size}")
        observedValues.forEach {
            println("item = $it")
        }
    }

    fun <Event> shouldContainEvents(vararg events: Event) {
        val wrapped = events.map { ConsumableEvent(it) }
        observedValues.shouldContainSame(wrapped)
    }

    fun <T> shouldContainValues(vararg values: T) {
        observedValues.shouldContainSame(values.asList())
    }

    fun shouldBeEmpty() {
        observedValues.size.shouldBeEqualTo(0)
    }

    override fun onChanged(value: T) {
        observedValues.add(value)
    }
}

fun <T> LiveData<T>.testObserver(clear: Boolean = false) =
    TestObserver<T>().also {
        observeForever(it)
        if (clear) it.clearLiveDataEvents()
    }
