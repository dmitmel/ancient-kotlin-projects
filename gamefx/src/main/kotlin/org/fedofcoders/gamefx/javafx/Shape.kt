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

package org.fedofcoders.gamefx.javafx

import javafx.scene.paint.Color
import org.fedofcoders.gamefx.math.Vector2

data class Edge(val start: Vector2, val end: Vector2)

abstract class Shape : Node() {
    open var position: Vector2 = Vector2.zero()
    open var scale: Vector2 = Vector2.zero()
    open var center: Vector2
        get() = Vector2(position.x + scale.x / 2, position.y + scale.y / 2)
        set(value) { position = Vector2(value.x - scale.x / 2, value.y - scale.y / 2)
        }

    var fill: Color = Color.BLACK
    var fillEnabled: Boolean = true
    var stroke: Color = Color.BLACK
    @get:JvmName("isStrokeEnabled")
    var strokeEnabled: Boolean = true
    var strokeWidth: Float = 1f
        set(value) {
            if (strokeWidth < 0) throw IllegalArgumentException("stroke width must be >= 0")
            field = value
        }

    var rotation: Float = 0f

    abstract val points: List<Vector2>
    abstract val edges: List<Edge>

    fun move(movement: Vector2) = position.plus(movement, position)

    fun move(x: Float, y: Float) = move(Vector2(x, y))

    override fun <T : Node> findByID(id: String): T? = if (this.id == id) this as T else null

    abstract operator fun contains(point: Vector2): Boolean
}
