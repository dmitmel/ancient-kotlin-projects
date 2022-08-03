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

package org.fedofcoders.gamefx

import org.fedofcoders.gamefx.math.*
import java.util.*
import java.util.function.Consumer

open class GameObject(var name: String) : Iterable<GameObject>, LifeCycle {
    var game: Game? = null
        internal set
    var parent: GameObject? = null
        internal set
    private val children: ArrayList<GameObject> = arrayListOf()

    var tag: String = ""
    @get:JvmName("isActive")
    var active: Boolean = true

    //region Transform
    var position: Vector3 = Vector3.zero()
    var localPosition: Vector3
        get() {
            val parentPosition = parent?.position
            return if (parentPosition != null) position - parentPosition else position
        }
        set(value) {
            val parentPosition = parent?.position
            position = if (parentPosition != null) parentPosition + value else value
        }

    var rotation: Quaternion = Quaternion.identity()
//    var localRotation: Quaternionf
//        get() {
//            val parentRotation = parent?.rotation
//            return if (parentRotation != null) rotation - parentRotation else rotation
//        }
//        set(value) {
//            val parentRotation = parent?.rotation
//            rotation = if (parentRotation != null) parentRotation + value else value
//        }

    var eulerAngles: Vector3
        get() = rotation.toEulerAngles()
        set(value) { rotation = Quaternion.fromEulerAnglesDeg(
            FloatMath.toRadians(value.x),
            FloatMath.toRadians(value.y),
            FloatMath.toRadians(value.z)) }
//    var localEulerAngles: Vector3f
//        get() = localRotation.eulerAngles()
//        set(value) { localRotation = Quat(value) }

    var scale: Vector3 = Vector3.one()
    var localScale: Vector3
        get() {
            val parentScale = parent?.scale
            return if (parentScale != null) scale - parentScale else scale
        }
        set(value) {
            val parentScale = parent?.scale
            scale = if (parentScale != null) parentScale + value else value
        }

    fun translate(translation: Vector3) { position += translation }
    fun rotate(rotation: Quaternion) { this.rotation.times(rotation, this.rotation) }

//    fun lookAt(direction: Vector3f, up: Vector3f) {
//        val forwardVector = (point - position).normalize_()
//
//        val dot = glm.dot(Vector3f(0, 0, 1), forwardVector)
//
//        if (FloatMath.abs(dot - -1.0f) < 0.000001f) {
//            return Quat(FloatMath.PI, 0f, 1f, 0f)
//        }
//        if (FloatMath.abs(dot - 1.0f) < 0.000001f) {
//            return Quat()
//        }
//
//        val rotAngle = FloatMath.acos(dot)
//        val rotAxis = glm.cross(Vector3f(0, 0, 1), forwardVector)
//        rotAxis.normalize_()
//        return Quat.angleAxis(rotAngle, rotAxis)
//
//        val z = direction.normalize()
//        val x = up.cross(direction).normalize()
//        val y = direction.cross(x).normalize()
//        rotation.setFromNormalized(Matrix3f(x, y, z))
//    }
    //endregion

    private val components: ArrayList<Component> = arrayListOf()

    override fun toString(): String = name

    //region Life cycle methods
    override fun start()  = components.forEach { it.start()  }
    override fun update() = components.forEach { it.update() }
    override fun render() = components.forEach { it.render() }
    //endregion

    //region Children
    @get:JvmName("childrenCount")
    val childrenCount: Int get() = children.size

    fun addChild(child: GameObject) {
        children.add(attachChild(child))
    }

    fun addChild(index: Int, child: GameObject) {
        children.add(index, attachChild(child))
    }

    fun clearChildren() {
        for (child in children) detachChild(child)
        children.clear()
    }

    fun containsDirectChild(gameObject: GameObject): Boolean = gameObject in children

    fun containsChild(gameObject: GameObject): Boolean =
        gameObject in children || children.any { it.containsChild(gameObject) }

    fun getChild(index: Int): GameObject = children[index]

    fun removeChild(child: GameObject) {
        val isChildOfThis = children.remove(child)
        if (isChildOfThis) detachChild(child)
    }

    private fun attachChild(child: GameObject): GameObject {
        child.game = this.game
        child.parent = this
        return child
    }

    private fun detachChild(child: GameObject): GameObject {
        child.game = null
        child.parent = null
        return child
    }
    //endregion

    //region Components
    @get:JvmName("componentsCount")
    val componentsCount: Int get() = components.size

    fun addComponent(component: Component) {
        components.add(attachComponent(component))
    }

    fun addComponent(index: Int, component: Component) {
        components.add(index, detachComponent(component))
    }

    fun clearComponenets() {
        for (component in components) detachComponent(component)
        components.clear()
    }

    fun containsComponent(component: Component): Boolean = component in components

    fun getComponent(index: Int): Component = components[index]

    fun removeComponent(component: Component) {
        val isComponentOfThis = components.remove(component)
        if (isComponentOfThis) detachComponent(component)
    }

    private fun attachComponent(component: Component): Component {
        component.game = this.game
        component.gameObject = this
        return component
    }

    private fun detachComponent(component: Component): Component {
        component.game = null
        component.gameObject = null
        return component
    }

    @Suppress("UNCHECKED_CAST")
    fun <C : Component> findComponent(componentClass: Class<C>): C? =
        components
            .firstOrNull(componentClass::isInstance)
            ?.let { it as C }

    @Suppress("UNCHECKED_CAST")
    fun <C : Component> findComponents(componentClass: Class<C>): List<C> =
        components.filter(componentClass::isInstance) as List<C>
    //endregion

    //region Finding GameObjects
    fun findBy(predicate: (GameObject) -> Boolean): GameObject? {
        if (predicate(this)) return this
        for (child in children) {
            val foundInChild = child.findBy(predicate)
            if (foundInChild != null) return foundInChild
        }
        return null
    }

    fun findObjectsBy(predicate: (GameObject) -> Boolean): List<GameObject> {
        val result = arrayListOf<GameObject>()
        forEach(Consumer { if (predicate(it)) result.add(it) })
        return result
    }

    fun findWithName(name: String): GameObject?             = findBy { it.name == name }
    fun findObjectsWithName(name: String): List<GameObject> = findObjectsBy { it.name == name }
    fun findWithTag(tag: String): GameObject?               = findBy { it.tag == tag }
    fun findObjectsWithTag(tag: String): List<GameObject>   = findObjectsBy { it.tag == tag }
    //endregion

    //region Iterator
    override fun iterator(): Iterator<GameObject> = TreeItr()
    override fun forEach(action: Consumer<in GameObject>) {
        action.accept(this)
        for (child in children) child.forEach(action)
    }

    private inner class TreeItr : Iterator<GameObject> {
        private var childrenIterator: Iterator<GameObject> = Collections.emptyIterator()
        private var iteratorOfChild: Iterator<GameObject> = Collections.emptyIterator()
        private var firstIteration: Boolean = true

        override fun hasNext(): Boolean = firstIteration || iteratorOfChild.hasNext() || childrenIterator.hasNext()

        override fun next(): GameObject {
            if (firstIteration) {
                firstIteration = false
                childrenIterator = children.iterator()
                return this@GameObject
            } else if (iteratorOfChild.hasNext()) {
                return iteratorOfChild.next()
            } else if (childrenIterator.hasNext()) {
                iteratorOfChild = childrenIterator.next().iterator()
                return iteratorOfChild.next()
            } else {
                throw NoSuchElementException()
            }
        }
    }
    //endregion
}
