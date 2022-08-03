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

import org.joml.*

//region Matrix3f
//operator fun Matrix3f.get(c: Int, r: Int): Float = get(c, r)
operator fun Matrix3f.minus(m: Matrix3f): Matrix3f = sub(m)
operator fun Matrix3f.plus(m: Matrix3fc): Matrix3fc = add(m)
operator fun Matrix3f.times(m: Matrix3fc): Matrix3f = mul(m)
operator fun Matrix3f.times(v: Vector3f): Vector3f = transform(v)
operator fun Matrix3f.times(q: Quaternionfc): Matrix3f = rotate(q)
infix fun Matrix3f.rotate(q: Quaternionfc): Matrix3f = rotate(q)
infix fun Matrix3f.transform(v: Vector3f): Vector3f = transform(v)
//endregion

//region Matrix3d
//operator fun Matrix3d.get(c: Int, r: Int): Double = get(c, r)
operator fun Matrix3d.minus(m: Matrix3d): Matrix3d = sub(m)
operator fun Matrix3d.plus(m: Matrix3dc): Matrix3d = add(m)
operator fun Matrix3d.times(m: Matrix3dc): Matrix3d = mul(m)
operator fun Matrix3d.times(m: Matrix3fc): Matrix3d = mul(m)
operator fun Matrix3d.times(v: Vector3d): Vector3d = transform(v)
operator fun Matrix3d.times(v: Vector3f): Vector3f = transform(v)
operator fun Matrix3d.times(q: Quaternionfc): Matrix3d = rotate(q)
operator fun Matrix3d.times(q: Quaterniondc): Matrix3d = rotate(q)
infix fun Matrix3d.rotate(q: Quaternionfc): Matrix3d = rotate(q)
infix fun Matrix3d.rotate(q: Quaterniondc): Matrix3d = rotate(q)
infix fun Matrix3d.transform(v: Vector3f): Vector3f = transform(v)
infix fun Matrix3d.transform(v: Vector3d): Vector3d = transform(v)
//endregion

//region Matrix4x3f
//operator fun Matrix4x3f.get(c: Int, r: Int): Float = get(c, r)
operator fun Matrix4x3f.minus(m: Matrix4x3f): Matrix4x3f = sub(m)
operator fun Matrix4x3f.plus(m: Matrix4x3fc): Matrix4x3f = add(m)
operator fun Matrix4x3f.times(m: Matrix4x3fc): Matrix4x3f = mul(m)
operator fun Matrix4x3f.times(v: Vector4f): Vector4f = transform(v)
operator fun Matrix4x3f.times(q: Quaternionfc): Matrix4x3f = rotate(q)
infix fun Matrix4x3f.rotate(q: Quaternionfc): Matrix4x3f = rotate(q)
infix fun Matrix4x3f.transform(v: Vector4f): Vector4f = transform(v)
infix fun Matrix4x3f.transformPosition(v: Vector3f): Vector3f = transformPosition(v)
infix fun Matrix4x3f.transformDirection(v: Vector3f): Vector3f = transformDirection(v)
//endregion

//region Matrix4x3d
//operator fun Matrix4x3d.get(c: Int, r: Int): Double = get(c, r)
operator fun Matrix4x3d.minus(m: Matrix4x3dc): Matrix4x3d = sub(m)
operator fun Matrix4x3d.plus(m: Matrix4x3dc): Matrix4x3d = add(m)
operator fun Matrix4x3d.times(m: Matrix4x3fc): Matrix4x3d = mul(m)
operator fun Matrix4x3d.times(m: Matrix4x3dc): Matrix4x3d = mul(m)
operator fun Matrix4x3d.times(v: Vector4d): Vector4d = transform(v)
operator fun Matrix4x3d.times(q: Quaternionfc): Matrix4x3d = rotate(q)
operator fun Matrix4x3d.times(q: Quaterniondc): Matrix4x3d = rotate(q)
infix fun Matrix4x3d.rotate(q: Quaternionfc): Matrix4x3d = rotate(q)
infix fun Matrix4x3d.rotate(q: Quaterniondc): Matrix4x3d = rotate(q)
infix fun Matrix4x3d.transform(v: Vector4d): Vector4d = transform(v)
infix fun Matrix4x3d.transformPosition(v: Vector3d): Vector3d = transformPosition(v)
infix fun Matrix4x3d.transformDirection(v: Vector3d): Vector3d = transformDirection(v)

//region Matrix4f
//operator fun Matrix4f.get(c: Int, r: Int): Float = get(c, r)
operator fun Matrix4f.minus(m: Matrix4f): Matrix4f = sub(m)
operator fun Matrix4f.plus(m: Matrix4fc): Matrix4f = add(m)
operator fun Matrix4f.times(m: Matrix4fc): Matrix4f = mul(m)
operator fun Matrix4f.times(m: Matrix4x3fc): Matrix4f = mul(m, this)
operator fun Matrix4f.times(v: Vector4f): Vector4f = transform(v)
operator fun Matrix4f.times(q: Quaternionfc): Matrix4f = rotate(q)
infix fun Matrix4f.mulAffine(m: Matrix4fc): Matrix4f = this.mulAffine(m)
infix fun Matrix4f.mulAffineR(m: Matrix4fc): Matrix4f = this.mulAffineR(m)
infix fun Matrix4f.rotate(q: Quaternionfc): Matrix4f = rotate(q)
infix fun Matrix4f.transform(v: Vector4f): Vector4f = transform(v)
infix fun Matrix4f.transformPosition(v: Vector3f): Vector3f = transformPosition(v)
infix fun Matrix4f.transformDirection(v: Vector3f): Vector3f = transformDirection(v)
//endregion

