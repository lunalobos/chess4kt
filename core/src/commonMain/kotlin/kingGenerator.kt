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

internal fun yieldKingMoves(
    kingMovesMatrix: LongArray,
    threats: (bitboards: LongArray, friends: Long, enemies: Long, square: Int, wm: Boolean) -> Long,
    moveFunction: (origin: Int, target: Int) -> Move
): (square: Int, pieceType: Int, enemies: Long, friends: Long, inCheck: Boolean, bitboards: LongArray,
    wm: Boolean, wk: Long, wq: Long, bk: Long, bq: Long ) -> KingMoves {
    return {
            square: Int, pieceType: Int, enemies: Long, friends: Long, inCheck: Boolean, bitboards: LongArray,
    wm: Boolean, wk: Long, wq: Long, bk: Long, bq: Long ->

        val emptyOrEnemy = friends.inv()
        val moves = kingMovesMatrix[square]
        val threats = threats(bitboards, friends and (1L shl square).inv(), enemies, square, wm)
        val regularMoves: Long = moves and emptyOrEnemy and threats.inv()
        if (inCheck) {
            KingMoves(pieceType, square, enemies, regularMoves, 0L, moveFunction)
        } else{
            var castleMoves = 0L
            val inCheckArg = if(inCheck) 1L else 0L
            castleMoves = castleMoves or (isShortCastleWhiteEnable(square, enemies, friends, wk, inCheckArg, threats) shl 6)
            castleMoves = castleMoves or (isLongCastleWhiteEnable(square, enemies, friends, wq, inCheckArg, threats) shl 2)
            castleMoves = castleMoves or (isShortCastleBlackEnable(square, enemies, friends, bk, inCheckArg, threats) shl 62)
            castleMoves = castleMoves or (isLongCastleBlackEnable(square, enemies, friends, bq, inCheckArg, threats) shl 58)
            KingMoves(pieceType, square, enemies, regularMoves, castleMoves, moveFunction)
        }
    }
}

private fun isShortCastleWhiteEnable(
    kingSquare: Int,
    enemies: Long,
    friends: Long,
    wk: Long,
    inCheck: Long,
    threats: Long
): Long {
    val kingLocation = ((1L shl kingSquare) and (1L shl 4)) ushr 4
    val piecesInterruption1 = ((1L shl 5) and (enemies or friends)) ushr 5
    val piecesInterruption2 = ((1L shl 6) and (enemies or friends)) ushr 6
    val check1 = (threats and (1L shl 5)) ushr 5
    val check2 = (threats and (1L shl 6)) ushr 6
    return kingLocation and piecesInterruption1.inv() and piecesInterruption2.inv() and wk and check1.inv() and check2.inv() and inCheck.inv()
}
private fun isShortCastleBlackEnable(
    kingSquare: Int,
    enemies: Long,
    friends: Long,
    bk: Long,
    inCheck: Long,
    threats: Long
): Long {
    val kingLocation = ((1L shl kingSquare) and (1L shl 60)) ushr 60
    val piecesInterruption1 = ((1L shl 61) and (enemies or friends)) ushr 61
    val piecesInterruption2 = ((1L shl 62) and (enemies or friends)) ushr 62
    val check1 = (threats and (1L shl 61)) ushr 61
    val check2 = (threats and (1L shl 62)) ushr 62
    return kingLocation and piecesInterruption1.inv() and piecesInterruption2.inv() and bk and check1.inv() and check2.inv() and inCheck.inv()
}

private fun isLongCastleWhiteEnable(
    kingSquare: Int,
    enemies: Long,
    friends: Long,
    wq: Long,
    inCheck: Long,
    threats: Long
): Long {
    val kingLocation = ((1L shl kingSquare) and (1L shl 4)) ushr 4
    val piecesInterruption1 = ((1L shl 2) and (enemies or friends)) ushr 2
    val piecesInterruption2 = ((1L shl 3) and (enemies or friends)) ushr 3
    val piecesInterruption3 = ((1L shl 1) and (enemies or friends)) ushr 1
    val check1 = (threats and (1L shl 3)) ushr 3
    val check2 = (threats and (1L shl 2)) ushr 2
    return (kingLocation and piecesInterruption1.inv() and piecesInterruption2.inv() and piecesInterruption3.inv() and wq
            and check1.inv() and check2.inv() and inCheck.inv())
}

private fun isLongCastleBlackEnable(
    kingSquare: Int,
    enemies: Long,
    friends: Long,
    bq: Long,
    inCheck: Long,
    threats: Long
): Long {
    val kingLocation = ((1L shl kingSquare) and (1L shl 60)) ushr 60
    val piecesInterruption1 = ((1L shl 58) and (enemies or friends)) ushr 58
    val piecesInterruption2 = ((1L shl 59) and (enemies or friends)) ushr 59
    val piecesInterruption3 = ((1L shl 57) and (enemies or friends)) ushr 57
    val check1 = (threats and (1L shl 58)) ushr 58
    val check2 = (threats and (1L shl 59)) ushr 59
    return (kingLocation and piecesInterruption1.inv() and piecesInterruption2.inv() and piecesInterruption3.inv() and bq
            and check1.inv() and check2.inv() and inCheck.inv())
}
