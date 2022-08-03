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

enum class RequestType {
    CONNECT, DISCONNECT, EVENT
}

interface Request {
    val namespace: String
    val type: RequestType

    companion object Deserializer {
        fun fromJSON(json: JsonElement): Request {
            val jsonObj: JsonObject = json.asJsonObject
            val namespace: String = jsonObj["nsp"]?.asString ?: "/"
            val type: RequestType = jsonObj["type"]?.let { RequestType.values()[it.asInt] } ?:
                throw JsonSyntaxException("'type' is required: $jsonObj")

            when (type) {
                RequestType.CONNECT -> return ConnectRequest(namespace)
                RequestType.DISCONNECT -> return DisconnectRequest(namespace)
                RequestType.EVENT -> {
                    val eventName: String = jsonObj["evt"]?.asString ?: "message"
                    val dataType: Class<*> = jsonObj["dataType"]?.let { Class.forName(it.asString) } ?:
                        throw JsonSyntaxException("'dataType' is required: $jsonObj")
                    val data: Any = jsonObj["data"]?.let { gson.fromJson(it, dataType) } ?:
                        throw JsonSyntaxException("'data' is required: $jsonObj")
                    return EventRequest(namespace, eventName, data)
                }
            }
        }
    }
}

data class ConnectRequest(override val namespace: String) : Request {
    override val type: RequestType get() = RequestType.CONNECT
}

data class DisconnectRequest(override val namespace: String) : Request {
    override val type: RequestType get() = RequestType.DISCONNECT
}

data class EventRequest(override val namespace: String, val eventName: String, val data: Any) : Request {
    override val type: RequestType get() = RequestType.EVENT
}
