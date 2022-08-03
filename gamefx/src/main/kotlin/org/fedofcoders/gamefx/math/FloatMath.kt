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

object FloatMath {
    const val PI:     Float = Math.PI.toFloat()
    const val TWO_PI: Float = PI * 2
    const val E:      Float = Math.E.toFloat()

    const val DEG_TO_RAD: Float = PI / 180f
    const val RAD_TO_DEG: Float = 180f / PI

    @JvmStatic fun abs(f:         Float): Float = if (f <= 0f) 0f - f else f
    @JvmStatic fun acos(f:        Float): Float = Math.acos(f.toDouble()).toFloat()
    @JvmStatic fun asin(f:        Float): Float = Math.asin(f.toDouble()).toFloat()
    @JvmStatic fun atan(f:        Float): Float = Math.atan(f.toDouble()).toFloat()

    @JvmStatic fun atan2(a: Float, b: Float): Float = Math.atan2(a.toDouble(), b.toDouble()).toFloat()

    @JvmStatic fun cbrt(f:        Float): Float = Math.cbrt(f.toDouble()).toFloat()
    @JvmStatic fun ceil(f:        Float): Float = Math.ceil(f.toDouble()).toFloat()
    @JvmStatic fun ceilToInt(f:   Float): Int   = Math.ceil(f.toDouble()).toInt()

    @JvmStatic fun copySign(magnitude: Float, sign: Float): Float = Math.copySign(magnitude, sign)

    @JvmStatic fun cos(f:         Float): Float = Math.cos(f.toDouble()).toFloat()
    @JvmStatic fun cosh(f:        Float): Float = Math.cosh(f.toDouble()).toFloat()
    @JvmStatic fun exp(f:         Float): Float = Math.exp(f.toDouble()).toFloat()
    @JvmStatic fun floor(f:       Float): Float = Math.floor(f.toDouble()).toFloat()
    @JvmStatic fun floorToInt(f:  Float): Int   = Math.floor(f.toDouble()).toInt()
    @JvmStatic fun log(f:         Float): Float = Math.log(f.toDouble()).toFloat()
    @JvmStatic fun log10(f:       Float): Float = Math.log10(f.toDouble()).toFloat()

    @JvmStatic fun max(a: Float, b: Float): Float = Math.max(a, b)
    @JvmStatic fun min(a: Float, b: Float): Float = Math.min(a, b)
    @JvmStatic fun pow(a: Float, b: Float): Float = Math.pow(a.toDouble(), b.toDouble()).toFloat()

    @JvmStatic fun round(f:       Float): Float = Math.round(f.toDouble()).toFloat()
    @JvmStatic fun roundToInt(f:  Float): Int   = Math.round(f.toDouble()).toInt()
    @JvmStatic fun signum(f:      Float): Float = Math.signum(f)
    @JvmStatic fun sin(f:         Float): Float = Math.sin(f.toDouble()).toFloat()
    @JvmStatic fun sinh(f:        Float): Float = Math.sinh(f.toDouble()).toFloat()
    @JvmStatic fun sqrt(f:        Float): Float = Math.sqrt(f.toDouble()).toFloat()
    @JvmStatic fun tan(f:         Float): Float = Math.tan(f.toDouble()).toFloat()
    @JvmStatic fun tanh(f:        Float): Float = Math.tanh(f.toDouble()).toFloat()
    @JvmStatic fun toDegrees(rad: Float): Float = rad * RAD_TO_DEG
    @JvmStatic fun toRadians(deg: Float): Float = deg * DEG_TO_RAD

    @JvmOverloads
    @JvmStatic fun approximately(a: Float, b: Float, error: Float = 1e-6f) = Math.abs(b - a) < error

    @JvmStatic fun clamp(n: Float, min: Float, max: Float) = Math.max(min, Math.min(n, max))
    @JvmStatic fun clamp01(n: Float) = Math.max(0f, Math.min(n, 1f))

    @JvmStatic fun nextPowerOfTwo(value: Int): Int {
        var result = value
        if (result == 0) return 1
        result--
        result = result or (result shr 1)
        result = result or (result shr 2)
        result = result or (result shr 4)
        result = result or (result shr 8)
        result = result or (result shr 16)
        return result + 1
    }

    @JvmStatic fun isPowerOfTwo(value: Int): Boolean {
        return value != 0 && value and value - 1 == 0
    }

    @JvmStatic fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * FloatMath.clamp01(t)
    @JvmStatic fun lerpUnclamped(a: Float, b: Float, t: Float): Float = a + (b - a) * t
    @JvmStatic fun inverseLerp(a: Float, b: Float, value: Float): Float =
        if (a != b) FloatMath.clamp01((value - a) / (b - a)) else 0f

    @JvmStatic fun moveTowards(current: Float, target: Float, maxDelta: Float): Float =
        if (Math.abs(target - current) <= maxDelta) target
        else current + Math.signum(target - current) * maxDelta

    @JvmStatic fun repeat(t: Float, length: Float): Float = t - FloatMath.floor(t / length) * length

    @JvmStatic fun smoothStep(a: Float, b: Float, t: Float): Float {
        val clampedT = FloatMath.clamp01(t)
        return FloatMath.lerp(a, b, clampedT * clampedT * (3 - 2 * clampedT))
    }
    @JvmStatic fun smoothStepUnclamped(a: Float, b: Float, t: Float): Float =
        FloatMath.lerpUnclamped(a, b, t * t * (3 - 2 * t))
    @JvmStatic fun inverseSmoothStep(a: Float, b: Float, value: Float): Float {
        val t = FloatMath.inverseLerp(a, b, value)
        return FloatMath.lerp(a, b, t * t * (3 - 2 * t))
    }
}
