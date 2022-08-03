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

import java.util.HashMap

/**
 * State-machine parser for URIs in HTTP requests.
 *
 * ## URI Syntax
 * ```
 * /over/there#nose?name=ferret
 * \_________/ \__/ \_________/
 *     |        |        |
 *    path    anchor   params
 * ```
 *
 * ## Notes
 *
 * #### Path
 * * `path` is required
 *
 * #### Anchor
 * * `anchor` is optional
 * * If `anchor` is empty, but `#` is present - anchor will be set to an empty string
 *
 * #### Params
 * * `params` are optional
 * * You can use both `&` and `;` to separate params
 * * If there're no `params`, but `?` is present - params will be an empty map
 * * If param name and `=` are present, but param value isn't - value will be [URI.EMPTY_PARAM_VALUE].
 * * If param name is present, but param value and `=` aren't - value will be [URI.EMPTY_PARAM_VALUE].
 */
@Suppress("NON_EXHAUSTIVE_WHEN")
object URIParser {
    private enum class State {
        PATH,
        ANCHOR_START,
        ANCHOR,
        PARAMS_START,
        PARAM_NAME,
        PARAM_VALUE_START,
        PARAM_VALUE,
        PARAMS_SEPARATOR
    }

    fun parse(charSequence: CharSequence): URI {
        var state = State.PATH

        val path = StringBuilder()
        val anchor = StringBuilder()

        var paramName = StringBuilder()
        var paramValue = StringBuilder()
        val params = HashMap<String, String>()


        for (char in charSequence) {
            if (char == '#' && state == State.PATH)
                state = State.ANCHOR_START
            else if (char == '?' && (state == State.PATH || state == State.ANCHOR || state == State.ANCHOR_START))
                state = State.PARAMS_START
            else if (state == State.ANCHOR_START)
                state = State.ANCHOR
            else if (state == State.PARAMS_START)
                state = State.PARAM_NAME
            else if (char == '=' && state == State.PARAM_NAME)
                state = State.PARAM_VALUE_START
            else if ((char == '&' || char == ';') && (state == State.PARAM_VALUE || state == State.PARAM_NAME || state == State.PARAM_VALUE_START))
                state = State.PARAMS_SEPARATOR
            else if (state == State.PARAM_VALUE_START)
                state = State.PARAM_VALUE
            else if (state == State.PARAMS_SEPARATOR)
                state = State.PARAM_NAME

            when (state) {
                URIParser.State.PATH -> path.append(char)
                URIParser.State.ANCHOR_START -> {}        // Ignore
                URIParser.State.ANCHOR -> anchor.append(char)
                URIParser.State.PARAMS_START -> {}        // Ignore
                URIParser.State.PARAM_NAME -> paramName.append(char)
                URIParser.State.PARAM_VALUE_START -> {}   // Ignore
                URIParser.State.PARAM_VALUE -> paramValue.append(char)
                URIParser.State.PARAMS_SEPARATOR -> {
                    val decodedParamName = URI.decodeFragment(paramName.toString())
                    val decodedParamValue = URI.decodeFragment(paramValue.toString())
                    params.put(decodedParamName, decodedParamValue)

                    paramName = StringBuilder()
                    paramValue = StringBuilder()
                }
            }
        }

        // Finishing not finished operations, if they were working before EOF
        when (state) {
            URIParser.State.PARAM_NAME, URIParser.State.PARAM_VALUE_START -> {
                val decodedParamName = URI.decodeFragment(paramName.toString())
                params.put(decodedParamName, URI.EMPTY_PARAM_VALUE)
            }

            URIParser.State.PARAM_VALUE -> {
                val decodedParamName = URI.decodeFragment(paramName.toString())
                val decodedParamValue = URI.decodeFragment(paramValue.toString())
                params.put(decodedParamName, decodedParamValue)
            }
        }

        val decodedPath = URI.decodeFragment(path.toString())
        val decodedAnchor = URI.decodeFragment(anchor.toString())
        return URI(decodedPath, decodedAnchor, params)
    }

    /**
     * Parses sequence of values, that are grouped in pairs, separated
     * by '=', and pairs are separated by '&', into map. In other words,
     * parses URI params, without first '?'.
     */
    fun parseURILikeParams(charSequence: CharSequence): Map<String, String> {
        var state = State.PARAM_NAME

        var paramName = StringBuilder()
        var paramValue = StringBuilder()
        val params = HashMap<String, String>()

        for (char in charSequence) {
            if (char == '=' && state == State.PARAM_NAME)
                state = State.PARAM_VALUE_START
            else if ((char == '&' || char == ';') && (state == State.PARAM_VALUE || state == State.PARAM_NAME || state == State.PARAM_VALUE_START))
                state = State.PARAMS_SEPARATOR
            else if (state == State.PARAM_VALUE_START)
                state = State.PARAM_VALUE
            else if (state == State.PARAMS_SEPARATOR)
                state = State.PARAM_NAME

            when (state) {
                URIParser.State.PARAM_NAME -> paramName.append(char)
                URIParser.State.PARAM_VALUE_START -> {}     // Ignore
                URIParser.State.PARAM_VALUE -> paramValue.append(char)
                URIParser.State.PARAMS_SEPARATOR -> {
                    val decodedParamName = URI.decodeFragment(paramName.toString())
                    val decodedParamValue = URI.decodeFragment(paramValue.toString())
                    params.put(decodedParamName, decodedParamValue)

                    paramName = StringBuilder()
                    paramValue = StringBuilder()
                }
            }
        }

        // Finishing not finished operations, if they were working before end of string
        when (state) {
            URIParser.State.PARAM_NAME, URIParser.State.PARAM_VALUE_START -> {
                val decodedParamName = URI.decodeFragment(paramName.toString())
                params.put(decodedParamName, URI.EMPTY_PARAM_VALUE)
            }

            URIParser.State.PARAM_VALUE -> {
                val decodedParamName = URI.decodeFragment(paramName.toString())
                val decodedParamValue = URI.decodeFragment(paramValue.toString())
                params.put(decodedParamName, decodedParamValue)
            }
        }

        return params
    }
}
