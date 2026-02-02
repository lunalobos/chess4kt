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

internal fun generateChildren(
    mi: MovesInfo, pos: Position
): List<Tuple<Position, Move>> {
    val children = mutableListOf<Tuple<Position, Move>>()
    pawnTuples(mi, pos, children)
    knightBishopAndQueenTuples(mi, pos, children)
    rookTuples(mi, pos, children)
    kingTuples(mi, pos, children)
    return children
}

internal fun pawnTuples(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Tuple<Position, Move>>
) {
    mi.pawnMoves.forEach {
        generateTuples(
            it.regularMovesList, it.pawnPiece, it.originSquare,
            it.enemies, position, children, { -1 }) { castleInfo ->
            { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm) } }
        }
        generatePromotionTuples(
            it.promotionMovesList, it.pawnPiece, it.originSquare,
            position, children
        )
        generateTuples(
            it.advanceEpMovesList, it.pawnPiece, it.originSquare,
            it.enemies, position, children, { it.countTrailingZeroBits() }) { castleInfo ->
            { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm) } }
        }
        it.epCaptureMove?.let { move ->
            generateEnPassantCaptureTuple(move, it.pawnPiece, it.originSquare, position, children)
        }
    }
}

internal fun knightBishopAndQueenTuples(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Tuple<Position, Move>>
) {
    mi.knightMoves.forEach {
        generateTuples(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, { -1 }) { castleInfo ->
            { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm) } }
        }
    }
    mi.bishopMoves.forEach {
        generateTuples(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, { -1 }) { castleInfo ->
            { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm) } }
        }
    }
    mi.queenMoves.forEach {
        generateTuples(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, { -1 }) { castleInfo ->
            { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm) } }
        }
    }

}

internal fun rookTuples(
    mi: MovesInfo, position: Position, children: MutableList<Tuple<Position, Move>>
) {
    mi.rookMoves.forEach {
        generateTuples(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, { -1 }) { castleInfo ->
            { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm).applyCastleRules(bitboards, !wm) } }
        }
    }
}

internal fun kingTuples(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Tuple<Position, Move>>
) {
    generateTuples(
        mi.kingMoves.regularMovesList,
        mi.kingMoves.kingPiece,
        mi.kingMoves.originSquare,
        mi.kingMoves.enemies,
        position,
        children,
        { -1 }
    ) { castleInfo ->
        { bitboards -> { wm -> castleInfo.applyCastleRules(bitboards, wm).applyCastleRules(bitboards, !wm) } }
    }
    generateCastleTuples(
        mi.kingMoves.castleMovesList,
        mi.kingMoves.kingPiece,
        mi.kingMoves.originSquare,
        position,
        children
    )
}

internal fun generateTuples(
    moves: List<Move>,
    pieceType: Int,
    square: Int,
    enemies: Long,
    position: Position,
    children: MutableList<Tuple<Position, Move>>,
    epFunction: (Long) -> Int,
    castleFunction: (CastleInfo) -> (LongArray) -> (Boolean) -> CastleInfo
) {
    moves.forEach { m: Move ->
        val move: Long = m.move
        // pieces
        val bitboards = position.bitboards
        for (index in 0..11) {
            bitboards[index] = bitboards[index] and (move.inv())
        }
        bitboards[pieceType - 1] = (bitboards[pieceType - 1] and ((1L shl square).inv())) or move
        // color
        val wm = !position.whiteMove
        // castle
        val castleInfo = castleFunction(
            CastleInfo(
                position.whiteCastleKingside,
                position.whiteCastleQueenside,
                position.blackCastleKingside,
                position.blackCastleQueenside
            )
        )(bitboards)(wm)
        val (wk, wq, bk, bq) = castleInfo
        // moves counter
        val mc = position.movesCounter + (if (wm) 1 else 0)
        // en passant
        val ep = epFunction(move)
        // half moves counter
        val aux = if (wm) 6 else 0
        val isCapture = isPresent(enemies and move)
        val isPawnMove = isPresent(move and bitboards[Piece.BP.ordinal - aux - 1])
        val hm = if (isCapture || isPawnMove) 0 else (position.halfMovesCounter + 1)
        // add new tuple
        val tuple = tupleOf(Position(bitboards, wm, ep, wk, wq, bk, bq, mc, hm), m)
        children.add(tuple)
    }
}

