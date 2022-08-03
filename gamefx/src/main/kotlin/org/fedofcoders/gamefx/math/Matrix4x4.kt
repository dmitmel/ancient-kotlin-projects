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

data class Matrix4x4(var m00: Float, var m01: Float, var m02: Float, var m03: Float,
                     var m10: Float, var m11: Float, var m12: Float, var m13: Float,
                     var m20: Float, var m21: Float, var m22: Float, var m23: Float,
                     var m30: Float, var m31: Float, var m32: Float, var m33: Float) {
    constructor(): this(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f)
    constructor(f: Float): this(
        f,  0f, 0f, 0f,
        0f, f,  0f, 0f,
        0f, 0f, f,  0f,
        0f, 0f, 0f, f )
    constructor(matrix: Matrix4x4) : this(
        matrix.m00, matrix.m01, matrix.m02, matrix.m03,
        matrix.m10, matrix.m11, matrix.m12, matrix.m13,
        matrix.m20, matrix.m21, matrix.m22, matrix.m23,
        matrix.m30, matrix.m31, matrix.m32, matrix.m33)

    companion object {
        //region Constants
        @JvmStatic fun identity(): Matrix4x4 = Matrix4x4(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f)

        @JvmStatic fun zero(): Matrix4x4 = Matrix4x4(
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f)
        //endregion

        @JvmOverloads
        @JvmStatic fun lookAt(from: Vector3, to: Vector3, up: Vector3,
                              dest: Matrix4x4 = Matrix4x4.zero()): Matrix4x4 {
            val lookDir = to - from
            lookDir.normalize(lookDir)
            val upDir = up.normalize()

            val rightDir = lookDir cross upDir
            val perpUpDir = rightDir cross lookDir

            return dest.set( rightDir.x,   rightDir.y,   rightDir.z,  -from.x,
                             perpUpDir.x,  perpUpDir.y,  perpUpDir.z, -from.y,
                            -lookDir.x,   -lookDir.y,   -lookDir.z,   -from.z,
                             0f,           0f,           0f,          1f     )
        }

        @JvmStatic fun ortho(left: Float, right: Float, bottom: Float, top: Float, zNear: Float, zFar: Float,
                               dest: Matrix4x4 = Matrix4x4.zero()): Matrix4x4 {
            dest.m00 = 2f / (right - left)
            dest.m01 = 0f
            dest.m02 = 0f
            dest.m03 = 0f
            dest.m10 = 0f
            dest.m11 = 2f / (top - bottom)
            dest.m12 = 0f
            dest.m13 = 0f
            dest.m20 = 0f
            dest.m21 = 0f
            dest.m22 = 1f / (zFar - zNear)
            dest.m23 = 0f
            dest.m30 = -(right + left) / (right - left)
            dest.m31 = -(top + bottom) / (top - bottom)
            dest.m32 = -zNear / (zFar - zNear)
            dest.m33 = 1f
            return dest
        }

        @JvmStatic fun perspective(fov: Float, aspect: Float, zNear: Float, zFar: Float,
                                   dest: Matrix4x4 = Matrix4x4.zero()): Matrix4x4 {
            val tanHalfFovy = FloatMath.tan((fov * 0.5f))
            dest.m00 = 1f / (aspect * tanHalfFovy)
            dest.m01 = 0f
            dest.m02 = 0f
            dest.m03 = 0f
            dest.m10 = 0f
            dest.m11 = 1f / tanHalfFovy
            dest.m12 = 0f
            dest.m13 = 0f
            dest.m20 = 0f
            dest.m21 = 0f
            dest.m22 = (zFar + zNear) / (zFar - zNear)
            dest.m23 = 1f
            dest.m30 = 0f
            dest.m31 = 0f
            dest.m32 = -2f * zFar * zNear / (zFar - zNear)
            dest.m33 = 0f
            return dest
        }

        fun translate(translation: Vector3): Matrix4x4 =
            Matrix4x4(1f, 0f, 0f, translation.x,
                      0f, 1f, 0f, translation.y,
                      0f, 0f, 1f, translation.z,
                      0f, 0f, 0f, 1f           )

        fun rotationX(angle: Float): Matrix4x4 {
            val sin = FloatMath.sin(angle)
            val cos = FloatMath.cos(angle)
            return Matrix4x4(1f, 0f,   0f,  0f,
                             0f, cos, -sin, 0f,
                             0f, sin,  cos, 0f,
                             0f, 0f,   0f,  1f)
        }

        fun rotationY(angle: Float): Matrix4x4 {
            val sin = FloatMath.sin(angle)
            val cos = FloatMath.cos(angle)
            return Matrix4x4( cos, 0f, sin, 0f,
                              0f,  1f, 0f,  0f,
                             -sin, 0f, cos, 0f,
                              0f,  0f, 0f,  1f)
        }

        fun rotationZ(angle: Float): Matrix4x4 {
            val sin = FloatMath.sin(angle)
            val cos = FloatMath.cos(angle)
            return Matrix4x4(cos, -sin, 0f, 0f,
                             sin,  cos, 0f, 0f,
                             0f,   0f,  1f, 0f,
                             0f,   0f,  0f, 1f)
        }
    }

    fun set(f: Float): Matrix4x4 =
        set(f, 0f, 0f, 0f,
            0f, f, 0f, 0f,
            0f, 0f, f, 0f,
            0f, 0f, 0f, f)
    fun set(m00: Float, m01: Float, m02: Float, m03: Float,
            m10: Float, m11: Float, m12: Float, m13: Float,
            m20: Float, m21: Float, m22: Float, m23: Float,
            m30: Float, m31: Float, m32: Float, m33: Float): Matrix4x4 {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33
        return this
    }
    fun set(matrix: Matrix4x4): Matrix4x4 = set(
        matrix.m00, matrix.m01, matrix.m02, matrix.m03,
        matrix.m10, matrix.m11, matrix.m12, matrix.m13,
        matrix.m20, matrix.m21, matrix.m22, matrix.m23,
        matrix.m30, matrix.m31, matrix.m32, matrix.m33)

    operator fun get(index: Int): Float = when (index) {
        0  -> m00
        1  -> m01
        2  -> m02
        3  -> m03
        4  -> m10
        5  -> m11
        6  -> m12
        7  -> m13
        8  -> m20
        9  -> m21
        10 -> m22
        11 -> m23
        12 -> m30
        13 -> m31
        14 -> m32
        15 -> m33
        else -> throw IndexOutOfBoundsException("16")
    }

    operator fun set(index: Int, value: Float) = when (index) {
        0  -> m00 = value
        1  -> m01 = value
        2  -> m02 = value
        3  -> m03 = value
        4  -> m10 = value
        5  -> m11 = value
        6  -> m12 = value
        7  -> m13 = value
        8  -> m20 = value
        9  -> m21 = value
        10 -> m22 = value
        11 -> m23 = value
        12 -> m30 = value
        13 -> m31 = value
        14 -> m32 = value
        15 -> m33 = value
        else -> throw IndexOutOfBoundsException("16")
    }

    operator fun get(col: Int, row: Int): Float =
        if (col < 0 || row < 0 || col > 3 || row > 3) throw IndexOutOfBoundsException("4x4")
        else this[col + row * 4]

    operator fun set(col: Int, row: Int, value: Float) =
        if (col < 0 || row < 0 || col > 3 || row > 3) throw IndexOutOfBoundsException("4x4")
        else this[col + row * 4] = value

    @JvmOverloads
    fun getColumn(col: Int, dest: Vector4 = Vector4()): Vector4 =
        if (col < 0 || col > 3) throw IndexOutOfBoundsException("4")
        else dest.set(this[col], this[col+4], this[col+8], this[col+12])

    @JvmOverloads
    fun getRow(row: Int, dest: Vector4 = Vector4()): Vector4 =
        if (row < 0 || row > 3) throw IndexOutOfBoundsException("4")
        else dest.set(this[row*4], this[row*4+1], this[row*4+2], this[row*4+3])

    fun setColumn(col: Int, colValues: Vector4) {
        if (col < 0 || col > 3) throw IndexOutOfBoundsException("4")
        this[col]    = colValues.x
        this[col+4]  = colValues.y
        this[col+8]  = colValues.z
        this[col+12] = colValues.w
    }

    fun setRow(row: Int, rowValues: Vector4) {
        if (row < 0 || row > 3) throw IndexOutOfBoundsException("4")
        this[row*4]   = rowValues.x
        this[row*4+1] = rowValues.y
        this[row*4+2] = rowValues.z
        this[row*4+3] = rowValues.w
    }

    @get:JvmName("determinant")
    val determinant: Float get() =
        (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32) +
        (m02 * m10 - m00 * m12) * (m21 * m33 - m23 * m31) +
        (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31) +
        (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30) +
        (m03 * m11 - m01 * m13) * (m20 * m32 - m22 * m30) +
        (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30)

    @JvmOverloads
    fun invert(dest: Matrix4x4 = Matrix4x4.zero()): Matrix4x4 {
        val a = m00 * m11 - m01 * m10
        val b = m00 * m12 - m02 * m10
        val c = m00 * m13 - m03 * m10
        val d = m01 * m12 - m02 * m11
        val e = m01 * m13 - m03 * m11
        val f = m02 * m13 - m03 * m12
        val g = m20 * m31 - m21 * m30
        val h = m20 * m32 - m22 * m30
        val i = m20 * m33 - m23 * m30
        val j = m21 * m32 - m22 * m31
        val k = m21 * m33 - m23 * m31
        val l = m22 * m33 - m23 * m32
        val det = a * l - b * k + c * j + d * i - e * h + f * g

        if (det > 0f) {
            val invDet = 1f / det
            return dest.set(
                (+m11 * l - m12 * k + m13 * j) * invDet,
                (-m01 * l + m02 * k - m03 * j) * invDet,
                (+m31 * f - m32 * e + m33 * d) * invDet,
                (-m21 * f + m22 * e - m23 * d) * invDet,
                (-m10 * l + m12 * i - m13 * h) * invDet,
                (+m00 * l - m02 * i + m03 * h) * invDet,
                (-m30 * f + m32 * c - m33 * b) * invDet,
                (+m20 * f - m22 * c + m23 * b) * invDet,
                (+m10 * k - m11 * i + m13 * g) * invDet,
                (-m00 * k + m01 * i - m03 * g) * invDet,
                (+m30 * e - m31 * c + m33 * a) * invDet,
                (-m20 * e + m21 * c - m23 * a) * invDet,
                (-m10 * j + m11 * h - m12 * g) * invDet,
                (+m00 * j - m01 * h + m02 * g) * invDet,
                (-m30 * d + m31 * b - m32 * a) * invDet,
                (+m20 * d - m21 * b + m22 * a) * invDet)
        } else {
            return Matrix4x4.zero()
        }
    }

    @JvmOverloads
    fun transpose(dest: Matrix4x4 = Matrix4x4.zero()): Matrix4x4 =
        dest.set(m00, m10, m20, m30,
                 m01, m11, m21, m31,
                 m02, m12, m22, m32,
                 m03, m13, m23, m33)

//    fun translate(vector: Vector3) = translate(vector.x, vector.y, vector.z)
//    fun translate(x: Float, y: Float, z: Float) {
//        m03 += x
//        m13 += y
//        m23 += z
//    }

//    fun setRotation(quaternion: Quaternion) {
//        val norm = quaternion.norm()
//        // we explicitly test norm against one here, saving a division
//        // at the cost of a test and branch.  Is it worth it?
//        val s = if (norm == 1f) 2f else if (norm > 0f) 2f / norm else 0f
//
//        // compute xs/ys/zs first to save 6 multiplications, since xs/ys/zs
//        // will be used 2-4 times each.
//        val xs = quaternion.x * s
//        val ys = quaternion.y * s
//        val zs = quaternion.z * s
//        val xx = quaternion.x * xs
//        val xy = quaternion.x * ys
//        val xz = quaternion.x * zs
//        val xw = quaternion.w * xs
//        val yy = quaternion.y * ys
//        val yz = quaternion.y * zs
//        val yw = quaternion.w * ys
//        val zz = quaternion.z * zs
//        val zw = quaternion.w * zs
//
//        // using s=2/norm (instead of 1/norm) saves 9 multiplications by 2 here
//        m00 = 1 - (yy + zz)
//        m01 = xy - zw
//        m02 = xz + yw
//        m10 = xy + zw
//        m11 = 1 - (xx + zz)
//        m12 = yz - xw
//        m20 = xz - yw
//        m21 = yz + xw
//        m22 = 1 - (xx + yy)
//    }

//    fun scale(vector: Vector3) = translate(vector.x, vector.y, vector.z)
//    fun scale(x: Float, y: Float, z: Float) {
//        val vector = Vector3(m00, m10, m20)
//        vector.normalize()
//        vector *= x
//        m00 = vector.x
//        m10 = vector.y
//        m20 = vector.z
//
//        vector.set(m01, m11, m21)
//        vector.normalize()
//        vector *= y
//        m01 = vector.x
//        m11 = vector.y
//        m21 = vector.z
//
//        vector.set(m02, m12, m22)
//        vector.normalize()
//        vector *= z
//        m02 = vector.x
//        m12 = vector.y
//        m22 = vector.z
//    }

    operator fun times(vector: Vector3): Vector3 =
        Vector3(m00 * vector.x + m01 * vector.y + m02 * vector.z + m03,
                m10 * vector.x + m11 * vector.y + m12 * vector.z + m13,
                m20 * vector.x + m21 * vector.y + m22 * vector.z + m23)

    operator fun times(vector: Vector4): Vector4 =
        Vector4(m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w,
                m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w,
                m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w,
                m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w)

    infix fun timesDirection(vector: Vector3): Vector3 =
        Vector3(m00 * vector.x + m01 * vector.y + m02 * vector.z,
                m10 * vector.x + m11 * vector.y + m12 * vector.z,
                m20 * vector.x + m21 * vector.y + m22 * vector.z)

    operator fun times(b: Matrix4x4): Matrix4x4 = times(b, Matrix4x4.zero())
    fun times(b: Matrix4x4, dest: Matrix4x4): Matrix4x4 =
        dest.set(m00 * b.m00 + m10 * b.m01 + m20 * b.m02 + m30 * b.m03,
                 m01 * b.m00 + m11 * b.m01 + m21 * b.m02 + m31 * b.m03,
                 m02 * b.m00 + m12 * b.m01 + m22 * b.m02 + m32 * b.m03,
                 m03 * b.m00 + m13 * b.m01 + m23 * b.m02 + m33 * b.m03,
                 m00 * b.m10 + m10 * b.m11 + m20 * b.m12 + m30 * b.m13,
                 m01 * b.m10 + m11 * b.m11 + m21 * b.m12 + m31 * b.m13,
                 m02 * b.m10 + m12 * b.m11 + m22 * b.m12 + m32 * b.m13,
                 m03 * b.m10 + m13 * b.m11 + m23 * b.m12 + m33 * b.m13,
                 m00 * b.m20 + m10 * b.m21 + m20 * b.m22 + m30 * b.m23,
                 m01 * b.m20 + m11 * b.m21 + m21 * b.m22 + m31 * b.m23,
                 m02 * b.m20 + m12 * b.m21 + m22 * b.m22 + m32 * b.m23,
                 m03 * b.m20 + m13 * b.m21 + m23 * b.m22 + m33 * b.m23,
                 m00 * b.m30 + m10 * b.m31 + m20 * b.m32 + m30 * b.m33,
                 m01 * b.m30 + m11 * b.m31 + m21 * b.m32 + m31 * b.m33,
                 m02 * b.m30 + m12 * b.m31 + m22 * b.m32 + m32 * b.m33,
                 m03 * b.m30 + m13 * b.m31 + m23 * b.m32 + m33 * b.m33)

    operator fun times(s: Float): Matrix4x4 = times(s, Matrix4x4.zero())
    fun times(s: Float, dest: Matrix4x4): Matrix4x4 =
        dest.set(m00 * s, m01 * s, m02 * s, m03 * s,
                 m10 * s, m11 * s, m12 * s, m13 * s,
                 m20 * s, m21 * s, m22 * s, m23 * s,
                 m30 * s, m31 * s, m32 * s, m33 * s)

    fun toQuaternion(dest: Quaternion = Quaternion()): Quaternion {
        dest.x = FloatMath.sqrt(FloatMath.max(0f, 1f + m00 - m11 - m22)) / 2
        dest.y = FloatMath.sqrt(FloatMath.max(0f, 1f - m00 + m11 - m22)) / 2
        dest.z = FloatMath.sqrt(FloatMath.max(0f, 1f - m00 - m11 + m22)) / 2
        dest.w = FloatMath.sqrt(FloatMath.max(0f, 1f + m00 + m11 + m22)) / 2
        dest.x *= FloatMath.signum(dest.x * (m21 - m12))
        dest.y *= FloatMath.signum(dest.y * (m02 - m20))
        dest.z *= FloatMath.signum(dest.z * (m10 - m01))
        return dest
    }

    override fun toString(): String =
        "($m00, $m01, $m02, $m03,\n" +
        " $m10, $m11, $m12, $m13,\n" +
        " $m20, $m21, $m22, $m23,\n" +
        " $m30, $m31, $m32, $m33)"
}
