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

import io.github.lunalobos.chess4kt.Piece.*

internal class PawnMoves(
    val pawnPiece: Int,
    val originSquare: Int,
    val enemies: Long,
    val regularMoves: Long,
    val advanceEpMoves: Long,
    val promotionMoves: Long,
    val epCapture: Long,
    private val regularMoveFunction: (origin: Int, target: Int) -> Move,
    private val promotionMoveFunction: (origin: Int, target: Int, promotionPiece: Int) -> Move
) {
    val regularMovesList: List<Move> by lazy {
        bitboardToList(regularMoves) { regularMoveFunction(originSquare, it.countTrailingZeroBits()) }
    }
    val advanceEpMovesList: List<Move> by lazy {
        bitboardToList(advanceEpMoves) { regularMoveFunction(originSquare, it.countTrailingZeroBits()) }
    }
    val promotionMovesList: List<Move> by lazy {
        bitboardToCollectedList(promotionMoves) { move ->
            (if (pawnPiece == WP.ordinal) sequenceOf(WQ, WR, WB, WN) else sequenceOf(BQ, BR, BB, BN))
                .map { promotionMoveFunction(originSquare, move.countTrailingZeroBits(), it.ordinal) }.toList()
        }
    }
    val epCaptureMove: Move? by lazy {
        if (epCapture != 0L)
            moveOf(this.originSquare, epCapture.countTrailingZeroBits())
        else
            null;
    }
    val allMovesBitboard = regularMoves or advanceEpMoves or promotionMoves or epCapture
    val allMovesList: List<Move> by lazy {
        sequence {
            yieldAll(regularMovesList)
            yieldAll(advanceEpMovesList)
            yieldAll(promotionMovesList)
            epCaptureMove?.let { yield(it) }
        }.toList()
    }

    override fun toString(): String {
        return "PawnMoves(piece=${Piece.entries[pawnPiece]}, origin=${Square.entries[originSquare]}, enemies=${
            Bitboard(
                enemies
            ).toSquares()
        }, moves=$allMovesList)"
    }
}