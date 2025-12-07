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

//private val logger = getLogger("io.github.lunalobos.chess4kt.pawnGenerator")

internal fun isPromotion(finalSquare: Int): Long {
    return (((((finalSquare ushr 3).toLong() and 7L) ushr 2) and ((((finalSquare ushr 3).toLong() and 7L) ushr 1) and 1L)
            and (((finalSquare ushr 3).toLong() and 7L) and 1L))
            or (((((63 - finalSquare) ushr 3).toLong() and 7L) ushr 2) and (((((63 - finalSquare) ushr 3).toLong() and 7L) ushr 1) and 1L)
            and ((((63 - finalSquare) ushr 3).toLong() and 7L) and 1L)))
}

private fun transformEnPassant(enPassant: Int, wm: Boolean): Int {
    return 8 * (-2 * (if(wm) 1 else 0) + 1) + enPassant
}

private fun isEnPassant(originSquare: Int, finalSquare: Int, wm: Boolean): Long {
    val difference = (finalSquare - originSquare).toLong()
    val maskedDifference = 16L and (if(wm) difference else (difference.inv() + 1))
    return maskedDifference ushr 4
}

private fun generateEnPassantCaptureBitboard(
    move: Long, pieceType: Int, originSquare: Int, bitboards: LongArray,
    wm: Boolean
): Long {
    if (move == 0L) {
        return 0L
    }
    val capture = 1L shl (move.countTrailingZeroBits() + (if (wm) -8 else 8))
    val newBitboards = bitboards.copyOf()
    for (index in 0..11) {
        newBitboards[index] = newBitboards[index] and (capture.inv())
    }
    newBitboards[pieceType - 1] = (newBitboards[pieceType - 1] and (1L shl originSquare).inv()) or move
    return if (inCheck(newBitboards, wm)) {
        0L
    } else {
        move
    }
}

internal fun yieldPawmMoves(
    visibleSquaresRook: (square: Int, friends: Long, enemies: Long) -> Long,
    regularMoveFunction: (origin: Int, target: Int) -> Move,
    promotionMoveFunction: (origin: Int, target: Int, promotionPiece: Int) -> Move
): (
    br: Long, square: Int, pieceType: Int, matrix1: Array<IntArray>, matrix2: Array<IntArray>, kingSquare: Int,
    enemies: Long, friends: Long, ep: Int, wm: Boolean, bitboards: LongArray, checkMask: Long, inCheckMask: Long
) -> PawnMoves

{
    return { br: Long, square: Int, pieceType: Int, matrix1: Array<IntArray>, matrix2: Array<IntArray>, kingSquare: Int,
             enemies: Long, friends: Long, ep: Int, wm: Boolean, bitboards: LongArray, checkMask: Long, inCheckMask: Long,
              ->
        val captureArray = matrix2[square]
        var captureMoves = 0L
        var captureCoronationMoves = 0L
        for (squareToCapture in captureArray) {
            captureCoronationMoves =
                captureCoronationMoves or ((1L and isPromotion(squareToCapture)) shl squareToCapture)
            captureMoves = captureMoves or ((1L and isPromotion(squareToCapture).inv()) shl squareToCapture)
        }
        val normalizedEnPassant = transformEnPassant(ep, !wm)
        val possibleEnPassant = (1L shl normalizedEnPassant) and captureMoves
        captureMoves = captureMoves and enemies
        captureCoronationMoves = captureCoronationMoves and enemies
        val advanceMatrix = matrix1[square]
        var advanceMoves = 0L
        var advancePromotionMoves = 0L
        var advanceEnPassantMoves = 0L
        for (squareToOccupy in advanceMatrix) {
            advancePromotionMoves =
                advancePromotionMoves or ((1L and isPromotion(squareToOccupy)) shl squareToOccupy)
            advanceEnPassantMoves = (advanceEnPassantMoves
                    or ((1L and isEnPassant(square, squareToOccupy, wm)) shl squareToOccupy))
            advanceMoves = advanceMoves or (((1L and isPromotion(squareToOccupy).inv()
                    and isEnPassant(square, squareToOccupy, wm).inv())) shl squareToOccupy)
        }
        val visible = visibleSquaresRook(square, friends, enemies)
        advanceMoves = advanceMoves and (friends or enemies or visible.inv()).inv()
        advancePromotionMoves = advancePromotionMoves and (friends or enemies or visible.inv()).inv()
        advanceEnPassantMoves = advanceEnPassantMoves and (friends or enemies or visible.inv()).inv()
        val pseudoPromotionMoves = advancePromotionMoves or captureCoronationMoves
        val pseudoLegalMoves = advanceMoves or captureMoves
        val operation = (br and checkMask) ushr br.countTrailingZeroBits()
        val defense = defenseDirection(kingSquare, square)
        val pin1 = longArrayOf(-1L, pseudoLegalMoves and checkMask and defense)
        val pinMask1 = pin1[operation.toInt()]
        val pin2 = longArrayOf(-1L, pseudoPromotionMoves and checkMask and defense)
        val pinMask2 = pin2[operation.toInt()]
        val pin3 = longArrayOf(-1L, advanceEnPassantMoves and checkMask and defense)
        val pinMask3 = pin3[operation.toInt()]
        val legalMoves = pseudoLegalMoves and pinMask1 and inCheckMask
        val legalPromotionMoves = pseudoPromotionMoves and pinMask2 and inCheckMask
        val legalAdvanceEnPassantMoves = advanceEnPassantMoves and pinMask3 and inCheckMask
        PawnMoves(
            pieceType,
            square,
            enemies,
            legalMoves,
            legalAdvanceEnPassantMoves,
            legalPromotionMoves,
            generateEnPassantCaptureBitboard(
                possibleEnPassant,
                pieceType,
                square,
                bitboards,
                wm
            ),
            regularMoveFunction,
            promotionMoveFunction
        )
    }
}
