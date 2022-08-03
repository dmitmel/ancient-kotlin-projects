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

@file:JvmName("JSON")

package org.fedofcoders.servario.util

import com.google.gson.*

var gson = Gson()
private var jsonParser = JsonParser()

fun parseJSON(json: String): JsonElement {
    return jsonParser.parse(json)
}

@Suppress("UNCHECKED_CAST")
fun parseJSONToMap(json: String): Map<String, Any?> {
    return gson.fromJson(json, Map::class.java) as Map<String, Any?>
}
