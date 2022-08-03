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

package org.fedofcoders.ktpubsub.server

import org.fedofcoders.ktpubsub.util.EventEmitter
import org.fedofcoders.ktpubsub.util.EventListener

data class Namespace(val name: String, val clients: MutableList<Client> = arrayListOf()) : EventEmitter() {
    var onConnection: EventListener<Client>? = null
        get() = field
        set(value) {
            field = value
            if (value == null) removeAll("connection")
            else on("connection", value)
        }
    var onMessage: EventListener<Any>? = null
        get() = field
        set(value) {
            field = value
            if (value == null) removeAll("message")
            else on("message", value)
        }

}
