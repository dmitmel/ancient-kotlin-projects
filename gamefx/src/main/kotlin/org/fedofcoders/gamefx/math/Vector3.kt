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

data class Vector3(@JvmField var x: Float, @JvmField var y: Float, @JvmField var z: Float) : Comparable<Vector3> {
    constructor() : this(0f, 0f, 0f)
    constructor(f: Float) : this(f, f, f)
    constructor(vector: Vector3) : this(vector.x, vector.y, vector.z)

    companion object {
        //region Constants
        @JvmStatic fun back():    Vector3 = Vector3( 0f,  0f, -1f)
        @JvmStatic fun down():    Vector3 = Vector3( 0f, -1f,  0f)
        @JvmStatic fun forward(): Vector3 = Vector3( 0f,  0f,  1f)
        @JvmStatic fun left():    Vector3 = Vector3(-1f,  0f,  0f)
        @JvmStatic fun one():     Vector3 = Vector3( 1f,  1f,  0f)
        @JvmStatic fun right():   Vector3 = Vector3( 1f,  0f,  0f)
        @JvmStatic fun up():      Vector3 = Vector3( 0f,  1f,  0f)
        @JvmStatic fun zero():    Vector3 = Vector3( 0f,  0f,  0f)
        //endregion

        //region Interpolation
        @JvmOverloads
        @JvmStatic fun lerp(a: Vector3, b: Vector3, t: Float, dest: Vector3 = Vector3()): Vector3 =
            lerpUnclamped(a, b, FloatMath.clamp01(t), dest)
        @JvmOverloads
        @JvmStatic fun lerpUnclamped(a: Vector3, b: Vector3, t: Float, dest: Vector3 = Vector3()): Vector3 =
            // (b - a)*t + a
            dest.set(b).minus(a, dest).times(t, dest).plus(a, dest)
        @JvmOverloads
        @JvmStatic fun moveTowards(a: Vector3, b: Vector3, maxDistance: Float, dest: Vector3 = Vector3()): Vector3 =
            // (b - a).clampLength(maxDistance)
            dest.set(b).minus(a, dest).clampLength(maxDistance, dest)
        //endregion

        @JvmStatic fun min(a: Vector3, b: Vector3): Vector3 =
            Vector3(FloatMath.min(a.x, b.x), FloatMath.min(a.y, b.y), FloatMath.min(a.z, b.z))
        @JvmStatic fun max(a: Vector3, b: Vector3): Vector3 =
            Vector3(FloatMath.max(a.x, b.x), FloatMath.max(a.y, b.y), FloatMath.max(a.z, b.z))

        @JvmStatic fun triangleNormal(a: Vector3, b: Vector3, c: Vector3): Vector3 =
            (b - a) cross (c - a)
    }

    fun set(f: Float): Vector3 = set(f, f, f)
    fun set(x: Float, y: Float, z: Float): Vector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }
    fun set(vector: Vector3): Vector3 = set(vector.x, vector.y, vector.z)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IndexOutOfBoundsException("3")
    }

    operator fun set(index: Int, value: Float) = when (index) {
        0 -> x = value
        1 -> y = value
        2 -> z = value
        else -> throw IndexOutOfBoundsException("3")
    }

    @get:JvmName("length")
    val length: Float get() = FloatMath.sqrt(x * x + y * y + z * z)
    @get:JvmName("sqrLength")
    val sqrLength: Float get() = x * x + y * y + z * z

    @JvmOverloads
    fun normalize(dest: Vector3 = Vector3()): Vector3 {
        val sqrLen = sqrLength
        return if (sqrLen > 0f) dest.div(FloatMath.sqrt(sqrLen), dest)
        else dest.set(0f, 0f, 0f)
    }

    infix fun angleDeg(b: Vector3): Float = FloatMath.toDegrees(this angleRad b)
    infix fun angleRad(b: Vector3): Float {
        val lenProduct = length * b.length
        return if (lenProduct > 0f) FloatMath.acos((this dot b) / lenProduct)
        else 0f
    }

    @JvmOverloads
    fun clampLength(maxLength: Float, dest: Vector3 = Vector3()): Vector3 =
        if (sqrLength > maxLength * maxLength) {
            dest.normalize(dest)
            dest.times(maxLength, dest)
            dest
        } else dest

    infix fun cross(b: Vector3): Vector3 = cross(b, Vector3())
    fun cross(b: Vector3, dest: Vector3) = dest.set(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x)

    infix fun distance(to: Vector3): Float = (this - to).length
    infix fun sqrDistance(to: Vector3): Float = (this - to).sqrLength

    infix fun dot(b: Vector3): Float = x * b.x + y * b.y + z * b.z

    infix fun projectOn(normal: Vector3): Vector3 = projectOn(normal, Vector3())
    fun projectOn(normal: Vector3, dest: Vector3): Vector3 =
        dest.set(normal * (this dot normal) / normal.sqrLength)

    infix fun projectOnPlane(planeNormal: Vector3): Vector3 = projectOn(planeNormal, Vector3())
    fun projectOnPlane(planeNormal: Vector3, dest: Vector3): Vector3 =
        dest.set(this - (this projectOn planeNormal))

    infix fun reflect(normal: Vector3): Vector3 = projectOn(normal, Vector3())
    fun reflect(normal: Vector3, dest: Vector3): Vector3 =
        dest.set(this - normal * (normal dot this) * 2f)

    operator fun times(b: Vector3): Vector3 = times(b, Vector3())
    fun times(b: Vector3, dest: Vector3): Vector3 = dest.set(x * b.x, y * b.y, z * b.z)

    operator fun plus(b: Vector3): Vector3 = plus(b, Vector3())
    fun plus(b: Vector3, dest: Vector3): Vector3 = dest.set(x + b.x, y + b.y, z + b.z)

    operator fun minus(b: Vector3): Vector3 = minus(b, Vector3())
    fun minus(b: Vector3, dest: Vector3): Vector3 = dest.set(x - b.x, y - b.y, z - b.z)

    operator fun unaryMinus(): Vector3 = negate()
    @JvmOverloads
    fun negate(dest: Vector3 = Vector3()): Vector3 = dest.set(-x, -y, -z)

    operator fun div(s: Float): Vector3 = div(s, Vector3())
    fun div(s: Float, dest: Vector3): Vector3 {
        val inv = 1f / s
        return dest.set(x * inv, y * inv, z * inv)
    }

    operator fun times(s: Float): Vector3 = times(s, Vector3())
    fun times(s: Float, dest: Vector3): Vector3 = dest.set(x * s, y * s, z * s)

    @JvmOverloads
    fun toVector2(dest: Vector2 = Vector2()): Vector2 = dest.set(x, y)
    @JvmOverloads
    fun toVector4(dest: Vector4 = Vector4()): Vector4 = dest.set(x, y, z, 0f)

    override operator fun compareTo(other: Vector3): Int {
        var result = x.compareTo(other.x)
        if (result != 0) return result
        result = y.compareTo(other.y)
        if (result != 0) return result
        result = z.compareTo(other.z)
        return result
    }

    override fun toString(): String = "($x, $y, $z)"
}
