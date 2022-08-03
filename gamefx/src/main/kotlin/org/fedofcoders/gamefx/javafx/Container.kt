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

open class Container : Node() {
    val children: MutableList<Node> = arrayListOf()

    override fun <T> fire(eventName: String, arg: T): EventEmitter {
        super.fire(eventName, arg)
        children.forEach { it.fire(eventName, arg) }
        return this
    }

    protected fun <T> fireOnlyForThis(eventName: String, arg: T): EventEmitter {
        super.fire(eventName, arg)
        return this
    }

    fun add(child: Node): Container {
        if (child.parent == null) {
            children.add(child)
            return this
        } else {
            throw IllegalArgumentException("child is already attached to container")
        }
    }

    fun remove(child: Node): Container {
        val childWasRemoved = children.remove(child)
        if (childWasRemoved) child.parent = null
        return this
    }

    override fun <T : Node> findByID(id: String): T? {
        if (this.id == id) {
            return this as T
        } else {
            for (child in children) {
                if (child.id == id) {
                    return child as T
                } else {
                    val foundInChild = child.findByID<T>(id)
                    if (foundInChild != null) return foundInChild
                }
            }
            return null
        }
    }

    override fun draw(context: GraphicsContext) {
        if (active) {
            children.forEach {
                it.draw(context)
                it.fire("update", GameFXEvent(this))
            }
            fireOnlyForThis("update", GameFXEvent(this))
        }
    }
}
