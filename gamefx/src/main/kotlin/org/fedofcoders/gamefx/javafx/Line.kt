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
import org.fedofcoders.gamefx.math.AnalyticGeometry
import org.fedofcoders.gamefx.math.Vector2

class Line(configure: (Line) -> Unit = { line -> }) : Shape() {
    var start: Vector2 = Vector2.zero()
    var end: Vector2 = Vector2.zero()

    override val points: List<Vector2> get() = listOf(start, end)
    override val edges: List<Edge> get() = listOf(Edge(start, end))

    init {
        configure(this)
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            if (strokeEnabled) {
                context.stroke = stroke
                context.lineWidth = strokeWidth.toDouble()
                context.strokeLine(start.x.toDouble(), start.y.toDouble(), end.x.toDouble(), end.y.toDouble())
            }
        }
    }

    fun relativePointSign(point: Vector2): PointSign {
        val sign: Float = AnalyticGeometry.lineSign(start, end, point)
        if (sign > 0) return PointSign.ON_LEFT
        else if (sign < 0) return PointSign.ON_RIGHT
        else return PointSign.ON_LINE
    }

    fun isPointInLineBounds(point: Vector2): Boolean =
            point.x >= Math.min(start.x, end.x) && point.x <= Math.max(start.x, end.x) &&
            point.y >= Math.min(start.y, end.y) && point.y <= Math.max(start.y, end.y)

    override fun contains(point: Vector2): Boolean =
        relativePointSign(point) == PointSign.ON_LINE && isPointInLineBounds(point)
}

enum class PointSign {
    ON_LEFT, ON_LINE, ON_RIGHT
}
