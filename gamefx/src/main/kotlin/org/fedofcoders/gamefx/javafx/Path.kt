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
import org.fedofcoders.gamefx.math.Vector2

class Path(configure: (Path) -> Unit = { path -> }) : Shape() {
    override var points: List<Vector2> = arrayListOf()
    override val edges: List<Edge>
        get() = (0 until points.size - 1).map { i ->
            val point: Vector2 = points[i]
            val nextPoint: Vector2 = points[i + 1]
            Edge(point, nextPoint)
        }

    init {
        configure(this)
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            if (strokeEnabled) {
                context.stroke = stroke
                context.lineWidth = strokeWidth.toDouble()
                for ((start, end) in edges)
                    context.strokeLine(start.x.toDouble(), start.y.toDouble(), end.x.toDouble(), end.y.toDouble())
            }
        }
    }

    override fun contains(point: Vector2): Boolean = false
}
