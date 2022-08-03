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

package org.fedofcoders.gamefx.math

data class Vector2(@JvmField var x: Float, @JvmField var y: Float) : Comparable<Vector2> {
    constructor() : this(0f, 0f)
    constructor(f: Float) : this(f, f)
    constructor(vector: Vector2) : this(vector.x, vector.y)

    companion object {
        //region Constants
        @JvmStatic fun down():  Vector2 = Vector2(0f, -1f)
        @JvmStatic fun left():  Vector2 = Vector2(-1f, 0f)
        @JvmStatic fun one():   Vector2 = Vector2(1f,  1f)
        @JvmStatic fun right(): Vector2 = Vector2(1f,  0f)
        @JvmStatic fun up():    Vector2 = Vector2(0f,  1f)
        @JvmStatic fun zero():  Vector2 = Vector2(0f,  0f)
        //endregion

        //region Interpolation
        @JvmOverloads
        @JvmStatic fun lerp(a: Vector2, b: Vector2, t: Float, dest: Vector2 = Vector2()): Vector2 =
            lerpUnclamped(a, b, FloatMath.clamp01(t), dest)
        @JvmOverloads
        @JvmStatic fun lerpUnclamped(a: Vector2, b: Vector2, t: Float, dest: Vector2 = Vector2()): Vector2 =
            // (b - a)*t + a
            dest.set(b).minus(a, dest).times(t, dest).plus(a, dest)
        @JvmOverloads
        @JvmStatic fun moveTowards(a: Vector2, b: Vector2, maxDistance: Float, dest: Vector2 = Vector2()): Vector2 =
            // (b - a).clampLength(maxDistance)
            dest.set(b).minus(a, dest).clampLength(maxDistance, dest)
        //endregion

        @JvmStatic fun min(a: Vector2, b: Vector2): Vector2 =
            Vector2(FloatMath.min(a.x, b.x), FloatMath.min(a.y, b.y))
        @JvmStatic fun max(a: Vector2, b: Vector2): Vector2 =
            Vector2(FloatMath.max(a.x, b.x), FloatMath.max(a.y, b.y))
    }

    fun set(f: Float): Vector2 = set(f, f)
    fun set(x: Float, y: Float): Vector2 {
        this.x = x
        this.y = y
        return this
    }
    fun set(vector: Vector2): Vector2 = set(vector.x, vector.y)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("2")
    }

    operator fun set(index: Int, value: Float) = when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("2")
    }

    @get:JvmName("length")
    val length: Float get() = FloatMath.sqrt(x * x + y * y)
    @get:JvmName("sqrLength")
    val sqrLength: Float get() = x * x + y * y

    @JvmOverloads
    fun normalize(dest: Vector2 = Vector2()): Vector2 {
        val sqrLen = sqrLength
        return if (sqrLen > 0f) dest.div(FloatMath.sqrt(sqrLen), dest)
        else dest.set(0f, 0f)
    }

    infix fun angleDeg(b: Vector2): Float = FloatMath.toDegrees(this angleRad b)
    infix fun angleRad(b: Vector2): Float {
        val lenProduct = length * b.length
        return if (lenProduct > 0f) FloatMath.acos((this dot b) / lenProduct)
        else 0f
    }

    @JvmOverloads
    fun clampLength(maxLength: Float, dest: Vector2 = Vector2()): Vector2 =
        if (sqrLength > maxLength * maxLength) {
            dest.normalize(dest)
            dest.times(maxLength, dest)
            dest
        } else dest

    infix fun distance(to: Vector2): Float = (this - to).length
    infix fun sqrDistance(to: Vector2): Float = (this - to).sqrLength

    infix fun dot(b: Vector2): Float = x * b.x + y * b.y

    @JvmOverloads
    fun perpindicular(dest: Vector2 = Vector2()): Vector2 = dest.set(-y, x)

    infix fun projectOn(normal: Vector2): Vector2 = projectOn(normal, Vector2())
    fun projectOn(normal: Vector2, dest: Vector2): Vector2 =
        dest.set(normal * (this dot normal) / normal.sqrLength)

    infix fun projectOnPlane(planeNormal: Vector2): Vector2 = projectOn(planeNormal, Vector2())
    fun projectOnPlane(planeNormal: Vector2, dest: Vector2): Vector2 =
        dest.set(this - (this projectOn planeNormal))

    infix fun reflect(normal: Vector2): Vector2 = projectOn(normal, Vector2())
    fun reflect(normal: Vector2, dest: Vector2): Vector2 =
        dest.set(this - normal * (normal dot this) * 2f)

    infix fun rotateDeg(deg: Float): Vector2 = rotateRad(FloatMath.toRadians(deg))
    fun rotateDeg(deg: Float, dest: Vector2): Vector2 = rotateRad(FloatMath.toRadians(deg), dest)

    infix fun rotateRad(rad: Float) = rotateRad(rad, Vector2())
    fun rotateRad(rad: Float, dest: Vector2): Vector2 {
        val ca = FloatMath.cos(rad)
        val sa = FloatMath.sin(rad)
        return dest.set(ca * x - sa * y, sa * x + ca * y)
    }

    operator fun times(b: Vector2): Vector2 = times(b, Vector2())
    fun times(b: Vector2, dest: Vector2): Vector2 = dest.set(x * b.x, y * b.y)

    operator fun plus(b: Vector2): Vector2 = plus(b, Vector2())
    fun plus(b: Vector2, dest: Vector2): Vector2 = dest.set(x + b.x, y + b.y)

    operator fun minus(b: Vector2): Vector2 = minus(b, Vector2())
    fun minus(b: Vector2, dest: Vector2): Vector2 = dest.set(x - b.x, y - b.y)

    operator fun unaryMinus(): Vector2 = negate()
    @JvmOverloads
    fun negate(dest: Vector2 = Vector2()): Vector2 = dest.set(-x, -y)

    operator fun div(s: Float): Vector2 = div(s, Vector2())
    fun div(s: Float, dest: Vector2): Vector2 {
        val inv = 1f / s
        return dest.set(x * inv, y * inv)
    }

    operator fun times(s: Float): Vector2 = times(s, Vector2())
    fun times(s: Float, dest: Vector2): Vector2 = dest.set(x * s, y * s)

    @JvmOverloads
    fun toVector3(dest: Vector3 = Vector3()): Vector3 = dest.set(x, y, 0f)
    @JvmOverloads
    fun toVector4(dest: Vector4 = Vector4()): Vector4 = dest.set(x, y, 0f, 0f)

    override operator fun compareTo(other: Vector2): Int {
        var result = x.compareTo(other.x)
        if (result != 0) return result
        result = y.compareTo(other.y)
        return result
    }

    override fun toString(): String = "($x, $y)"
}
