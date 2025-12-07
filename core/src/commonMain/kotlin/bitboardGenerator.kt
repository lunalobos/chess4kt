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

//private val logger = getLogger("io.github.lunalobos.chess4kt.bitboardGenerator")

internal fun yieldCheckInfo(
    visibleSquares: (bits: LongArray, directionsIndexes: IntArray, square: Int, wm: Boolean) -> Long
): (kingPiece: Int, bits: LongArray, wm: Boolean, pawnsDirections: IntArray) -> CheckInfo {
    return { kingPiece: Int, bits: LongArray, wm: Boolean, pawnsDirections: IntArray ->
        //logger.traceEntry("checkInfo", kingPiece, bits, wm, pawnsDirections)
        var inCheckMask = 0L
        var checkCount = 0
        val kingSquare = bits[kingPiece - 1].countTrailingZeroBits()
        var isInCheck = false
        val aux = if (wm) 6 else 0
        val enemies = intArrayOf(
            Piece.WP.ordinal + aux, Piece.WN.ordinal + aux, Piece.WB.ordinal + aux,
            Piece.WR.ordinal + aux, Piece.WQ.ordinal + aux, Piece.WK.ordinal + aux
        )
        // pawns directions
        for (pawnDirection in pawnsDirections) {
            val enemyPawnDangerLocation = 1L shl pawnDirection
            val pawnScope = isPresent(bits[enemies[0] - 1] and enemyPawnDangerLocation)
            isInCheck = isInCheck || pawnScope
            inCheckMask = inCheckMask or if (pawnScope) enemyPawnDangerLocation else 0L
            checkCount += if (pawnScope) 1 else 0
        }
        //logger.trace("pawns directions inCheck=$isInCheck")
        // kings directions (only used in test cases)
        var kingDirectionsBits = 0L
        for (square in kingMatrix[kingSquare]) {
            kingDirectionsBits = kingDirectionsBits or (1L shl square)
        }
        val kingScope = isPresent(kingDirectionsBits and bits[enemies[5] - 1])
        isInCheck = isInCheck || kingScope
        checkCount += if (kingScope) 1 else 0
        //logger.trace("kings directions inCheck=$isInCheck")
        // knight directions
        var knightDirectionsBits: Long
        for (square in knightMatrix[kingSquare]) {
            knightDirectionsBits = 1L shl square
            val knightScope = isPresent(knightDirectionsBits and bits[enemies[1] - 1])
            isInCheck = isInCheck || knightScope
            inCheckMask = inCheckMask or if (knightScope) knightDirectionsBits else 0L
            checkCount += if (knightScope) 1 else 0
        }
        //logger.trace("knight directions inCheck=$isInCheck")
        // bishops directions
        val enemyBishopsAndQueens = bits[enemies[2] - 1] or bits[enemies[4] - 1]
        for (i in 0..3) {
            val visible: Long = visibleSquares(
                bits,
                intArrayOf(i),
                kingSquare,
                wm
            )
            val bishopScope = isPresent(enemyBishopsAndQueens and visible)
            isInCheck = isInCheck || bishopScope
            inCheckMask = inCheckMask or if (bishopScope) visible else 0L
            checkCount += if (bishopScope) 1 else 0
        }
        //logger.trace("bishop directions inCheck=$isInCheck")
        // rooks directions
        val enemyRooksAndQueens = bits[enemies[3] - 1] or bits[enemies[4] - 1]
        for (i in 4..7) {
            val visible: Long = visibleSquares(
                bits,
                intArrayOf(i),
                kingSquare,
                wm
            )
            val rookScope = isPresent(enemyRooksAndQueens and visible)
            isInCheck = isInCheck or rookScope
            inCheckMask = inCheckMask or if (rookScope) visible else 0
            checkCount += if (rookScope) 1 else 0
        }
        //logger.trace("rook directions inCheck=$isInCheck")
        inCheckMask = when (checkCount) {
            0 -> -1L
            1 -> inCheckMask
            else -> 0L
        }
        //logger.traceExit("checkInfo", CheckInfo(isInCheck, inCheckMask))
        CheckInfo(isInCheck, inCheckMask)
    }
}

