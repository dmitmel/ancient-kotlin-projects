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

class Ellipse(configure: (Ellipse) -> Unit = { ellipse -> }) : Shape() {
    var radius: Float
        get() =
            if (scale.x != scale.y)
                throw RuntimeException("can't get radius of ellipse width different width and height")
            else scale.x
        set(value) { scale = Vector2(value, value)
        }

    override val points: List<Vector2> get() = throw UnsupportedOperationException("get points of ellipse")
    override val edges: List<Edge> get() = throw UnsupportedOperationException("get edges of ellipse")

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
                context.fillOval(position.x.toDouble(), position.y.toDouble(), scale.x.toDouble(), scale.y.toDouble())
            }
            if (strokeEnabled) {
                context.stroke = stroke
                context.lineWidth = strokeWidth.toDouble()
                context.strokeOval(position.x.toDouble(), position.y.toDouble(), scale.x.toDouble(), scale.y.toDouble())
            }
            if (rotation != 0f) {
                context.restore()
            }
        }
    }

    override fun contains(point: Vector2): Boolean = false
}
