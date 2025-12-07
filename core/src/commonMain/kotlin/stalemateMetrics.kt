/*
 * Copyright 2025 Miguel Angel Luna Lobos
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

internal fun isStalemate(bitboards: LongArray, isWhiteMove: Boolean, legalMoves: Long): Boolean {
    val (friends,enemies) = friendsAndEnemies(bitboards, isWhiteMove)
    val kingSquare = (bitboards[if (isWhiteMove) Piece.WK.ordinal - 1 else Piece.BK.ordinal - 1])
        .countTrailingZeroBits()
    val threats = immediateThreats(bitboards, friends, enemies)
    val isInCheck = (1L shl kingSquare) and threats
    return (isInCheck == 0L) && (legalMoves == 0L)
}