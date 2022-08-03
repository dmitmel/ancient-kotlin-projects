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
 */

package org.fedofcoders.ktpubsub.util

typealias EventListener<T> = (T) -> Unit
typealias NoArgsEventListener = () -> Unit

open class EventEmitter {
    val events: MutableMap<String, MutableList<EventListener<*>>> = hashMapOf()
    val eventNames: MutableSet<String> get() = events.keys

    @Suppress("UNCHECKED_CAST")
    fun <T> listeners(eventName: String): MutableList<EventListener<T>> =
        events[eventName] as MutableList<EventListener<T>>

    operator fun contains(eventName: String): Boolean = eventName in events

    fun <T> on(eventName: String, listener: EventListener<T>): EventEmitter {
        if (eventName in events) events[eventName]?.add(listener)
        else events[eventName] = arrayListOf(listener as EventListener<*>)
        return this
    }

    fun <T> on(eventName: String, listener: NoArgsEventListener): EventEmitter =
        on<T>(eventName) { _ -> listener.invoke() }

    @Suppress("UNCHECKED_CAST")
    open fun <T> emit(eventName: String, arg: T): EventEmitter {
        val listeners = events[eventName]
        listeners?.forEach { listener -> (listener as EventListener<T>).invoke(arg) }
        return this
    }

    fun emit(eventName: String): EventEmitter = emit(eventName, Unit)

    fun removeAll(eventName: String): EventEmitter {
        val listeners = events[eventName]
        if (listeners != null) {
            events.remove(eventName)
        }
        return this
    }

    fun <T> remove(eventName: String, listener: EventListener<T>): EventEmitter {
        val listeners = events[eventName]
        if (listeners != null) {
            listeners.remove(listener)
            if (listeners.isEmpty()) events.remove(eventName)
        }
        return this
    }

    override fun equals(other: Any?): Boolean =
        this === other || (other is EventEmitter && this.events == other.events)

    override fun hashCode(): Int = events.hashCode()

    override fun toString(): String = "EventEmitter(events=$events)"
}
