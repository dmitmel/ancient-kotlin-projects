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

import javafx.application.Application
import javafx.scene.*
import javafx.scene.canvas.Canvas
import javafx.scene.input.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.fedofcoders.gamefx.math.Vector2

class GameFXTest : Application() {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Application.launch(GameFXTest::class.java, *args)
        }
    }

    override fun start(primaryStage: Stage) {
        primaryStage.title = "GameFX Test"

        val root = Group()
        val scene = Scene(root)
        primaryStage.scene = scene

        val canvas = Canvas()
        root.children.add(canvas)

        val game = GameFX { game ->
            game.canvas = canvas
            game.canvasScale = Vector2(640f, 480f)

            game.add(Circle { circle ->
                circle.radius = 100f
                circle.center = game.canvasCenter
                circle.fill = Color.BLUE

                canvas.addEventFilter(MouseEvent.MOUSE_MOVED) { event ->
                    val point = Vector2(event.x.toFloat(), event.y.toFloat())
                    circle.fill = if (point in circle) Color.GREEN else Color.RED
                }
            })

            game.add(RegularPolygon { polygon ->
                polygon.radius = 100f
                polygon.center = game.canvasCenter
                polygon.fill = Color.ORANGE
            })
        }

        game.start()

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.code == KeyCode.ESCAPE) {
                game.stop()
                primaryStage.close()
            }
        }

        primaryStage.widthProperty().addListener { observable, oldValue, newValue ->
            //game.canvasScale = game.canvasScale.withX(newValue.toFloat())
            game.canvasScale = Vector2(newValue.toFloat(), game.canvasScale.y)
        }
        primaryStage.widthProperty().addListener { observable, oldValue, newValue ->
            //game.canvasScale = game.canvasScale.withY(newValue.toFloat())
            game.canvasScale = Vector2(game.canvasScale.x, newValue.toFloat())
        }

        primaryStage.show()
    }
}