internal fun generatePromotionTuples(
    moves: List<Move>,
    pieceType: Int,
    square: Int,
    position: Position,
    children: MutableList<Tuple<Position, Move>>
) {
    moves.forEach { m ->
        val move = m.move
        val promotionPiece = m.promotionPiece
        // bitboards
        val bitboards = position.bitboards
        for (index in 0..11) {
            bitboards[index] = bitboards[index] and (move.inv())
        }
        bitboards[pieceType - 1] = (bitboards[pieceType - 1] and ((1L shl square).inv()))
        bitboards[promotionPiece - 1] = bitboards[promotionPiece - 1] or move
        // color
        val wm = !position.whiteMove
        // castle
        val scMask: LongArray
        val lcMask: LongArray
        val scSquares: IntArray
        val lcSquares: IntArray
        val rookBits: Long
        val kingBits: Long
        val scBitsMasked: Long
        val lcBitsMasked: Long
        val wk: Boolean
        val wq: Boolean
        val bk: Boolean
        val bq: Boolean
        val mc: Int
        if (wm) {
            scMask = castleMask[1][0]
            lcMask = castleMask[1][1]
            scSquares = castleSquares[1][0]
            lcSquares = castleSquares[1][1]
            rookBits = bitboards[Piece.WR.ordinal - 1]
            kingBits = bitboards[Piece.WK.ordinal - 1]
            scBitsMasked = ((rookBits and scMask[1]) ushr scSquares[1]) and ((kingBits and scMask[0]) ushr scSquares[0])
            lcBitsMasked = ((rookBits and lcMask[1]) ushr lcSquares[1]) and ((kingBits and lcMask[0]) ushr lcSquares[0])
            wk = isPresent(scBitsMasked) && position.whiteCastleKingside
            wq = isPresent(lcBitsMasked) && position.whiteCastleQueenside
            bk = position.blackCastleKingside
            bq = position.blackCastleQueenside
            mc = position.movesCounter + 1
        } else {
            scMask = castleMask[0][0]
            lcMask = castleMask[0][1]
            scSquares = castleSquares[0][0]
            lcSquares = castleSquares[0][1]
            rookBits = bitboards[Piece.BR.ordinal - 1]
            kingBits = bitboards[Piece.BK.ordinal - 1]
            scBitsMasked = ((rookBits and scMask[1]) ushr scSquares[1]) and ((kingBits and scMask[0]) ushr scSquares[0])
            lcBitsMasked = ((rookBits and lcMask[1]) ushr lcSquares[1]) and ((kingBits and lcMask[0]) ushr lcSquares[0])
            wk = position.whiteCastleKingside
            wq = position.whiteCastleQueenside
            bk = isPresent(scBitsMasked) && position.blackCastleKingside
            bq = isPresent(lcBitsMasked) && position.blackCastleQueenside
            mc = position.movesCounter + 1
        }
        // half moves counter
        val hm = 0
        // en passant
        val ep = -1
        // add new tuple
        val newPosition = Position(bitboards, wm, ep, wk, wq, bk, bq, mc, hm)
        children.add(tupleOf(newPosition, m))
    }
}

internal fun generateEnPassantCaptureTuple(
    m: Move,
    pieceType: Int,
    originSquare: Int,
    position: Position,
    children: MutableList<Tuple<Position, Move>>
) {
    val move = m.move
    val whiteMove = position.whiteMove
    val capture = 1L shl (move.countTrailingZeroBits() + if (whiteMove) -8 else 8)

    // bitboards
    val bitboards = position.bitboards
    for (index in 0..11) {
        bitboards[index] = bitboards[index] and (capture.inv())
    }
    bitboards[pieceType - 1] = (bitboards[pieceType - 1] and ((1L shl originSquare).inv())) or move
    // color
    val wm = !whiteMove
    // half moves counter
    val hm = 0
    // moves counter
    val mc = (position.movesCounter + if (wm) 1 else 0)
    // en passant
    val ep = -1
    // add new immutable instance
    val newPosition = Position(
        bitboards,
        wm,
        ep,
        position.whiteCastleKingside,
        position.whiteCastleQueenside,
        position.blackCastleKingside,
        position.blackCastleQueenside,
        mc,
        hm
    )
    children.add(Tuple(newPosition, m))
}

internal fun generateCastleTuples(
    moves: List<Move>,
    kingPiece: Int,
    square: Int,
    position: Position,
    children: MutableList<Tuple<Position, Move>>
) {
    moves.forEach { m ->
        val move: Long = m.move
        // bitboards
        val bitboards = position.bitboards
        for (index in indexes) {
            bitboards[index] = bitboards[index] and (move.inv())
        }
        bitboards[kingPiece - 1] = (bitboards[kingPiece - 1] and ((1L shl square).inv())) or move
        var rookMove = 0L
        rookMove = rookMove or (((1L shl 6) and (move)) shr 1)
        rookMove = rookMove or (((1L shl 2) and (move)) shl 1)
        rookMove = rookMove or (((1L shl 62) and (move)) shr 1)
        rookMove = rookMove or (((1L shl 58) and (move)) shl 1)
        var rookOrigin = 0L
        rookOrigin = rookOrigin or (((1L shl 6) and (move)) shl 1)
        rookOrigin = rookOrigin or (((1L shl 2) and (move)) shr 2)
        rookOrigin = rookOrigin or (((1L shl 62) and (move)) shl 1)
        rookOrigin = rookOrigin or (((1L shl 58) and (move)) shr 2)
        val rookType = kingPiece - 2
        for (i in 0..<bitboards.size) {
            bitboards[i] = bitboards[i] and (rookMove.inv())
        }
        bitboards[rookType - 1] = (bitboards[rookType - 1] and (rookOrigin.inv())) or rookMove
        // color
        val wm = !position.whiteMove
        // castle
        val castleInfo = CastleInfo(
            position.whiteCastleKingside,
            position.whiteCastleQueenside,
            position.blackCastleKingside,
            position.blackCastleQueenside
        ).applyCastleRules(bitboards, position.whiteMove)
        val (wk, wq, bk, bq) = castleInfo
        // half moves counter
        val hm = position.halfMovesCounter + 1
        // moves counter
        val mc = position.movesCounter + if (wm) 1 else 0
        // en passant
        val ep = -1
        // add new tuple
        val newPosition = Position(
            bitboards,
            wm,
            ep,
            wk,
            wq,
            bk,
            bq,
            mc,
            hm)
        children.add(tupleOf(newPosition, m))
    }
}