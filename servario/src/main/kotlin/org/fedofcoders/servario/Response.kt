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

class Response {
    var status: Int = 200
    var statusDescription: String
        get() = StatusDescriptionMaps.STATUS_TO_DESCRIPTION_MAP[status] ?:
            throw NoSuchElementException("no description for status code: $status")
        set(value) { status = StatusDescriptionMaps.DESCRIPTION_TO_STATUS_MAP[statusDescription] ?:
            throw NoSuchElementException("no status code for description: $statusDescription") }
}
