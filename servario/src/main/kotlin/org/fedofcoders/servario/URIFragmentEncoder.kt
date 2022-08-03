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

object URIFragmentEncoder {
    private val ALLOWED_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" + "!$%&()*+,-./:;=?@\\[]^_\\{|}~"
    private fun Char.isAllowed(): Boolean = this in ALLOWED_CHARS

    private fun isASCII(c: Char): Boolean {
        return c.toInt() < 128
    }

    fun encode(input: String): String {
        return encodeWithNotAllowed(input, "")
    }

    fun encodeWithNotAllowed(input: String, notAllowed: String): String = buildString {
        input.forEach { char ->
            if (!char.isAllowed() || char in notAllowed || !isASCII(char)) {
                append('%')
                append(Integer.toHexString(char.toInt()))
            } else {
                append(char)
            }
        }
    }
}
