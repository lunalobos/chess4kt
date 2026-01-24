/*
 * Copyright 2026 Miguel Angel Luna Lobos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/lunalobos/chessapi4j/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.lunalobos.chess4kt.js

import kotlin.math.max

/**
 * This class represents a bitboard.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Bitboard internal constructor(private val value: Long) {

    companion object {
        private fun reverseBytes(i: Long): Long {
            var i = i
            i = (i and 0x00ff00ff00ff00ffL) shl 8 or ((i ushr 8) and 0x00ff00ff00ff00ffL)
            return (i shl 48) or ((i and 0xffff0000L) shl 16) or
                    ((i ushr 16) and 0xffff0000L) or (i ushr 48)
        }

        private fun reverse(i: Long): Long {
            var i = i
            i = (i and 0x5555555555555555L) shl 1 or ((i ushr 1) and 0x5555555555555555L)
            i = (i and 0x3333333333333333L) shl 2 or ((i ushr 2) and 0x3333333333333333L)
            i = (i and 0x0f0f0f0f0f0f0f0fL) shl 4 or ((i ushr 4) and 0x0f0f0f0f0f0f0f0fL)
            return reverseBytes(i)
        }

        fun fromSquares(vararg squares: Square): Bitboard {
            var value = 0L
            for (i in squares) {
                value = value or (1L shl i.ordinal)
            }
            return Bitboard(value)
        }

        fun empty() = Bitboard(0)
    }

    override fun toString(): String {
        val sb = StringBuilder(500)
        val inverted: Long = reverse(value)
        val mask = 255L
        sb.append("\n+---+---+---+---+---+---+---+---+ \n")
        for (i in 0..7) {
            val isb = StringBuilder(24)
            val masked = (inverted and (mask shl (i * 8))) ushr (i * 8)
            val lz = masked.countLeadingZeroBits() - 56
            isb.append("0".repeat(max(0, lz)))
            isb.append(if (masked == 0L) "" else masked.toString(2))
            val characters = isb.toString().toCharArray()
            for (c in characters) {
                sb.append('|').append(' ').append(c).append(' ')
            }
            sb.append('|')
            sb.append("\n+---+---+---+---+---+---+---+---+ \n")
        }
        return sb.toString()
    }

    fun peekLastBit() = Bitboard(value.takeLowestOneBit())

    fun peekFirstBit() = Bitboard(value.takeHighestOneBit())

    fun trailingZeros() = value.countTrailingZeroBits()

    fun leadingZeros() = value.countLeadingZeroBits()

    infix fun and(other: Bitboard) = Bitboard(value and other.value)

    infix fun or(other: Bitboard) = Bitboard(value or other.value)

    infix fun xor(other: Bitboard) = Bitboard(value xor other.value)

    fun inv() = Bitboard(value.inv())

    infix fun shl(i: Int) = Bitboard(value shl i)

    infix fun ushr(i: Int) = Bitboard(value ushr i)
}