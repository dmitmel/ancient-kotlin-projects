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

import javafx.animation.AnimationTimer
import javafx.beans.value.ChangeListener
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.fedofcoders.gamefx.EventListener
import org.fedofcoders.gamefx.math.Vector2
import java.util.*

open class GameFXEvent(val target: Node) {
    override fun equals(other: Any?): Boolean =
        this === other || (other is GameFXEvent && target == other.target)

    override fun hashCode(): Int = target.hashCode()

    override fun toString(): String = "GameFXEvent(target=$target)"
}

class ResizeEvent(target: Node, val oldScale: Vector2, val newScale: Vector2) : GameFXEvent(target) {
    override fun equals(other: Any?): Boolean =
        this === other || (other is ResizeEvent && super.equals(other) &&
            oldScale == other.oldScale && newScale == other.newScale)

    override fun hashCode(): Int = Objects.hash(super.hashCode(), oldScale, newScale)

    override fun toString(): String = "ResizeEvent(target=$target, oldScale=$oldScale, newScale=$newScale)"
}

class GameFX(configure: (GameFX) -> Unit = { game -> }) : Container() {
    override var parent: Container?
        get() = null
        set(value) {
            if (value != null)
                throw UnsupportedOperationException("cannot set parent of game")
        }

    private lateinit var __canvas: Canvas
    var canvas: Canvas
        get() = __canvas
        set(value) {
            __canvas = value
            context = value.graphicsContext2D
            scene = value.scene
            stage = scene.window as Stage
        }
    lateinit var context: GraphicsContext
    lateinit var scene: Scene
    lateinit var stage: Stage

    var canvasScale: Vector2
        get() = Vector2(__canvas.width.toFloat(), __canvas.height.toFloat())
        set(value) { __canvas.width = value.x.toDouble(); __canvas.height = value.y.toDouble() }
    val canvasCenter: Vector2 get() = (canvasScale / 2f)
    var backgroundColor: Color = Color.WHITE

    var updateInterval: Long = 0
    internal var gameLoopTimer: GameLoopTimer? = null

    internal var canvasWidthChangeListener: ChangeListener<Number>? = null
    internal var canvasHeightChangeListener: ChangeListener<Number>? = null

    init {
        onNewListener = GameFXOnNewListener(this)
        onRemoveListener = GameFXOnRemoveListener(this)
        configure(this)
    }

    fun start() {
        fire("start", GameFXEvent(this))
        gameLoopTimer = GameLoopTimer(this)
        gameLoopTimer?.start()
        active = true
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            context.fill = backgroundColor
            context.fillRect(0.0, 0.0, __canvas.width, __canvas.height)
            children.forEach {
                it.draw(context)
                fire("update", GameFXEvent(this))
            }
            fireOnlyForThis("update", GameFXEvent(this))
        }
    }

    fun stop() {
        gameLoopTimer?.stop()
        gameLoopTimer = null
        fire("stop", GameFXEvent(this))
        active = false
    }
}

internal class GameFXOnNewListener(val game: GameFX) : EventListener<Pair<String, EventListener<*>>> {
    override fun handle(arg: Pair<String, EventListener<*>>) {
        val eventName = arg.first
        if (eventName == "resize") setupResizeEvent()
    }

    fun setupResizeEvent() {
        var oldVector2 = game.canvasScale

        game.canvasWidthChangeListener = ChangeListener { observable, oldValue, newValue ->
            //val newVector2 = oldVector2.withX(newValue.toFloat())
            val newVector2 = Vector2(newValue.toFloat(), oldVector2.y)
            val event = ResizeEvent(game, oldVector2, newVector2)
            game.fire("resize", event)
            oldVector2 = newVector2
        }

        game.scene.widthProperty().addListener(game.canvasWidthChangeListener)

        game.canvasHeightChangeListener = ChangeListener { observable, oldValue, newValue ->
            //val newVector2 = oldVector2.withY(newValue.toFloat())
            val newVector2 = Vector2(oldVector2.x, newValue.toFloat())
            val event = ResizeEvent(game, oldVector2, newVector2)
            game.fire("resize", event)
            oldVector2 = newVector2
        }

        game.scene.heightProperty().addListener(game.canvasHeightChangeListener)
    }
}

internal class GameFXOnRemoveListener(val game: GameFX) : EventListener<Pair<String, EventListener<*>>> {
    override fun handle(arg: Pair<String, EventListener<*>>) {
        val eventName = arg.first
        if (eventName == "resize") removeResizeEvent()
    }

    fun removeResizeEvent() {
        game.scene.widthProperty().removeListener(game.canvasWidthChangeListener)
        game.scene.heightProperty().removeListener(game.canvasHeightChangeListener)
    }
}

internal class GameLoopTimer(val game: GameFX) : AnimationTimer() {
    var lastUpdateNS: Long = 0

    override fun handle(nowNS: Long) {
        val sinceLastUpdate = nowNS - lastUpdateNS
        val updateIntervalNS = game.updateInterval * 1000000
        if (sinceLastUpdate >= updateIntervalNS) {
            game.draw(game.context)
            lastUpdateNS = System.nanoTime()
        }
    }
}