internal fun yieldCheckMask(
    visibleSquares: (bits: LongArray, directionsIndexes: IntArray, square: Int, wm: Boolean) -> Long
): (Int, Long, Long, Boolean, LongArray) -> Long {
    return { kingSquare: Int, enemies: Long, friends: Long, wm: Boolean, bitboards: LongArray ->
        //logger.traceEntry("checkMask", kingSquare, enemies, friends, wm, bitboards)
        val empty = (enemies or friends).inv()
        val enemyRook: Int
        val enemyQueen: Int
        val enemyBishop: Int
        if (wm) {
            enemyRook = Piece.BR.ordinal
            enemyQueen = Piece.BQ.ordinal
            enemyBishop = Piece.BB.ordinal
        } else {
            enemyRook = Piece.WR.ordinal
            enemyQueen = Piece.WQ.ordinal
            enemyBishop = Piece.WB.ordinal
        }
        var checkMask = 0L
        for (j in 0..3) {
            val visibleEmptyOrFriendsRD: Long = visibleSquares(
                bitboards,
                intArrayOf(rookDirections[j]),
                kingSquare,
                !wm
            )
            val friendsRD = visibleEmptyOrFriendsRD and empty.inv()
            val testBitsRD = bitboards.copyOf()
            for (i in 0..11) {
                testBitsRD[i] = testBitsRD[i] and friendsRD.inv()
            }
            val visibleEmptyOrEnemyRD: Long = visibleSquares(
                testBitsRD,
                intArrayOf(rookDirections[j]), kingSquare,
                wm
            )
            val enemiesThreadsRD = (visibleEmptyOrEnemyRD and (bitboards[enemyRook - 1] or bitboards[enemyQueen - 1]))
            checkMask = checkMask or (if (isPresent(enemiesThreadsRD)) (friendsRD or visibleEmptyOrEnemyRD) else 0L)
        }
        for (j in 0..3) {
            val visibleEmptyOrFriendsBD: Long = visibleSquares(
                bitboards,
                intArrayOf(bishopDirections[j]),
                kingSquare,
                !wm
            )
            val friendsBD = visibleEmptyOrFriendsBD and empty.inv()
            val testBitsBD = bitboards.copyOf()
            for (i in 0..11) {
                testBitsBD[i] = testBitsBD[i] and friendsBD.inv()
            }
            val visibleEmptyOrEnemyBD: Long = visibleSquares(
                testBitsBD,
                intArrayOf(bishopDirections[j]),
                kingSquare,
                wm
            )
            val enemiesThreadsBD = (visibleEmptyOrEnemyBD and (bitboards[enemyBishop - 1] or bitboards[enemyQueen - 1]))
            checkMask = checkMask or (if (isPresent(enemiesThreadsBD)) (friendsBD or visibleEmptyOrEnemyBD) else 0L)
        }
        //logger.traceExit("checkMask",checkMask)
        checkMask
    }
}

