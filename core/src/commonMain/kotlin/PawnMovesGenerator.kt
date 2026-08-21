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

//private val logger = getLogger("io.github.lunalobos.chess4kt.pawnGenerator")
import io.github.lunalobos.chess4kt.Square.*
internal class PawnMovesGenerator(
    private val visibleMetrics: VisibleMetrics,
    private val moves: Moves,
    private val checkMetrics: CheckMetrics,
) {

    private fun transformEnPassant(enPassant: Int, wm: Boolean): Int {
        return 8 * (-2 * (if (wm) 1 else 0) + 1) + enPassant
    }

    private fun transformEnPassantWhite(enPassant: Int): Int {
        return enPassant + 8
    }

    private fun transformEnPassantBlack(enPassant: Int): Int {
        return enPassant - 8
    }

    private fun isEnPassant(originSquare: Int, finalSquare: Int, wm: Boolean): Long {
        val difference = (finalSquare - originSquare).toLong()
        val maskedDifference = 16L and (if (wm) difference else (difference.inv() + 1))
        return maskedDifference ushr 4
    }

    private fun isEnPassantWhite(originSquare: Int, finalSquare: Int): Long {
        val difference = (finalSquare - originSquare).toLong()
        val maskedDifference = 16L and difference
        return maskedDifference ushr 4
    }

    private fun isEnPassantBlack(originSquare: Int, finalSquare: Int): Long {
        val difference = (finalSquare - originSquare).toLong()
        val maskedDifference = 16L and (difference.inv() + 1)
        return maskedDifference ushr 4
    }

    private fun generateEnPassantCaptureBitboard(
        move: Long, pieceType: Int, originSquare: Int, bitboards: LongArray, wm: Boolean
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
        return if (checkMetrics.inCheck(newBitboards, wm)) {
            0L
        } else {
            move
        }
    }

    private fun generateEnPassantCaptureBitboardWhite(
        move: Long,
        originSquare: Int,
        bitboards: LongArray,
        friends: Long,
        enemies: Long,

    ): Long {
        if (move == 0L) {
            return 0L
        }
        val capture = 1L shl (move.countTrailingZeroBits() - 8)
        val kingBitboard = bitboards[Piece.WK.ordinal - 1]
        return if (checkMetrics.inCheck(
                (friends and (1L shl originSquare).inv()) or move,
                enemies and capture.inv(),
                kingBitboard,
                bitboards)
            ) {
            0L
        } else {
            move
        }
    }

    private fun generateEnPassantCaptureBitboardBlack(
        move: Long,
        originSquare: Int,
        bitboards: LongArray,
        friends: Long,
        enemies: Long,
    ): Long {
        if (move == 0L) {
            return 0L
        }
        val capture = 1L shl (move.countTrailingZeroBits() + 8)
        val kingBitboard = bitboards[Piece.BK.ordinal - 1]
        return if (checkMetrics.inCheck(
                (friends and (1L shl originSquare).inv()) or move,
                enemies and capture.inv(),
                kingBitboard,
                bitboards)
        ) {
            0L
        } else {
            move
        }
    }

    fun pawnMoves(
        br: Long,
        square: Int,
        pieceType: Int,
        matrix1: Array<IntArray>,
        matrix2: Array<IntArray>,
        kingSquare: Int,
        enemies: Long,
        friends: Long,
        ep: Int,
        wm: Boolean,
        bitboards: LongArray,
        checkMask: Long,
        inCheckMask: Long
    ): PawnMoves {
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
            advancePromotionMoves = advancePromotionMoves or ((1L and isPromotion(squareToOccupy)) shl squareToOccupy)
            advanceEnPassantMoves =
                (advanceEnPassantMoves or ((1L and isEnPassant(square, squareToOccupy, wm)) shl squareToOccupy))
            advanceMoves = advanceMoves or (((1L and isPromotion(squareToOccupy).inv() and isEnPassant(
                square, squareToOccupy, wm
            ).inv())) shl squareToOccupy)
        }
        val visible = visibleMetrics.visibleSquaresRook(square, friends, enemies)
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
        return PawnMoves(
            pieceType,
            square,
            enemies,
            legalMoves,
            legalAdvanceEnPassantMoves,
            legalPromotionMoves,
            generateEnPassantCaptureBitboard(
                possibleEnPassant, pieceType, square, bitboards, wm
            ),
            moves
        )
    }

    fun pawnMovesWhite(
        br: Long,
        square: Int,
        kingSquare: Int,
        enemies: Long,
        friends: Long,
        ep: Int,
        bitboards: LongArray,
        checkMask: Long,
        inCheckMask: Long
    ): PawnMoves {
        val captureArray = pawnMatrix2[1][square]
        var captureMoves = 0L
        var captureCoronationMoves = 0L
        for (squareToCapture in captureArray) {
            captureCoronationMoves =
                captureCoronationMoves or ((1L and isPromotionWhite(squareToCapture)) shl squareToCapture)
            captureMoves = captureMoves or ((1L and isPromotionWhite(squareToCapture).inv()) shl squareToCapture)
        }
        val normalizedEnPassant = transformEnPassantWhite(ep)
        val possibleEnPassant = (1L shl normalizedEnPassant) and captureMoves
        captureMoves = captureMoves and enemies
        captureCoronationMoves = captureCoronationMoves and enemies
        val advanceMatrix = pawnMatrix1[1][square]
        var advanceMoves = 0L
        var advancePromotionMoves = 0L
        var advanceEnPassantMoves = 0L
        for (squareToOccupy in advanceMatrix) {
            advancePromotionMoves = advancePromotionMoves or ((1L and isPromotionWhite(squareToOccupy)) shl squareToOccupy)
            advanceEnPassantMoves =
                (advanceEnPassantMoves or ((1L and isEnPassantWhite(square, squareToOccupy)) shl squareToOccupy))
            advanceMoves = advanceMoves or (((1L and isPromotionWhite(squareToOccupy).inv() and isEnPassantWhite(
                square, squareToOccupy
            ).inv())) shl squareToOccupy)
        }
        val visible = visibleMetrics.visibleSquaresRook(square, friends, enemies)
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
        return PawnMoves(
            Piece.WP.ordinal,
            square,
            enemies,
            legalMoves,
            legalAdvanceEnPassantMoves,
            legalPromotionMoves,
            generateEnPassantCaptureBitboardWhite(
                possibleEnPassant,  square, bitboards, friends, enemies
            ),
            moves
        )
    }

    fun pawnMovesBlack(
        br: Long,
        square: Int,
        kingSquare: Int,
        enemies: Long,
        friends: Long,
        ep: Int,
        bitboards: LongArray,
        checkMask: Long,
        inCheckMask: Long
    ): PawnMoves {
        val captureArray = pawnMatrix2[0][square]
        var captureMoves = 0L
        var captureCoronationMoves = 0L
        for (squareToCapture in captureArray) {
            captureCoronationMoves =
                captureCoronationMoves or ((1L and isPromotionBlack(squareToCapture)) shl squareToCapture)
            captureMoves = captureMoves or ((1L and isPromotionBlack(squareToCapture).inv()) shl squareToCapture)
        }
        val normalizedEnPassant = transformEnPassantBlack(ep)
        val possibleEnPassant = (1L shl normalizedEnPassant) and captureMoves
        captureMoves = captureMoves and enemies
        captureCoronationMoves = captureCoronationMoves and enemies
        val advanceMatrix = pawnMatrix1[0][square]
        var advanceMoves = 0L
        var advancePromotionMoves = 0L
        var advanceEnPassantMoves = 0L
        for (squareToOccupy in advanceMatrix) {
            advancePromotionMoves = advancePromotionMoves or ((1L and isPromotionBlack(squareToOccupy)) shl squareToOccupy)
            advanceEnPassantMoves =
                (advanceEnPassantMoves or ((1L and isEnPassantBlack(square, squareToOccupy)) shl squareToOccupy))
            advanceMoves = advanceMoves or (((1L and isPromotionBlack(squareToOccupy).inv() and isEnPassantBlack(
                square, squareToOccupy
            ).inv())) shl squareToOccupy)
        }
        val visible = visibleMetrics.visibleSquaresRook(square, friends, enemies)
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
        return PawnMoves(
            Piece.BP.ordinal,
            square,
            enemies,
            legalMoves,
            legalAdvanceEnPassantMoves,
            legalPromotionMoves,
            generateEnPassantCaptureBitboardBlack(
                possibleEnPassant,  square, bitboards, friends, enemies
            ),
            moves
        )
    }

    companion object {
        internal fun isPromotion(finalSquare: Int): Long {
            return (((((finalSquare ushr 3).toLong() and 7L) ushr 2) and ((((finalSquare ushr 3).toLong() and 7L) ushr 1) and 1L) and (((finalSquare ushr 3).toLong() and 7L) and 1L)) or (((((63 - finalSquare) ushr 3).toLong() and 7L) ushr 2) and (((((63 - finalSquare) ushr 3).toLong() and 7L) ushr 1) and 1L) and ((((63 - finalSquare) ushr 3).toLong() and 7L) and 1L)))
        }

        private val whitePromotion = Bitboard.fromSquares(A8, B8, C8, D8, E8, F8, G8, H8).value
        private val blackPromotion = Bitboard.fromSquares(A1, B1, C1, D1, E1, F1, G1, H1).value

        internal fun isPromotionWhite(finalSquare: Int): Long {
            return isPresentAsInt((1L shl finalSquare) and whitePromotion).toLong()
        }
        internal fun isPromotionBlack(finalSquare: Int): Long {
            return isPresentAsInt((1L shl finalSquare) and blackPromotion).toLong()
        }
    }
}
