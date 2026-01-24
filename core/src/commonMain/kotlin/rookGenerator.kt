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

internal fun yieldRookMoves(
    visibleSquaresRook: (square: Int, friends: Long, enemies: Long) -> Long,
    moveFunction: (origin: Int, target: Int) -> Move
): (
    br: Long, square: Int, pieceType: Int, kingSquare: Int, enemies: Long,
    friends: Long, checkMask: Long, inCheckMask: Long
) -> RegularPieceMoves {
    return { br: Long, square: Int, pieceType: Int, kingSquare: Int, enemies: Long,
             friends: Long, checkMask: Long, inCheckMask: Long ->
        val defense = defenseDirection(kingSquare, square)
        val pseudoLegalMoves = visibleSquaresRook(square, friends, enemies)
        val pin = longArrayOf(-1L, pseudoLegalMoves and checkMask and defense)
        val pinMask = pin[((br and checkMask) ushr br.countTrailingZeroBits()).toInt()]
        RegularPieceMoves(
            pieceType, square, enemies, pseudoLegalMoves and pinMask and inCheckMask,
            moveFunction
        )
    }
}