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

data class Vector4(@JvmField var x: Float, @JvmField var y: Float, @JvmField var z: Float, @JvmField var w: Float)
    : Comparable<Vector4> {
    constructor() : this(0f, 0f, 0f, 0f)
    constructor(f: Float) : this(f, f, f, f)
    constructor(vector: Vector4) : this(vector.x, vector.y, vector.z, vector.w)

    companion object {
        //region Constants
        @JvmStatic fun one():  Vector4 = Vector4(1f, 1f, 1f, 1f)
        @JvmStatic fun zero(): Vector4 = Vector4(0f, 0f, 0f, 1f)
        //endregion

        //region Interpolation
        @JvmOverloads
        @JvmStatic fun lerp(a: Vector4, b: Vector4, t: Float, dest: Vector4 = Vector4()): Vector4 =
            lerpUnclamped(a, b, FloatMath.clamp01(t), dest)
        @JvmOverloads
        @JvmStatic fun lerpUnclamped(a: Vector4, b: Vector4, t: Float, dest: Vector4 = Vector4()): Vector4 =
            // (b - a)*t + a
            dest.set(b).minus(a, dest).times(t, dest).plus(a, dest)
        @JvmOverloads
        @JvmStatic fun moveTowards(a: Vector4, b: Vector4, maxDistance: Float, dest: Vector4 = Vector4()): Vector4 =
            // (b - a).clampLength(maxDistance)
            dest.set(b).minus(a, dest).clampLength(maxDistance, dest)
        //endregion

        @JvmStatic fun min(a: Vector4, b: Vector4): Vector4 =
            Vector4(FloatMath.min(a.x, b.x), FloatMath.min(a.y, b.y),
                    FloatMath.min(a.z, b.z), FloatMath.max(a.w, b.w))
        @JvmStatic fun max(a: Vector4, b: Vector4): Vector4 =
            Vector4(FloatMath.max(a.x, b.x), FloatMath.max(a.y, b.y),
                    FloatMath.max(a.z, b.z), FloatMath.max(a.w, b.w))
    }

    fun set(f: Float): Vector4 = set(f, f, f, f)
    fun set(x: Float, y: Float, z: Float, w: Float): Vector4 {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        return this
    }
    fun set(vector: Vector4): Vector4 = set(vector.x, vector.y, vector.z, vector.w)

    operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        2 -> z
        3 -> w
        else -> throw IndexOutOfBoundsException("4")
    }

    operator fun set(index: Int, value: Float) = when (index) {
        0 -> x = value
        1 -> y = value
        2 -> z = value
        3 -> w = value
        else -> throw IndexOutOfBoundsException("4")
    }

    @get:JvmName("length")
    val length: Float get() = FloatMath.sqrt(x * x + y * y + z * z + w * w)
    @get:JvmName("sqrLength")
    val sqrLength: Float get() = x * x + y * y + z * z + w * w

    @JvmOverloads
    fun normalize(dest: Vector4 = Vector4()): Vector4 {
        val sqrLen = sqrLength
        return if (sqrLen > 0f) dest.div(FloatMath.sqrt(sqrLen), dest)
        else dest.set(0f, 0f, 0f, 0f)
    }

    infix fun angleDeg(b: Vector4): Float = FloatMath.toDegrees(this angleRad b)
    infix fun angleRad(b: Vector4): Float {
        val lenProduct = length * b.length
        return if (lenProduct > 0f) FloatMath.acos((this dot b) / lenProduct)
        else 0f
    }

    @JvmOverloads
    fun clampLength(maxLength: Float, dest: Vector4 = Vector4()): Vector4 =
        if (sqrLength > maxLength * maxLength) {
            dest.normalize(dest)
            dest.times(maxLength, dest)
            dest
        } else dest

    infix fun distance(to: Vector4): Float = (this - to).length
    infix fun sqrDistance(to: Vector4): Float = (this - to).sqrLength

    infix fun dot(b: Vector4): Float = x * b.x + y * b.y + z * b.z + w * b.w

    infix fun projectOn(normal: Vector4): Vector4 = projectOn(normal, Vector4())
    fun projectOn(normal: Vector4, dest: Vector4): Vector4 =
        dest.set(normal * (this dot normal) / normal.sqrLength)

    infix fun projectOnPlane(planeNormal: Vector4): Vector4 = projectOn(planeNormal, Vector4())
    fun projectOnPlane(planeNormal: Vector4, dest: Vector4): Vector4 =
        dest.set(this - (this projectOn planeNormal))

    infix fun reflect(normal: Vector4): Vector4 = projectOn(normal, Vector4())
    fun reflect(normal: Vector4, dest: Vector4): Vector4 =
        dest.set(this - normal * (normal dot this) * 2f)

    operator fun times(b: Vector4): Vector4 = times(b, Vector4())
    fun times(b: Vector4, dest: Vector4): Vector4 = dest.set(x * b.x, y * b.y, z * b.z, w * b.w)

    operator fun plus(b: Vector4): Vector4 = plus(b, Vector4())
    fun plus(b: Vector4, dest: Vector4): Vector4 = dest.set(x + b.x, y + b.y, z + b.z, w + b.w)

    operator fun minus(b: Vector4): Vector4 = minus(b, Vector4())
    fun minus(b: Vector4, dest: Vector4): Vector4 = dest.set(x - b.x, y - b.y, z - b.z, w - b.w)

    operator fun unaryMinus(): Vector4 = negate()
    @JvmOverloads
    fun negate(dest: Vector4 = Vector4()): Vector4 = dest.set(-x, -y, -z, -w)

    operator fun div(s: Float): Vector4 = div(s, Vector4())
    fun div(s: Float, dest: Vector4): Vector4 {
        val inv = 1f / s
        return dest.set(x * inv, y * inv, z * inv, w * inv)
    }

    operator fun times(s: Float): Vector4 = times(s, Vector4())
    fun times(s: Float, dest: Vector4): Vector4 = dest.set(x * s, y * s, z * s, w * s)

    @JvmOverloads
    fun toVector2(dest: Vector2 = Vector2()): Vector2 = dest.set(x, y)
    @JvmOverloads
    fun toVector3(dest: Vector3 = Vector3()): Vector3 = dest.set(x, y, z)

    override operator fun compareTo(other: Vector4): Int {
        var result = x.compareTo(other.x)
        if (result != 0) return result
        result = y.compareTo(other.y)
        if (result != 0) return result
        result = z.compareTo(other.z)
        if (result != 0) return result
        result = w.compareTo(other.w)
        return result
    }

    override fun toString(): String = "($x, $y, $z, $w)"
}
