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
import org.fedofcoders.gamefx.math.AnalyticGeometry
import org.fedofcoders.gamefx.math.Vector2

class Rect(configure: (Rect) -> Unit = { rect -> }) : Shape() {
    var side: Float
        get() =
            if (scale.x != scale.y)
                throw RuntimeException("can't get side of rect width different width and height")
            else scale.x
        set(value) { scale = Vector2(value, value)
        }
    var cornerRadius: Float = 0f
        set(value) {
            if (cornerRadius < 0) throw IllegalArgumentException("corner radius must be >= 0")
            field = value
        }

    val topLeft: Vector2 get() = position
    val topRight: Vector2 get() = position + Vector2(scale.x, 0f)
    val bottomRight: Vector2 get() = position + scale
    val bottomLeft: Vector2 get() = position + Vector2(0f, scale.y)

    override val points: List<Vector2> get() = listOf(topLeft, topRight, bottomRight, bottomLeft)
    override val edges: List<Edge> get() = listOf(
        Edge(topLeft, topRight),
        Edge(topRight, bottomRight),
        Edge(bottomRight, bottomLeft),
        Edge(bottomLeft, topLeft)
    )

    init {
        configure(this)
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            if (rotation != 0f) {
                context.save()
                val rotate = Rotate(rotation.toDouble(), center.x.toDouble(), center.y.toDouble())
                context.setTransform(rotate.mxx, rotate.myx, rotate.mxy, rotate.myy, rotate.tx, rotate.ty)
            }
            if (cornerRadius > 0.0) {
                if (fillEnabled) {
                    context.fill = fill
                    context.fillRoundRect(position.x.toDouble(), position.y.toDouble(), scale.x.toDouble(), scale.y.toDouble(),
                        cornerRadius.toDouble(), cornerRadius.toDouble())
                }
                if (strokeEnabled) {
                    context.stroke = stroke
                    context.lineWidth = strokeWidth.toDouble()
                    context.strokeRoundRect(position.x.toDouble(), position.y.toDouble(), scale.x.toDouble(), scale.y.toDouble(),
                        cornerRadius.toDouble(), cornerRadius.toDouble())
                }
            } else {
                if (fillEnabled) {
                    context.fill = fill
                    context.fillRect(position.x.toDouble(), position.y.toDouble(), scale.x.toDouble(), scale.y.toDouble())
                }
                if (strokeEnabled) {
                    context.stroke = stroke
                    context.lineWidth = strokeWidth.toDouble()
                    context.strokeRect(position.x.toDouble(), position.y.toDouble(), scale.x.toDouble(), scale.y.toDouble())
                }
            }
            if (rotation != 0f) {
                context.restore()
            }
        }
    }

    override fun contains(point: Vector2): Boolean =
        AnalyticGeometry.isPointInTriangle(topLeft, topRight, bottomRight, point) ||
            AnalyticGeometry.isPointInTriangle(bottomRight, bottomLeft, topLeft, point)
}
