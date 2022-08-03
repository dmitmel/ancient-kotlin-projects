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

@file:JvmName("Promises")

package org.fedofcoders.ktpubsub.util

import java.util.*

fun <S, E> resolvedPromise(result: S): Promise<S, E> = Promise { resolve, reject -> resolve(result) }
fun <S, E> rejectedPromise(error: E): Promise<S, E> = Promise { resolve, reject -> reject(error) }

sealed class PromiseResult<S, E> {
    abstract val value: S
    abstract val error: E

    abstract fun <T> map(onResolved: (S) -> T, onRejected: (E) -> T): T
    abstract fun isResolved(): Boolean
    abstract fun isRejected(): Boolean

    data class Resolved<S, E>(override val value: S) : PromiseResult<S, E>() {
        override val error: E get() = throw IllegalStateException("cannot get error of rejected result")

        override fun <T> map(onResolved: (S) -> T, onRejected: (E) -> T): T = onResolved(value)
        override fun isResolved(): Boolean = true
        override fun isRejected(): Boolean = false
    }

    data class Rejected<S, E>(override val error: E) : PromiseResult<S, E>() {
        override val value: S get() = throw IllegalStateException("cannot get value of rejected result")

        override fun <T> map(onResolved: (S) -> T, onRejected: (E) -> T): T = onRejected(error)
        override fun isResolved(): Boolean = false
        override fun isRejected(): Boolean = true
    }
}

class Promise<S, E>(executor: (resolve: (S) -> Unit, reject: (E) -> Unit) -> Unit) {
    var result: PromiseResult<S, E>? = null
        private set

    @get:JvmName("resolved")
    val resolved: Boolean get() = result != null && result!!.isResolved()
    @get:JvmName("isRejected")
    val rejected: Boolean get() = result != null && result!!.isRejected()

    init {
        executor.invoke({ result ->
            if (this.result != null)
                throw IllegalStateException("Promise is already ${if (resolved) "resolved" else "rejected"}")
            this.result = PromiseResult.Resolved(result)
        }, { error ->
            if (this.result != null)
                throw IllegalStateException("Promise is already ${if (resolved) "resolved" else "rejected"}")
            this.result = PromiseResult.Rejected(error)
        })

        if (result == null)
            throw IllegalStateException("Promise must be resolved OR rejected")
    }

    fun <N> then(callback: (S) -> N): Promise<N, E> = when (result) {
        is PromiseResult.Resolved -> resolvedPromise(callback.invoke(result!!.value))
        is PromiseResult.Rejected -> rejectedPromise(result!!.error)
        null -> throw IllegalStateException("Promise is pending")
    }

    fun <N> thenPromise(callback: (S) -> Promise<N, E>): Promise<N, E> = when (result) {
        is PromiseResult.Resolved -> callback.invoke(result!!.value)
        is PromiseResult.Rejected -> rejectedPromise(result!!.error)
        null -> throw IllegalStateException("Promise is pending")
    }

    fun onError(callback: (E) -> S): Promise<S, E> = when (result) {
        is PromiseResult.Resolved -> resolvedPromise(result!!.value)
        is PromiseResult.Rejected -> resolvedPromise(callback.invoke(result!!.error))
        null -> throw IllegalStateException("Promise is pending")
    }

    fun promiseOnError(callback: (E) -> Promise<S, E>): Promise<S, E> = when (result) {
        is PromiseResult.Resolved -> resolvedPromise(result!!.value)
        is PromiseResult.Rejected -> callback.invoke(result!!.error)
        null -> throw IllegalStateException("Promise is pending")
    }

    fun <T> map(onResolved: (S) -> T, onRejected: (E) -> T): T = when (result) {
        is PromiseResult.Resolved -> onResolved(result!!.value)
        is PromiseResult.Rejected -> onRejected((result!!.error))
        null -> throw IllegalStateException("Promise is pending")
    }
}
