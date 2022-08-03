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

import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Rotate
import org.fedofcoders.gamefx.math.Vector2

class Circle(configure: (Circle) -> Unit = { circle -> }) : Shape() {
    var radius: Float = 0f
        set(value) {
            if (value < 0) throw IllegalArgumentException("radius must be >= 0")
            field = value
        }
    var diameter: Float
        get() = radius * 2
        set(value) {
            if (value < 0) throw IllegalArgumentException("radius must be >= 0")
            radius = value / 2
        }
    override var scale: Vector2
        get() = Vector2(diameter, diameter)
        set(value) {
            if (value.x != value.y)
                throw RuntimeException("can't set scale of circle with different width and height")
            diameter = scale.x
        }

    override val points: List<Vector2> get() = throw UnsupportedOperationException("get points of circle")
    override val edges: List<Edge> get() = throw UnsupportedOperationException("get edges of circle")

    init {
        configure(this)
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            if (rotation != 0f) {
                context.save()
                val rotate = Rotate(rotation.toDouble(), center.x.toDouble(), center.y.toDouble())
                context.setTransform(rotate.mxx, rotate.myx, rotate.mxy, rotate.myy, rotate.tx, rotate.ty);
            }
            if (fillEnabled) {
                context.fill = fill
                context.fillOval(position.x.toDouble(), position.y.toDouble(), diameter.toDouble(), diameter.toDouble())
            }
            if (strokeEnabled) {
                context.stroke = stroke
                context.lineWidth = strokeWidth.toDouble()
                context.strokeOval(position.x.toDouble(), position.y.toDouble(), diameter.toDouble(), diameter.toDouble())
            }
            if (rotation != 0f) {
                context.restore()
            }
        }
    }

    override fun contains(point: Vector2): Boolean =
        pow2(point.x - center.x) + pow2(point.y - center.y) <= pow2(radius)

    private fun pow2(f: Float): Float = f * f
}
