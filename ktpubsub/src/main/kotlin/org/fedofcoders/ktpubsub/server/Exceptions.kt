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

enum class ServerExceptionType {
    SIMPLE, NAMESPACE_NOT_FOUND
}

open class ServerException(message: String?, cause: Throwable?) : RuntimeException(message, cause) {
    open val type: ServerExceptionType get() = ServerExceptionType.SIMPLE

    constructor(message: String?) : this(message, null)
    constructor(cause: Throwable?) : this(null, cause)
    constructor() : this(null, null)

    open fun toJSON(): JsonElement {
        val root = JsonObject()
        root.add("type", JsonPrimitive(type.ordinal))
        root.add("msg", JsonPrimitive(message))
        return root
    }
}

open class NamespaceNotFoundException(val namespace: String, val availableNamespaces: List<String>, cause: Throwable?) :
        ServerException("namespace '$namespace' not found", cause) {
    override val type: ServerExceptionType get() = ServerExceptionType.NAMESPACE_NOT_FOUND

    constructor(namespace: String, availableNamespaces: List<String>) : this(namespace, availableNamespaces, null)

    override fun toJSON(): JsonElement {
        val root = super.toJSON().asJsonObject
        root.add("nsp", JsonPrimitive(namespace))

        val namespacesJson = JsonArray()
        availableNamespaces.forEach(namespacesJson::add)
        root.add("nsps", namespacesJson)

        return root
    }
}

