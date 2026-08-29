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

    fun movesInfoWhite(
        bitboards: LongArray,
        wk: Boolean,
        wq: Boolean,
        enPassant: Int
    ): MovesInfo {
        val knightPiece = Piece.WN.ordinal
        val bishopPiece = Piece.WB.ordinal
        val rookPiece = Piece.WR.ordinal
        val queenPiece = Piece.WQ.ordinal
        val kingPiece = Piece.WK.ordinal
        val kingSquare = bitboards[kingPiece - 1].countTrailingZeroBits()
        val friends = (bitboards[Piece.WP.ordinal - 1] or bitboards[Piece.WN.ordinal - 1]
                or bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WR.ordinal - 1]
                or bitboards[Piece.WQ.ordinal - 1] or bitboards[Piece.WK.ordinal - 1])
        val enemies = (bitboards[Piece.BP.ordinal - 1] or bitboards[Piece.BN.ordinal - 1]
                or bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BR.ordinal - 1]
                or bitboards[Piece.BQ.ordinal - 1] or bitboards[Piece.BK.ordinal - 1])
        val checkInfo = checkInfoGenerator.checkInfoWhite(
            friends,
            enemies,
            bitboards,
            kingSquare
        )
        val (inCheck, inCheckMask) = checkInfo
        val checkMask = checkMaskGenerator.checkMaskWhite(
            kingSquare, enemies, friends, bitboards
        )
        val pawnMoves = bitboardToList(bitboards[Piece.WP.ordinal - 1]) {
            pawnMovesGenerator.pawnMovesWhite(
                it, it.countTrailingZeroBits(),
                kingSquare, enemies,
                friends, enPassant, bitboards, checkMask,
                inCheckMask
            )
        }
        val kingMoves = kingMovesGenerator.kingMovesWhite(
            bitboards[kingPiece - 1].countTrailingZeroBits(),
            enemies,
            friends,
            inCheck,
            bitboards,
            if (wk) 1L else 0L,
            if (wq) 1L else 0L,
        )

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

        /*.traceExit(
            "movesInfo",
            MovesInfo(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
        )*/
        return MovesInfoBlack(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
    }

    fun movesInfoBlack(
        bitboards: LongArray,
        bk: Boolean,
        bq: Boolean,
        enPassant: Int
    ): MovesInfo {
        val knightPiece = Piece.BN.ordinal
        val bishopPiece = Piece.BB.ordinal
        val rookPiece = Piece.BR.ordinal
        val queenPiece = Piece.BQ.ordinal
        val kingPiece = Piece.BK.ordinal
        val kingSquare = bitboards[kingPiece - 1].countTrailingZeroBits()
        val friends: Long = (bitboards[Piece.BP.ordinal - 1] or bitboards[Piece.BN.ordinal - 1]
                or bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BR.ordinal - 1]
                or bitboards[Piece.BQ.ordinal - 1] or bitboards[Piece.BK.ordinal - 1])
        val enemies: Long = (bitboards[Piece.WP.ordinal - 1] or bitboards[Piece.WN.ordinal - 1]
                or bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WR.ordinal - 1]
                or bitboards[Piece.WQ.ordinal - 1] or bitboards[Piece.WK.ordinal - 1])
        val checkInfo = checkInfoGenerator.checkInfoBlack(
            friends,
            enemies,
            bitboards,
            kingSquare
        )
        val (inCheck, inCheckMask) = checkInfo
        val checkMask: Long = checkMaskGenerator.checkMaskBlack(
            kingSquare, enemies, friends, bitboards
        )
        val pawnMoves = bitboardToList(bitboards[Piece.BP.ordinal - 1]) {
            pawnMovesGenerator.pawnMovesBlack(
                it, it.countTrailingZeroBits(),
                kingSquare, enemies,
                friends, enPassant, bitboards, checkMask,
                inCheckMask
            )
        }
        val kingMoves = kingMovesGenerator.kingMovesBlack(
            bitboards[kingPiece - 1].countTrailingZeroBits(),
            enemies,
            friends,
            inCheck,
            bitboards,
            if (bk) 1L else 0L,
            if (bq) 1L else 0L
        )

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

        /*.traceExit(
            "movesInfo",
            MovesInfo(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
        )*/
        return MovesInfoWhite(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
    }
}