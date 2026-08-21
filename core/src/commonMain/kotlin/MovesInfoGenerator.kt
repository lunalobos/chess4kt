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

//private val logger = getLogger("io.github.lunalobos.chess4kt.bitboardGenerator")

internal class MovesInfoGenerator(
    private val pawnMovesGenerator: PawnMovesGenerator,
    private val knightMovesGenerator: KnightMovesGenerator,
    private val bishopMovesGenerator: BishopMovesGenerator,
    private val rookMovesGenerator: RookMovesGenerator,
    private val queenMovesGenerator: QueenMovesGenerator,
    private val kingMovesGenerator: KingMovesGenerator,
    private val checkMaskGenerator: CheckMaskGenerator,
    private val checkInfoGenerator: CheckInfoGenerator
    ) {

    fun movesInfo(bitboards: LongArray, wm: Boolean, wk: Boolean, wq: Boolean, bk: Boolean, bq: Boolean, enPassant: Int): MovesInfo {
        val knightPiece: Int
        val bishopPiece: Int
        val rookPiece: Int
        val queenPiece: Int
        val kingPiece: Int
        val kingSquare: Int
        val friends: Long
        val enemies: Long
        val inCheck: Boolean
        val inCheckMask: Long
        val checkMask: Long
        val pawnMoves: MutableList<PawnMoves>
        if (wm) {

            knightPiece = Piece.WN.ordinal
            bishopPiece = Piece.WB.ordinal
            rookPiece = Piece.WR.ordinal
            queenPiece = Piece.WQ.ordinal
            kingPiece = Piece.WK.ordinal
            kingSquare = bitboards[kingPiece - 1].countTrailingZeroBits()
            friends = (bitboards[Piece.WP.ordinal - 1] or bitboards[Piece.WN.ordinal - 1]
                    or bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WR.ordinal - 1]
                    or bitboards[Piece.WQ.ordinal - 1] or bitboards[Piece.WK.ordinal - 1])
            enemies = (bitboards[Piece.BP.ordinal - 1] or bitboards[Piece.BN.ordinal - 1]
                    or bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BR.ordinal - 1]
                    or bitboards[Piece.BQ.ordinal - 1] or bitboards[Piece.BK.ordinal - 1])
            val checkInfo = checkInfoGenerator.checkInfoWhite(
                friends,
                enemies,
                bitboards,
                kingSquare
            )
            inCheck = checkInfo.inCheck
            inCheckMask = checkInfo.inCheckMask
            checkMask = checkMaskGenerator.checkMaskWhite(
                kingSquare, enemies, friends, bitboards
            )
            pawnMoves = bitboardToList(bitboards[Piece.WP.ordinal - 1]) {
                pawnMovesGenerator.pawnMovesWhite(
                    it, it.countTrailingZeroBits(),
                    kingSquare, enemies,
                    friends, enPassant, bitboards, checkMask,
                    inCheckMask
                )
            }
        } else {
            knightPiece = Piece.BN.ordinal
            bishopPiece = Piece.BB.ordinal
            rookPiece = Piece.BR.ordinal
            queenPiece = Piece.BQ.ordinal
            kingPiece = Piece.BK.ordinal
            kingSquare = bitboards[kingPiece - 1].countTrailingZeroBits()
            friends = (bitboards[Piece.BP.ordinal - 1] or bitboards[Piece.BN.ordinal - 1]
                    or bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BR.ordinal - 1]
                    or bitboards[Piece.BQ.ordinal - 1] or bitboards[Piece.BK.ordinal - 1])
            enemies = (bitboards[Piece.WP.ordinal - 1] or bitboards[Piece.WN.ordinal - 1]
                    or bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WR.ordinal - 1]
                    or bitboards[Piece.WQ.ordinal - 1] or bitboards[Piece.WK.ordinal - 1])
            val checkInfo = checkInfoGenerator.checkInfoBlack(
                friends,
                enemies,
                bitboards,
                kingSquare
            )
            inCheck = checkInfo.inCheck
            inCheckMask = checkInfo.inCheckMask
            checkMask = checkMaskGenerator.checkMaskBlack(
                kingSquare, enemies, friends, bitboards
            )
            pawnMoves = bitboardToList(bitboards[Piece.BP.ordinal - 1]) {
                pawnMovesGenerator.pawnMovesBlack(
                    it, it.countTrailingZeroBits(),
                    kingSquare, enemies,
                    friends, enPassant, bitboards, checkMask,
                    inCheckMask
                )
            }
        }

        // Knight Moves
        val knightMoves = bitboardToList(bitboards[knightPiece - 1]) {
            knightMovesGenerator.knightMoves(
                it, it.countTrailingZeroBits(), knightPiece, enemies,
                friends, checkMask, inCheckMask
            )
        }
        // Bishop Moves
        val bishopMoves = bitboardToList(bitboards[bishopPiece - 1]) {
            bishopMovesGenerator.bishopMoves(
                it, it.countTrailingZeroBits(), bishopPiece, kingSquare,
                enemies, friends, checkMask, inCheckMask
            )
        }
        // Rook Moves
        val rookMoves = bitboardToList(bitboards[rookPiece - 1]) {
            rookMovesGenerator.rookMoves(
                it, it.countTrailingZeroBits(), rookPiece, kingSquare,
                enemies, friends, checkMask, inCheckMask
            )
        }
        // Queen Moves
        val queenMoves = bitboardToList(bitboards[queenPiece - 1]) {
            queenMovesGenerator.queenMoves(
                it, it.countTrailingZeroBits(), queenPiece, kingSquare,
                friends, enemies, checkMask, inCheckMask
            )
        }

        // King Moves
        val kingMoves = kingMovesGenerator.kingMoves(
            bitboards[kingPiece - 1].countTrailingZeroBits(),
            kingPiece,
            enemies,
            friends,
            inCheck,
            bitboards,
            wm,
            if (wk) 1L else 0L,
            if (wq) 1L else 0L,
            if (bk) 1L else 0L,
            if (bq) 1L else 0L
        )

        /*.traceExit(
            "movesInfo",
            MovesInfo(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
        )*/
        return MovesInfo(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
    }
}