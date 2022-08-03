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

package org.fedofcoders.servario

import java.util.*

data class Request(val method: String, val uri: URI, val httpVersion: String,
                   val headers: Map<String, String>, val body: ByteArray) {
    override fun equals(other: Any?): Boolean =
        this === other || (other is Request &&
            method == other.method && uri == other.uri && httpVersion == other.httpVersion &&
            headers == other.headers && Arrays.equals(body, other.body))

    override fun hashCode(): Int = Objects.hash(method, uri, httpVersion, headers, Arrays.hashCode(body))
}
