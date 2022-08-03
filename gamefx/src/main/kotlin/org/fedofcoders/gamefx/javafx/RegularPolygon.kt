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
import org.fedofcoders.gamefx.math.FloatMath
import org.fedofcoders.gamefx.math.Vector2
import java.util.*

class RegularPolygon(configure: (RegularPolygon) -> Unit) : Shape() {
    var edgesCount: Int = 3
        set(value) {
            if (value < 3) throw IllegalArgumentException("edges count must be > 2")
            field = value
            points = createPoints()
        }

    var radius: Float = 0f
        set(value) {
            if (value < 0) throw IllegalArgumentException("radius must be >= 0")
            field = value
            points = createPoints()
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
            diameter = value.x
        }

    override var points: List<Vector2> = createPoints()
        private set(value) { field = value }
    override var edges: List<Edge> = ArrayList(edgesCount)
        private set(value) { field = value }

    private var xPoints: DoubleArray = DoubleArray(edgesCount)
    private var yPoints: DoubleArray = DoubleArray(edgesCount)

    init {
        configure(this)
    }

    private fun createPoints(): List<Vector2> {
        xPoints = DoubleArray(edgesCount)
        yPoints = DoubleArray(edgesCount)

        val step: Float = FloatMath.TWO_PI / edgesCount
        val points: List<Vector2> = (0 until edgesCount).map { i ->
            val x = position.x + radius + FloatMath.cos(rotation + i * step) * radius
            xPoints[i] = x.toDouble()
            val y = position.y + radius + FloatMath.sin(rotation + i * step) * radius
            yPoints[i] = y.toDouble()
            Vector2(x, y)
        }

        this.edges = (0..points.size - 1).map { i ->
            val point: Vector2 = points[i]
            val nextPoint: Vector2 = points[if (i + 1 == points.size) 0 else i + 1]
            Edge(point, nextPoint)
        }

        return points
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            if (fillEnabled) {
                context.fill = fill
                context.fillPolygon(xPoints, yPoints, edgesCount)
            }
            if (strokeEnabled) {
                context.stroke = stroke
                context.lineWidth = strokeWidth.toDouble()
                context.strokePolygon(xPoints, yPoints, edgesCount)
            }
        }
    }

    override operator fun contains(point: Vector2): Boolean {
        for ((start: Vector2, end: Vector2) in edges) {
            val sign: Float = (start.x - point.x) * (end.y - point.y) -
                (end.x - point.x) * (start.y - point.y)
            if (sign < 0)
                return false
        }
        return true
    }
}
