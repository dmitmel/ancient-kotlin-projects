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

import com.google.gson.*
import org.fedofcoders.ktpubsub.util.gson

enum class ResponseType {
    EVENT, ERROR
}

interface Response {
    val type: ResponseType

    fun toJSON(): JsonElement
}

data class EventResponse(val eventName: String, val data: Any) : Response {
    override val type: ResponseType get() = ResponseType.EVENT

    override fun toJSON(): JsonElement {
        val root = JsonObject()
        root.add("type", JsonPrimitive(type.ordinal))
        root.add("evt", JsonPrimitive(eventName))
        root.add("dataType", JsonPrimitive(data.javaClass.name))
        root.add("data", gson.toJsonTree(data))
        return root
    }
}

data class ErrorResponse(val exception: ServerException) : Response {
    override val type: ResponseType get() = ResponseType.ERROR

    override fun toJSON(): JsonElement {
        val root = JsonObject()
        root.add("type", JsonPrimitive(type.ordinal))
        root.add("err", exception.toJSON())
        return root
    }
}
