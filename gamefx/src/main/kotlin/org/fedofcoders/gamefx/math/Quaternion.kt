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


data class Quaternion(@JvmField var x: Float, @JvmField var y: Float, @JvmField var z: Float, @JvmField var w: Float)
    : Comparable<Quaternion>{
    constructor() : this(0f, 0f, 0f, 1f)
    constructor(f: Float) : this(f, f, f, f)
    constructor(x: Float, y: Float, z: Float) : this(x, y, z, 1f)
    constructor(quaternion: Quaternion) : this(quaternion.x, quaternion.y, quaternion.z, quaternion.w)
    constructor(vector: Vector4) : this(vector.x, vector.y, vector.z, vector.w)

    companion object {
        //region Constants
        @JvmStatic fun identity(): Quaternion = Quaternion(0f, 0f, 0f, 1f)
        //endregion

        //region Interpolation
        @JvmOverloads
        @JvmStatic fun lerp(a: Quaternion, b: Quaternion, t: Float, dest: Quaternion = Quaternion()): Quaternion =
            lerpUnclamped(a, b, FloatMath.clamp01(t), dest)
        @JvmOverloads
        @JvmStatic fun lerpUnclamped(a: Quaternion, b: Quaternion, t: Float, dest: Quaternion = Quaternion()): Quaternion =
            // (b - a)*t + a
            dest.set(b).minus(a, dest).times(t, dest).plus(a, dest)

//        @JvmStatic fun rotateTowards(current: Quaternion, target: Quaternion, maxDistanceDelta: Float): Quaternion {
//            val difference = target - current
//            val length = difference.length
//            return if (length <= maxDistanceDelta || length == 0f) target
//            else current + difference / length * maxDistanceDelta
//        }

        @JvmOverloads
        @JvmStatic fun slerp(a: Quaternion, b: Quaternion, t: Float, dest: Quaternion = Quaternion()): Quaternion =
            slerpUnclamped(a, b, FloatMath.clamp01(t), dest)
        @JvmOverloads
        @JvmStatic fun slerpUnclamped(a: Quaternion, b: Quaternion, t: Float, dest: Quaternion = Quaternion()): Quaternion {
            val dot = a dot b
            val absDot = FloatMath.abs(dot)

            // Set the first and second scale for the interpolation
            var aScale = 1f - t
            var bScale = t

            // Check if the angle between the 2 quaternions was big enough to
            // warrant such calculations
            if (1f - absDot > 0.1f) {
                // Get the angle between the 2 quaternions,
                // and then store the sin() of that angle
                val angle = FloatMath.acos(absDot)
                val invSinTheta = 1f / FloatMath.sin(angle)

                // Calculate the scale for q1 and q2, according to the angle and
                // it's sine value
                aScale = FloatMath.sin((1f - t) * angle) * invSinTheta
                bScale = FloatMath.sin(t * angle) * invSinTheta
            }

            if (dot < 0f) bScale = -bScale

            // Calculate the x, y, z and w values for the quaternion by using a
            // special form of linear interpolation for quaternions.
            return dest.set(a.x*aScale + b.x*bScale,
                            a.y*aScale + b.y*bScale,
                            a.z*aScale + b.z*bScale,
                            a.w*aScale + b.w*bScale)
        }
        //endregion

        @JvmStatic fun min(a: Quaternion, b: Quaternion): Quaternion =
            Quaternion(FloatMath.min(a.x, b.x), FloatMath.min(a.y, b.y),
                       FloatMath.min(a.z, b.z), FloatMath.max(a.w, b.w))
        @JvmStatic fun max(a: Quaternion, b: Quaternion): Quaternion =
            Quaternion(FloatMath.max(a.x, b.x), FloatMath.max(a.y, b.y),
                       FloatMath.max(a.z, b.z), FloatMath.max(a.w, b.w))

        //region Static constructors
        @JvmStatic fun fromAngleAxisDeg(angle: Float, axis: Vector3): Quaternion =
            fromAngleAxisRad(FloatMath.toRadians(angle), axis)
        @JvmStatic fun fromAngleAxisRad(angle: Float, axis: Vector3): Quaternion {
            val halfAngle = angle * 0.5f
            val sin = FloatMath.sin(halfAngle)
            val cos = FloatMath.cos(halfAngle)
            return Quaternion(axis.x * sin, axis.y * sin, axis.z * sin, cos)
        }

        @JvmStatic fun fromEulerAnglesDeg(vector: Vector3): Quaternion =
            fromEulerAnglesRad(FloatMath.toRadians(vector.x), FloatMath.toRadians(vector.y), FloatMath.toRadians(vector.z))
        @JvmStatic fun fromEulerAnglesDeg(pitch: Float, roll: Float, yaw: Float): Quaternion =
            fromEulerAnglesRad(FloatMath.toRadians(pitch), FloatMath.toRadians(roll), FloatMath.toRadians(yaw))
        @JvmStatic fun fromEulerAnglesRad(vector: Vector3): Quaternion =
            fromEulerAnglesRad(vector.x, vector.y, vector.z)
        @JvmStatic fun fromEulerAnglesRad(pitch: Float, roll: Float, yaw: Float): Quaternion {
            val t0 = FloatMath.cos(yaw * 0.5f)
            val t1 = FloatMath.sin(yaw * 0.5f)
            val t2 = FloatMath.cos(roll * 0.5f)
            val t3 = FloatMath.sin(roll * 0.5f)
            val t4 = FloatMath.cos(pitch * 0.5f)
            val t5 = FloatMath.sin(pitch * 0.5f)

            return Quaternion(
                t0 * t2 * t4 + t1 * t3 * t5,
                t0 * t3 * t4 - t1 * t2 * t5,
                t0 * t2 * t5 + t1 * t3 * t4,
                t1 * t2 * t4 - t0 * t3 * t5)
        }
        //endregion

        fun fromRotationBetween(start: Vector3, dest: Vector3): Quaternion {
            val cosTheta = start.dot(dest)
            var axis: Vector3

            if (cosTheta < -1 + 0.001f) {
                axis = Vector3.forward().cross(start)
                if (axis.sqrLength < 0.01)
                    axis = Vector3.right().cross(start)

                axis.normalize(axis)
                return Quaternion.fromAngleAxisRad(FloatMath.PI, axis)
            }

            axis = start.cross(dest)

            val s = FloatMath.sqrt((1 + cosTheta) * 2)
            val invS = 1 / s

            return Quaternion(axis.x * invS, axis.y * invS, axis.z * invS, s * 0.5f)
        }

        fun lookAt(direction: Vector3, desiredUp: Vector3): Quaternion {
            if (direction.sqrLength < 0.0001f)
                return Quaternion.identity()

            val right = direction.cross(desiredUp)
            val desiredUp = right.cross(direction)

            val rot1 = fromRotationBetween(Vector3.forward(), direction)
            val newUp = rot1.times(Vector3.up())
            val rot2 = fromRotationBetween(newUp, desiredUp)

            return rot2.times(rot1)
        }
    }


    fun set(f: Float): Quaternion = set(f, f, f, f)
    fun set(x: Float, y: Float, z: Float): Quaternion = set(x, y, z, w)
    fun set(x: Float, y: Float, z: Float, w: Float): Quaternion {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        return this
    }
    fun set(quaternion: Quaternion): Quaternion = set(quaternion.x, quaternion.y, quaternion.z, quaternion.w)
    fun set(vector: Vector4): Quaternion = set(vector.x, vector.y, vector.z, vector.w)
    fun set(vector: Vector3, w: Float): Quaternion = set(vector.x, vector.y, vector.z, w)

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
    fun normalize(dest: Quaternion = Quaternion()): Quaternion {
        val sqrLen = sqrLength
        return if (sqrLen > 0f) dest.div(FloatMath.sqrt(sqrLen), dest)
        else dest.set(0f, 0f, 0f, 1f)
    }

    infix fun angleDeg(b: Quaternion): Float = FloatMath.toDegrees(this angleRad b)
    infix fun angleRad(b: Quaternion): Float = (this * Vector3.forward()) angleRad (b * Vector3.forward())

    @JvmOverloads
    fun conjugate(dest: Quaternion = Quaternion()): Quaternion = dest.set(-x, -y, -z, w)

    infix fun dot(b: Quaternion): Float = x * b.x + y * b.y + z * b.z + w * b.w

    @JvmOverloads
    fun invert(dest: Quaternion = Quaternion()): Quaternion {
        val len = 1f / sqrLength
        if (len > 0f) {
            val invLen = 1f / sqrLength
            return dest.set(-x * invLen, -y * invLen, -z * invLen, w * invLen)
        } else {
            return dest.set(0f, 0f, 0f, 1f)
        }
    }

    operator fun plus(b: Quaternion): Quaternion = plus(b, Quaternion())
    fun plus(b: Quaternion, dest: Quaternion): Quaternion = dest.set(x + b.x, y + b.y, z + b.z, w + b.w)

    operator fun minus(b: Quaternion): Quaternion = minus(b, Quaternion())
    fun minus(b: Quaternion, dest: Quaternion): Quaternion = dest.set(x - b.x, y - b.y, z - b.z, w - b.w)

    operator fun times(s: Float): Quaternion = times(s, Quaternion())
    fun times(s: Float, dest: Quaternion): Quaternion = dest.set(x * s, y * s, z * s, w * s)

    operator fun div(s: Float): Quaternion = div(s, Quaternion())
    fun div(s: Float, dest: Quaternion): Quaternion {
        val inv = 1f / s
        return dest.set(x * inv, y * inv, z * inv, w * inv)
    }

    operator fun times(b: Quaternion): Quaternion = times(b, Quaternion())
    fun times(b: Quaternion, dest: Quaternion): Quaternion =
        dest.set(w * b.w - x * b.x - y * b.y - z * b.z,
                 w * b.x + x * b.w + y * b.z - z * b.y,
                 w * b.y + y * b.w + z * b.x - x * b.z,
                 w * b.z + z * b.w + x * b.y - y * b.x)

    operator fun times(vector: Vector3): Vector3 = times(vector, Vector3())
    fun times(vector: Vector3, dest: Vector3): Vector3 {
        val m00 =  w*w + x*x - z*z - y*y
        val m01 =  x*y + z*w + z*w + x*y
        val m02 =  x*z - y*w + x*z - y*w

        val m10 = -z*w + x*y - z*w + x*y
        val m11 =  y*y - z*z + w*w - x*x
        val m12 =  y*z + y*z + x*w + x*w

        val m20 =  y*w + x*z + x*z + y*w
        val m21 =  y*z + y*z - x*w - x*w
        val m22 =  z*z - y*y - x*x + w*w

        return dest.set(vector.x*m00 + vector.y*m10 + vector.z*m20,
                        vector.x*m01 + vector.y*m11 + vector.z*m21,
                        vector.x*m02 + vector.y*m12 + vector.z*m22)
    }

    operator fun unaryMinus(): Quaternion = conjugate()

    fun toEulerAngles(dest: Vector3 = Vector3()): Vector3 =
        dest.set(FloatMath.atan2(2f * (x * w - y * z), 1f - 2f * (x * x + y * y)),
            FloatMath.asin( 2f * (x * z + y * w)),
            FloatMath.atan2(2f * (z * w - x * y), 1f - 2f * (y * y + z * z)))

    @JvmOverloads
    fun toAngleAxis(dest: Vector4 = Vector4()): Vector4 {
//        var x = this.x
//        var y = this.y
//        var z = this.z
//        var w = this.w
//        if (w > 1f) {
//            val invLen = 1f / length
//            x *= invLen
//            y *= invLen
//            z *= invLen
//            w *= invLen
//        }
        val angle = 2f * FloatMath.acos(w)
        var s = FloatMath.sqrt(1f - w * w)
        if (s < 1e-6) {
            dest.set(x, y, z, angle)
        } else {
            s = 1f / s
            dest.set(x * s, y * s, z * s, angle)
        }
        return dest
    }

    @JvmOverloads
    fun toMatrix4x4(dest: Matrix4x4 = Matrix4x4.zero()): Matrix4x4 = dest.set(
         w*w + x*x - z*z - y*y, x*y + z*w + z*w + x*y, x*y - y*w + x*y - y*w, 0f,
        -z*w + x*y - z*w + x*y, y*y - z*z + w*w - x*x, y*z + y*z + x*w + x*w, 0f,
         y*w + x*y + x*y + y*w, y*z + y*z - x*w - x*w, z*z - y*y - x*x + w*w, 0f,
         0f,                    0f,                    0f,                    1f)

    @JvmOverloads
    fun toVector4(dest: Vector4 = Vector4()): Vector4 = dest.set(x, y, z, w)

    override operator fun compareTo(other: Quaternion): Int {
        var result = z.compareTo(other.x)
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
