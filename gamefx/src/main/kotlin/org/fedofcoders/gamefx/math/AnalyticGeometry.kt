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

object AnalyticGeometry {
    @JvmStatic fun lineSign(start: Vector2, end: Vector2, point: Vector2): Float =
        (start.x - point.x) * (end.y - point.y) - (end.x - point.x) * (start.y - point.y)

    @JvmStatic fun isPointInTriangle(a: Vector2, b: Vector2, c: Vector2, point: Vector2): Boolean {
        val onLeftSideOfAB = lineSign(point, a, b) > 0f
        val onLeftSideOfBC = lineSign(point, b, c) > 0f
        val onLeftSideOfCA = lineSign(point, c, a) > 0f

        return onLeftSideOfAB && onLeftSideOfBC && onLeftSideOfCA
    }
}
