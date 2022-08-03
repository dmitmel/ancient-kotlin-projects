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

import java.io.InputStream

object RequestReader {
    fun read(inputStream: InputStream): Request {
        val inputBytes = readInputBytes(inputStream)

        return Request("GET", URI("/"), "HTTP/1.1", emptyMap(), ByteArray(0))
    }

    private fun readInputBytes(inputStream: InputStream): ByteArray {
        // Creating buffer with size of 64 KB
        val buffer = ByteArray(1024 * 64)
        val requestLength = inputStream.read(buffer)

        if (requestLength < 1) {
            // Request is empty
            return ByteArray(0)
        } else {
            // If input data is less than 64 KB, buffer will be filled with many trailing zero bytes,
            // so buffer must be trimmed
            val trimmedBuffer = ByteArray(requestLength)
            System.arraycopy(buffer, 0, trimmedBuffer, 0, requestLength)
            return trimmedBuffer
        }
    }
}