internal fun yieldMovesInfo(
    pawnMoves: (
        br: Long, square: Int, pieceType: Int, matrix1: Array<IntArray>, matrix2: Array<IntArray>, kingSquare: Int,
        enemies: Long, friends: Long, ep: Int, wm: Boolean, bitboards: LongArray, checkMask: Long, inCheckMask: Long,
    ) -> PawnMoves,
    knightMoves: (
        br: Long, square: Int, pieceType: Int, enemies: Long, friends: Long, checkMask: Long, inCheckMask: Long
    ) -> RegularPieceMoves,
    bishopMoves: (
        br: Long, square: Int, pieceType: Int, kingSquare: Int, enemies: Long, friends: Long,
        checkMask: Long, inCheckMask: Long
    ) -> RegularPieceMoves,
    rookMoves: (
        br: Long, square: Int, pieceType: Int, kingSquare: Int, enemies: Long,
        friends: Long, checkMask: Long, inCheckMask: Long
    ) -> RegularPieceMoves,
    queenMoves: (
        br: Long, square: Int, pieceType: Int, kingSquare: Int, friends: Long, enemies: Long,
        checkMask: Long, inCheckMask: Long
    ) -> RegularPieceMoves,
    kingMoves: (
        square: Int, pieceType: Int, enemies: Long, friends: Long, inCheck: Boolean, bitboards: LongArray,
        wm: Boolean, wk: Long, wq: Long, bk: Long, bq: Long
    ) -> KingMoves,
    checkMask: (kingSquare: Int, enemies: Long, friends: Long, wm: Boolean, bitboards: LongArray) -> Long,
    checkInfo: (kingPiece: Int, bits: LongArray, wm: Boolean, pawnsDirections: IntArray) -> CheckInfo
): (bitboards: LongArray, wm: Boolean, wk: Boolean, wq: Boolean, bk: Boolean, bq: Boolean, enPassant: Int) -> MovesInfo {
    return { bitboards: LongArray, wm: Boolean, wk: Boolean, wq: Boolean, bk: Boolean, bq: Boolean, enPassant: Int ->
        //logger.traceEntry("movesInfo", bitboards, wm, wk, wq, bk, bq, enPassant)
        val pawnPiece: Int
        val knightPiece: Int
        val bishopPiece: Int
        val rookPiece: Int
        val queenPiece: Int
        val kingPiece: Int
        val aux: Int
        val matrix1: Array<IntArray>
        val matrix2: Array<IntArray>
        val kingSquare: Int
        val pawnsDirections: IntArray
        val friends: Long
        val enemies: Long
        if (wm) {
            pawnPiece = Piece.WP.ordinal
            knightPiece = Piece.WN.ordinal
            bishopPiece = Piece.WB.ordinal
            rookPiece = Piece.WR.ordinal
            queenPiece = Piece.WQ.ordinal
            kingPiece = Piece.WK.ordinal
            aux = 6
            matrix1 = pawnMatrix1[1]
            matrix2 = pawnMatrix2[1]
            kingSquare = bitboards[kingPiece - 1].countTrailingZeroBits()
            pawnsDirections = whitePawnMatrix2[kingSquare]
            friends = (bitboards[Piece.WP.ordinal - 1] or bitboards[Piece.WN.ordinal - 1]
                    or bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WR.ordinal - 1]
                    or bitboards[Piece.WQ.ordinal - 1] or bitboards[Piece.WK.ordinal - 1])
            enemies = (bitboards[Piece.BP.ordinal - 1] or bitboards[Piece.BN.ordinal - 1]
                    or bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BR.ordinal - 1]
                    or bitboards[Piece.BQ.ordinal - 1] or bitboards[Piece.BK.ordinal - 1])
        } else {
            pawnPiece = Piece.BP.ordinal
            knightPiece = Piece.BN.ordinal
            bishopPiece = Piece.BB.ordinal
            rookPiece = Piece.BR.ordinal
            queenPiece = Piece.BQ.ordinal
            kingPiece = Piece.BK.ordinal
            aux = 0
            matrix1 = pawnMatrix1[0]
            matrix2 = pawnMatrix2[0]
            kingSquare = bitboards[kingPiece - 1].countTrailingZeroBits()
            pawnsDirections = blackPawnMatrix2[kingSquare]
            friends = (bitboards[Piece.BP.ordinal - 1] or bitboards[Piece.BN.ordinal - 1]
                    or bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BR.ordinal - 1]
                    or bitboards[Piece.BQ.ordinal - 1] or bitboards[Piece.BK.ordinal - 1])
            enemies = (bitboards[Piece.WP.ordinal - 1] or bitboards[Piece.WN.ordinal - 1]
                    or bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WR.ordinal - 1]
                    or bitboards[Piece.WQ.ordinal - 1] or bitboards[Piece.WK.ordinal - 1])
        }

        val (inCheck, inCheckMask) = checkInfo(
            kingPiece,
            bitboards,
            wm,
            pawnsDirections
        )
        //logger.trace("inCheck=$inCheck")
        //logger.trace("inCheckMask=${Bitboard(inCheckMask).toSquares()}")
        val checkMask = checkMask(
            kingSquare, enemies, friends, wm, bitboards
        )
        //logger.trace("checkMask=${Bitboard(checkMask).toSquares()}")
        // Pawn Moves
        val pawnMoves = bitboardToList(bitboards[pawnPiece - 1]) {
            pawnMoves(
                it, it.countTrailingZeroBits(), pawnPiece,
                matrix1, matrix2, kingSquare, enemies,
                friends, enPassant, wm, bitboards, checkMask,
                inCheckMask
            )
        }
        // Knight Moves
        val knightMoves = bitboardToList(bitboards[knightPiece - 1]) {
            knightMoves(
                it, it.countTrailingZeroBits(), knightPiece, enemies,
                friends, checkMask, inCheckMask
            )
        }
        // Bishop Moves
        val bishopMoves = bitboardToList(bitboards[bishopPiece - 1]) {
            bishopMoves(
                it, it.countTrailingZeroBits(), bishopPiece, kingSquare,
                enemies, friends, checkMask, inCheckMask
            )
        }
        // Rook Moves
        val rookMoves = bitboardToList(bitboards[rookPiece - 1]) {
            rookMoves(
                it, it.countTrailingZeroBits(), rookPiece, kingSquare,
                enemies, friends, checkMask, inCheckMask
            )
        }
        // Queen Moves
        val queenMoves = bitboardToList(bitboards[queenPiece - 1]) {
            queenMoves(
                it, it.countTrailingZeroBits(), queenPiece, kingSquare,
                friends, enemies, checkMask, inCheckMask
            )
        }
        // King Moves
        val kingMoves = kingMoves(
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
        MovesInfo(pawnMoves, knightMoves, bishopMoves, rookMoves, queenMoves, kingMoves)
    }
}