//region Matrix4d
//operator fun Matrix4d.get(c: Int, r: Int): Double = get(c, r)
operator fun Matrix4d.minus(m: Matrix4dc): Matrix4d = sub(m)
operator fun Matrix4d.plus(m: Matrix4dc): Matrix4d = add(m)
operator fun Matrix4d.times(m: Matrix4dc): Matrix4d = mul(m)
operator fun Matrix4d.times(m: Matrix4x3fc): Matrix4d = mul(m, this)
operator fun Matrix4d.times(m: Matrix4x3dc): Matrix4d = mul(m, this)
operator fun Matrix4d.times(v: Vector4d): Vector4d = transform(v)
operator fun Matrix4d.times(q: Quaternionfc): Matrix4d = rotate(q)
operator fun Matrix4d.times(q: Quaterniondc): Matrix4d = rotate(q)
infix fun Matrix4d.mulAffine(m: Matrix4dc): Matrix4d = this.mulAffine(m)
infix fun Matrix4d.mulAffineR(m: Matrix4dc): Matrix4d = this.mulAffineR(m)
infix fun Matrix4d.rotate(q: Quaternionfc): Matrix4d = rotate(q)
infix fun Matrix4d.transform(v: Vector4d): Vector4d = transform(v)
infix fun Matrix4d.transformPosition(v: Vector3d): Vector3d = transformPosition(v)
infix fun Matrix4d.transformDirection(v: Vector3f): Vector3f = transformDirection(v)
infix fun Matrix4d.transformDirection(v: Vector3d): Vector3d = transformDirection(v)
//endregion

//region Vector2f
//operator fun Vector2f.get(e: Int): Float = get(e)
operator fun Vector2f.minus(v: Vector2fc): Vector2f = sub(v)
operator fun Vector2f.plus(v: Vector2fc): Vector2f = add(v)
operator fun Vector2f.unaryMinus(): Vector2f = negate()
//endregion

//region Vector2d
operator fun Vector2d.get(e: Int): Double = get(e)
operator fun Vector2d.minus(v: Vector2fc): Vector2d = sub(v)
operator fun Vector2d.minus(v: Vector2dc): Vector2d = sub(v)
operator fun Vector2d.plus(v: Vector2fc): Vector2d = add(v)
operator fun Vector2d.plus(v: Vector2dc): Vector2d = add(v)
operator fun Vector2d.unaryMinus(): Vector2d = negate()
//endregion

//region Vector3f
//operator fun Vector3f.get(e: Int): Float = get(e)
operator fun Vector3f.minus(v: Vector3fc): Vector3f = sub(v)
operator fun Vector3f.plus(v: Vector3fc): Vector3f = add(v)
operator fun Vector3f.unaryMinus(): Vector3f = negate()
//endregion

//region Vector3d
//operator fun Vector3d.get(e: Int): Double = get(e)
operator fun Vector3d.minus(v: Vector3fc): Vector3d = sub(v)
operator fun Vector3d.minus(v: Vector3dc): Vector3d = sub(v)
operator fun Vector3d.plus(v: Vector3fc): Vector3d = add(v)
operator fun Vector3d.plus(v: Vector3dc): Vector3d = add(v)
operator fun Vector3d.unaryMinus(): Vector3d = negate()
//endregion

//region Vector4f
//operator fun Vector4f.get(e: Int): Float = get(e)
operator fun Vector4f.minus(v: Vector4fc): Vector4f = sub(v)
operator fun Vector4f.plus(v: Vector4fc): Vector4f = add(v)
operator fun Vector4f.unaryMinus(): Vector4f = negate()
//endregion

//region Vector4d
//operator fun Vector4d.get(e: Int): Double = get(e)
operator fun Vector4d.minus(v: Vector4fc): Vector4d = sub(v)
operator fun Vector4d.minus(v: Vector4dc): Vector4d = sub(v)
operator fun Vector4d.plus(v: Vector4fc): Vector4d = add(v)
operator fun Vector4d.plus(v: Vector4dc): Vector4d = add(v)
operator fun Vector4d.unaryMinus(): Vector4d = negate()
//endregion

//region Quaternionf
//operator fun Quaternionf.get(e: Int): Float = get(e)
operator fun Quaternionf.times(q: Quaternionfc): Quaternionf = mul(q)
operator fun Quaternionf.unaryMinus(): Quaternionf = conjugate()
operator fun Quaternionf.times(v: Vector3f): Vector3f = transform(v)
operator fun Quaternionf.times(v: Vector4f): Vector4f = transform(v)
//endregion

//region Quaterniond
//operator fun Quaterniond.get(e: Int): Double = get(e)
operator fun Quaterniond.times(q: Quaterniondc): Quaterniond = mul(q)
operator fun Quaterniond.unaryMinus(): Quaterniond = conjugate()
operator fun Quaterniond.times(v: Vector3d): Vector3d = transform(v)
operator fun Quaterniond.times(v: Vector4d): Vector4d = transform(v)
