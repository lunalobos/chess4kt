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
package io.github.lunalobos.chess4kt



internal class KnightMovesGenerator(
    private val movesObj: Moves
) {
    private val knightMovesMatrix =
        knightMatrix.map { it.map { sq -> 1L shl sq }.reduceOrNull { acc, element -> acc or element } ?: 0L }
            .toLongArray()

    fun knightMoves(
        br: Long, square: Int, pieceType: Int, enemies: Long, friends: Long, checkMask: Long, inCheckMask: Long
    ): RegularPieceMoves {
        val emptyOrEnemy = friends.inv()
        val moves: Long = knightMovesMatrix[square]
        val pinMask = pin[((br and checkMask) ushr (br and checkMask).countTrailingZeroBits()).toInt()]
        return RegularPieceMoves(
            pieceType,
            square,
            enemies,
            moves and emptyOrEnemy and pinMask and inCheckMask,
            movesObj
        )
    }

    companion object {
        internal val pin = longArrayOf(-1L, 0L)
    }
}