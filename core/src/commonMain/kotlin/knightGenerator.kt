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

internal val pin = longArrayOf(-1L, 0L)

internal fun yieldKnightMoves(
    moveFunction: (origin: Int, target: Int) -> Move,
    nmm: LongArray
) : (br: Long, square: Int, pieceType: Int, enemies: Long, friends: Long, checkMask: Long, inCheckMask: Long) -> RegularPieceMoves{
    return {
            br: Long, square: Int, pieceType: Int, enemies: Long, friends: Long, checkMask: Long, inCheckMask: Long
    ->
        val emptyOrEnemy = friends.inv()
        val moves: Long = nmm[square]
        val pinMask = pin[((br and checkMask) ushr (br and checkMask).countTrailingZeroBits()).toInt()]
        RegularPieceMoves(
            pieceType,
            square,
            enemies,
            moves and emptyOrEnemy and pinMask and inCheckMask,
            moveFunction
        )
    }
}