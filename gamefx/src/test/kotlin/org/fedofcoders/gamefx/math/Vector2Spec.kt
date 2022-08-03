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

package org.fedofcoders.gamefx.math

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object Vector2Spec : Spek({
    describe("a 2D vector") {
        on("addition") {
            val sum = Vector2.up() + Vector2.right()

            it("should return the result of adding the first vector to the second vector") {
                assert(sum == Vector2.one())
            }
        }
    }
})
