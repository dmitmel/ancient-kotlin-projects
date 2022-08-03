/*
 * Copyright (c) 2017 FedOfCoders.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.fedofcoders.gamefx.javafx

import org.fedofcoders.gamefx.EventListener

internal class LambdaEventListener<T>(val listener: (T) -> Unit) : EventListener<T> {
    override fun handle(event: T) {
        listener.invoke(event)
    }
}

internal class NoArgsLambdaEventListener<T>(val listener: () -> Unit) : EventListener<T> {
    override fun handle(event: T) {
        listener.invoke()
    }
}

open class EventEmitter {
    val events: MutableMap<String, MutableList<EventListener<*>>> = hashMapOf()
    val eventNames: MutableSet<String> get() = events.keys

    var onNewListener: EventListener<Pair<String, EventListener<*>>>? = null
    var onRemoveListener: EventListener<Pair<String, EventListener<*>>>? = null

    @Suppress("UNCHECKED_CAST")
    fun <T> listeners(eventName: String): MutableList<EventListener<T>> =
        events[eventName] as MutableList<EventListener<T>>

    fun hasEvent(eventName: String): Boolean = eventName in events

    fun <T> on(eventName: String, listener: (T) -> Unit): EventEmitter =
        on(eventName, LambdaEventListener(listener))

    fun <T> on(eventName: String, listener: EventListener<T>): EventEmitter {
        if (onNewListener != null) onNewListener?.handle(eventName to listener)
        if (eventName in events) events[eventName]?.add(listener)
        else events[eventName] = arrayListOf(listener as EventListener<*>)
        return this
    }

    fun <T> on(eventNames: Array<String>, listener: (T) -> Unit): EventEmitter {
        eventNames.forEach { on(it, LambdaEventListener(listener)) }
        return this
    }

    fun <T> on(eventNames: Array<String>, listener: EventListener<T>): EventEmitter {
        eventNames.forEach { on(it, listener) }
        return this
    }

    fun <T> on(eventNames: Iterable<String>, listener: (T) -> Unit): EventEmitter {
        eventNames.forEach { on(it, LambdaEventListener(listener)) }
        return this
    }

    fun <T> on(eventNames: Iterable<String>, listener: EventListener<T>): EventEmitter {
        eventNames.forEach { on(it, listener) }
        return this
    }

    fun <T> on(eventName: String, listener: () -> Unit): EventEmitter =
        on(eventName, NoArgsLambdaEventListener<T>(listener))

    fun <T> on(eventNames: Array<String>, listener: () -> Unit): EventEmitter =
        on(eventNames, NoArgsLambdaEventListener<T>(listener))

    fun <T> on(eventNames: Iterable<String>, listener: () -> Unit): EventEmitter =
        on(eventNames, NoArgsLambdaEventListener<T>(listener))

    @Suppress("UNCHECKED_CAST")
    open fun <T> fire(eventName: String, arg: T): EventEmitter {
        val listeners = events[eventName]
        listeners?.forEach { listener -> (listener as EventListener<T>).handle(arg) }
        return this
    }

    fun removeAll(eventName: String): EventEmitter {
        val listeners = events[eventName]
        if (listeners != null) {
            events.remove(eventName)
            if (onRemoveListener != null)
                listeners.forEach { listener -> onRemoveListener?.handle(eventName to listener) }
        }
        return this
    }

    fun <T> remove(eventName: String, listener: EventListener<T>): EventEmitter {
        val listeners = events[eventName]
        if (listeners != null) {
            listeners.remove(listener)
            if (listeners.isEmpty()) events.remove(eventName)
            onRemoveListener?.handle(eventName to listener)
        }
        return this
    }

    override fun equals(other: Any?): Boolean =
        this === other || (other is EventEmitter && this.events == other.events)

    override fun hashCode(): Int = events.hashCode()

    override fun toString(): String = "EventEmitter(events=$events)"
}
