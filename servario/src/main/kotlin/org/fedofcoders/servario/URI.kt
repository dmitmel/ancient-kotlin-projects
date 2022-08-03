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

import java.net.URLDecoder

/**
 * Class for representing URI in HTTP request.
 *
 * ## Structure
 *
 * These URIs have structure, that differs from simple URI. They
 * have only path, anchor (fragment) and params (query).
 *
 * ```
 * /over/there#nose?name=ferret
 * \_________/ \__/ \_________/
 *     |        |        |
 *    path    anchor   params
 * ```
 *
 * For more information on parsing URIs visit [URIParser].
 */
data class URI
@JvmOverloads constructor(val path: String,
                          val anchor: String = URI.EMPTY_ANCHOR,
                          val params: Map<String, String> = URI.EMPTY_PARAMS) {
    /**
     * Just like primary constructor with all args, but only for
     * `path` and `params`.
     */
    constructor(path: String, params: Map<String, String>) : this(path, EMPTY_ANCHOR, params)

    companion object {
        @JvmField val EMPTY_PARAMS = emptyMap<String, String>()
        @JvmField val EMPTY_ANCHOR = ""
        @JvmField val EMPTY_PARAM_VALUE = ""

        /** ASCII-characters that will be encoded while encoding path. */
        private val NOT_ALLOWED_IN_PATH = " ?#"
        /** ASCII-characters that will be encoded while encoding anchor. */
        private val NOT_ALLOWED_IN_ANCHOR = "?"
        /** ASCII-characters that will be encoded while encoding params. */
        private val NOT_ALLOWED_IN_PARAMETERS = " &;="

        /** @see URIParser.parse */
        @JvmStatic fun parse(charSequence: CharSequence): URI = URIParser.parse(charSequence)

        /** @see URIParser.parseURILikeParams */
        @JvmStatic fun parseURILikeParams(paramsString: CharSequence): Map<String, String> = URIParser.parseURILikeParams(paramsString)

        /** @see URLDecoder.decode */
        @JvmStatic fun decodeFragment(encoded: String): String = URLDecoder.decode(encoded, "UTF-8")
    }
    
    override fun toString(): String = buildString {
        append(URIFragmentEncoder.encodeWithNotAllowed(path, NOT_ALLOWED_IN_PATH))
        
        if (anchor.isNotEmpty()) {
            val encodedAnchor = URIFragmentEncoder.encodeWithNotAllowed(anchor, NOT_ALLOWED_IN_ANCHOR)
            append('#').append(encodedAnchor)
        }

        append(paramsToString())
    }
    
    private fun paramsToString(): String =
            if (params.isEmpty()) "" else
                params.entries.toList().map { (key: String, value: String) ->
                    val encodedKey = URIFragmentEncoder.encodeWithNotAllowed(key, NOT_ALLOWED_IN_PARAMETERS)
                    val encodedValue = URIFragmentEncoder.encodeWithNotAllowed(value, NOT_ALLOWED_IN_PARAMETERS)
                    "$encodedKey=$encodedValue"
                }.joinToString(prefix = "?", separator = "&")

    /**
     * Computes length of string representation of this URI. It's more
     * efficient that calling `uri.toString().length()`
     */
    fun length(): Int =
            path.length +
            // Adding 1 if anchor isn't empty to represent '#'
            (if (anchor.isEmpty()) 0 else 1 + anchor.length) +
            paramsLength()
    
    private fun paramsLength(): Int =
        if (params.isEmpty())
            0
        else
            1 + // Adding 1 to represent '?'
            // Counting length of params without '?' and '&'
            params.toList().fold(0) { lengthWithoutSep: Int, (key: String, value: String) ->
                // Adding 1 to represent '='
                lengthWithoutSep + key.length + 1 + value.length } +
            // Adding length of separators ('&')
            (params.size - 1)

    /** Synonym for function [length] for Kotlin.
     * @see length */
    val length: Int
        get() = length()
}
