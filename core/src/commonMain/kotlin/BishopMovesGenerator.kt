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

internal class BishopMovesGenerator(
    private val movesObj: Moves, private val visibleMetrics: VisibleMetrics
) {
    fun bishopMoves(
        br: Long,
        square: Int,
        pieceType: Int,
        kingSquare: Int,
        enemies: Long,
        friends: Long,
        checkMask: Long,
        inCheckMask: Long
    ): RegularPieceMoves {
        val pseudoLegalMoves: Long = visibleMetrics.visibleSquaresBishop(square, friends, enemies)
        val pin = longArrayOf(-1L, pseudoLegalMoves and checkMask and defenseDirection(kingSquare, square))
        val isPin = pin[((br and checkMask) ushr (br and checkMask).countTrailingZeroBits()).toInt()]
        return RegularPieceMoves(
            pieceType, square, enemies, pseudoLegalMoves and isPin and inCheckMask, movesObj
        )
    }
}