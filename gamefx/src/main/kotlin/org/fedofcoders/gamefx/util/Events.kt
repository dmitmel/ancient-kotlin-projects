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

package org.fedofcoders.gamefx.util

import java.util.*

class Event0(vararg listeners: () -> Unit) {
    private val listeners: LinkedList<() -> Unit> = LinkedList()

    init {
        for (listener in listeners) this.listeners.add(listener)
    }

    @get:JvmName("listenersCount")
    val listenersCount: Int get() = listeners.size

    operator fun invoke() = listeners.forEach { it() }

    fun addListener(listener: () -> Unit): Boolean = listeners.add(listener)
    fun removeListener(listener: () -> Unit): Boolean = listeners.remove(listener)
    fun removeAllListeners() = listeners.clear()

    operator fun plusAssign(listener:  () -> Unit) { addListener(listener)    }
    operator fun minusAssign(listener: () -> Unit) { removeListener(listener) }
}

class Event1<A>(vararg listeners: (A) -> Unit) {
    private val listeners: LinkedList<(A) -> Unit> = LinkedList()

    init {
        for (listener in listeners) this.listeners.add(listener)
    }

    @get:JvmName("listenersCount")
    val listenersCount: Int get() = listeners.size

    operator fun invoke(a: A) = listeners.forEach { it(a) }

    fun addListener(listener: (A) -> Unit): Boolean = listeners.add(listener)
    fun removeListener(listener: (A) -> Unit): Boolean = listeners.remove(listener)
    fun removeAllListeners() = listeners.clear()

    operator fun plusAssign(listener:  (A) -> Unit) { addListener(listener)    }
    operator fun minusAssign(listener: (A) -> Unit) { removeListener(listener) }
}

class Event2<A, B>(vararg listeners: (A, B) -> Unit) {
    private val listeners: LinkedList<(A, B) -> Unit> = LinkedList()

    init {
        for (listener in listeners) this.listeners.add(listener)
    }

    @get:JvmName("listenersCount")
    val listenersCount: Int get() = listeners.size

    operator fun invoke(a: A, b: B) = listeners.forEach { it(a, b) }

    fun addListener(listener: (A, B) -> Unit): Boolean = listeners.add(listener)
    fun removeListener(listener: (A, B) -> Unit): Boolean = listeners.remove(listener)
    fun removeAllListeners() = listeners.clear()

    operator fun plusAssign(listener:  (A, B) -> Unit) { addListener(listener)    }
    operator fun minusAssign(listener: (A, B) -> Unit) { removeListener(listener) }
}

class Event3<A, B, C>(vararg listeners: (A, B, C) -> Unit) {
    private val listeners: LinkedList<(A, B, C) -> Unit> = LinkedList()

    init {
        for (listener in listeners) this.listeners.add(listener)
    }

    @get:JvmName("listenersCount")
    val listenersCount: Int get() = listeners.size

    operator fun invoke(a: A, b: B, c: C) = listeners.forEach { it(a, b, c) }

    fun addListener(listener: (A, B, C) -> Unit): Boolean = listeners.add(listener)
    fun removeListener(listener: (A, B, C) -> Unit): Boolean = listeners.remove(listener)
    fun removeAllListeners() = listeners.clear()

    operator fun plusAssign(listener:  (A, B, C) -> Unit) { addListener(listener)    }
    operator fun minusAssign(listener: (A, B, C) -> Unit) { removeListener(listener) }
}

class Event4<A, B, C, D>(vararg listeners: (A, B, C, D) -> Unit) {
    private val listeners: LinkedList<(A, B, C, D) -> Unit> = LinkedList()

    init {
        for (listener in listeners) this.listeners.add(listener)
    }

    @get:JvmName("listenersCount")
    val listenersCount: Int get() = listeners.size

    operator fun invoke(a: A, b: B, c: C, d: D) = listeners.forEach { it(a, b, c, d) }

    fun addListener(listener: (A, B, C, D) -> Unit): Boolean = listeners.add(listener)
    fun removeListener(listener: (A, B, C, D) -> Unit): Boolean = listeners.remove(listener)
    fun removeAllListeners() = listeners.clear()

    operator fun plusAssign(listener:  (A, B, C, D) -> Unit) { addListener(listener)    }
    operator fun minusAssign(listener: (A, B, C, D) -> Unit) { removeListener(listener) }
}
