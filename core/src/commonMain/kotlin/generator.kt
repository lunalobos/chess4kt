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

internal fun generateChildrenWhite(mi: MovesInfo, pos: Position): List<Pair<Position, Move>>{
    val children = linkedListOf<Pair<Position, Move>>()
    pawnTuplesWhite(mi, pos, children)
    knightBishopAndQueenTuplesWhite(mi, pos, children)
    rookTuplesWhite(mi, pos, children)
    kingTuplesWhite(mi, pos, children)
    return children
}

internal fun generateChildrenBlack(mi: MovesInfo, pos: Position): List<Pair<Position, Move>>{
    val children = linkedListOf<Pair<Position, Move>>()
    pawnTuplesBlack(mi, pos, children)
    knightBishopAndQueenTuplesBlack(mi, pos, children)
    rookTuplesBlack(mi, pos, children)
    kingTuplesBlack(mi, pos, children)
    return children
}

internal fun pawnTuplesWhite(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Pair<Position, Move>>
) {
    mi.pawnMoves.forEach {
        generateTuplesWhite(
            it.regularMovesList, it.pawnPiece, it.originSquare,
            it.enemies, position, children, true, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesWhite(bitboards) }
        }
        generatePromotionTuplesWhite(
            it.promotionMovesList, it.pawnPiece, it.originSquare,
            position, children
        )
        generateTuplesWhite(
            it.advanceEpMovesList, it.pawnPiece, it.originSquare,
            it.enemies, position, children, true, { move -> move.countTrailingZeroBits() }) { castleInfo ->
            { bitboards ->  castleInfo.applyCastleRulesWhite(bitboards) }
        }
        it.epCaptureMove?.let { move ->
            generateEnPassantCaptureTuple(move, it.pawnPiece, it.originSquare, position, children)
        }
    }
}

internal fun pawnTuplesBlack(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Pair<Position, Move>>
) {
    mi.pawnMoves.forEach {
        generateTuplesBlack(
            it.regularMovesList, it.pawnPiece, it.originSquare,
            it.enemies, position, children, true, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesBlack(bitboards) }
        }
        generatePromotionTuplesBlack(
            it.promotionMovesList, it.pawnPiece, it.originSquare,
            position, children
        )
        generateTuplesBlack(
            it.advanceEpMovesList, it.pawnPiece, it.originSquare,
            it.enemies, position, children, true, { move -> move.countTrailingZeroBits() }) { castleInfo ->
            { bitboards ->  castleInfo.applyCastleRulesBlack(bitboards) }
        }
        it.epCaptureMove?.let { move ->
            generateEnPassantCaptureTuple(move, it.pawnPiece, it.originSquare, position, children)
        }
    }
}

internal fun knightBishopAndQueenTuplesWhite(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Pair<Position, Move>>
) {
    mi.knightMoves.forEach {
        generateTuplesWhite(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesWhite(bitboards)  }
        }
    }
    mi.bishopMoves.forEach {
        generateTuplesWhite(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false,{ -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesWhite(bitboards) }
        }
    }
    mi.queenMoves.forEach {
        generateTuplesWhite(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesWhite(bitboards)  }
        }
    }
}

internal fun knightBishopAndQueenTuplesBlack(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Pair<Position, Move>>
) {
    mi.knightMoves.forEach {
        generateTuplesBlack(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesBlack(bitboards)  }
        }
    }
    mi.bishopMoves.forEach {
        generateTuplesBlack(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false,{ -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesBlack(bitboards) }
        }
    }
    mi.queenMoves.forEach {
        generateTuplesBlack(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesBlack(bitboards)  }
        }
    }
}

internal fun rookTuplesWhite(
    mi: MovesInfo, position: Position, children: MutableList<Pair<Position, Move>>
) {
    mi.rookMoves.forEach {
        generateTuplesWhite(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesWhite(bitboards).applyCastleRulesBlack(bitboards) }
        }
    }
}

internal fun rookTuplesBlack(
    mi: MovesInfo, position: Position, children: MutableList<Pair<Position, Move>>
) {
    mi.rookMoves.forEach {
        generateTuplesBlack(
            it.allMovesList, it.piece, it.square, it.enemies,
            position, children, false, { -1 }) { castleInfo ->
            { bitboards -> castleInfo.applyCastleRulesBlack(bitboards).applyCastleRulesWhite(bitboards) }
        }
    }
}

internal fun kingTuplesWhite(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Pair<Position, Move>>
) {
    generateTuplesWhite(
        mi.kingMoves.regularMovesList,
        mi.kingMoves.kingPiece,
        mi.kingMoves.originSquare,
        mi.kingMoves.enemies,
        position,
        children,
        false,
        { -1 }
    ) { castleInfo ->
        { bitboards -> castleInfo.applyCastleRulesWhite(bitboards).applyCastleRulesBlack(bitboards) }
    }
    generateCastleTuples(
        mi.kingMoves.castleMovesList,
        mi.kingMoves.kingPiece,
        mi.kingMoves.originSquare,
        position,
        children
    )
}

internal fun kingTuplesBlack(
    mi: MovesInfo,
    position: Position,
    children: MutableList<Pair<Position, Move>>
) {
    generateTuplesBlack(
        mi.kingMoves.regularMovesList,
        mi.kingMoves.kingPiece,
        mi.kingMoves.originSquare,
        mi.kingMoves.enemies,
        position,
        children,
        false,
        { -1 }
    ) { castleInfo ->
        { bitboards -> castleInfo.applyCastleRulesBlack(bitboards).applyCastleRulesWhite(bitboards) }
    }
    generateCastleTuples(
        mi.kingMoves.castleMovesList,
        mi.kingMoves.kingPiece,
        mi.kingMoves.originSquare,
        position,
        children
    )
}

internal inline fun generateTuplesWhite(
    moves: List<Move>,
    pieceType: Int,
    square: Int,
    enemies: Long,
    position: Position,
    children: MutableList<Pair<Position, Move>>,
    isPawnMove: Boolean,
    epFunction: (Long) -> Int,
    castleFunction: (CastleInfo) -> (LongArray) -> CastleInfo,
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
        )(bitboards)
        val (wk, wq, bk, bq) = castleInfo
        // moves counter
        val mc = position.movesCounter + 1
        // en passant
        val ep = epFunction(move)
        // half moves counter
        val isCapture = isPresent(enemies and move)
        //val isPawnMove = isPresent(move and bitboards[Piece.BP.ordinal - aux - 1])
        val hm = if (isCapture || isPawnMove) 0 else (position.halfMovesCounter + 1)
        // add new tuple
        val tuple = Pair(WhitePosition(bitboards, ep, wk, wq, bk, bq, mc, hm) as Position, m)
        children.add(tuple)
    }
}

internal inline fun generateTuplesBlack(
    moves: List<Move>,
    pieceType: Int,
    square: Int,
    enemies: Long,
    position: Position,
    children: MutableList<Pair<Position, Move>>,
    isPawnMove: Boolean,
    epFunction: (Long) -> Int,
    castleFunction: (CastleInfo) -> (LongArray) -> CastleInfo,
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
        )(bitboards)
        val (wk, wq, bk, bq) = castleInfo
        // moves counter
        val mc = position.movesCounter
        // en passant
        val ep = epFunction(move)
        // half moves counter
        val isCapture = isPresent(enemies and move)
        //val isPawnMove = isPresent(move and bitboards[Piece.BP.ordinal - aux - 1])
        val hm = if (isCapture || isPawnMove) 0 else (position.halfMovesCounter + 1)
        // add new tuple
        val tuple = Pair(BlackPosition(bitboards, ep, wk, wq, bk, bq, mc, hm) as Position, m)
        children.add(tuple)
    }
}

private fun positionOf(
    bitboards: LongArray,
    whiteMove: Boolean,
    enPassant: Int,
    whiteCastleKingside: Boolean,
    whiteCastleQueenside: Boolean,
    blackCastleKingside: Boolean,
    blackCastleQueenside: Boolean,
    movesCounter: Int,
    halfMovesCounter: Int
): Position {
    if(whiteMove){
        return WhitePosition(
            bitboards = bitboards,
            enPassant = enPassant,
            whiteCastleKingside = whiteCastleKingside,
            whiteCastleQueenside = whiteCastleQueenside,
            blackCastleKingside = blackCastleKingside,
            blackCastleQueenside = blackCastleQueenside,
            mc = movesCounter,
            hm = halfMovesCounter
        )
    } else {
        return BlackPosition(
            bitboards = bitboards,
            enPassant = enPassant,
            whiteCastleKingside = whiteCastleKingside,
            whiteCastleQueenside = whiteCastleQueenside,
            blackCastleKingside = blackCastleKingside,
            blackCastleQueenside = blackCastleQueenside,
            mc = movesCounter,
            hm = halfMovesCounter
        )
    }
}

internal fun generatePromotionTuplesWhite(
    moves: List<Move>,
    pieceType: Int,
    square: Int,
    position: Position,
    children: MutableList<Pair<Position, Move>>
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

        // castle
        val scMask = castleMask[1][0]
        val lcMask = castleMask[1][1]
        val scSquares = castleSquares[1][0]
        val lcSquares = castleSquares[1][1]
        val rookBits = bitboards[Piece.WR.ordinal - 1]
        val kingBits = bitboards[Piece.WK.ordinal - 1]
        val scBitsMasked = ((rookBits and scMask[1]) ushr scSquares[1]) and ((kingBits and scMask[0]) ushr scSquares[0])
        val lcBitsMasked = ((rookBits and lcMask[1]) ushr lcSquares[1]) and ((kingBits and lcMask[0]) ushr lcSquares[0])
        val wk = isPresent(scBitsMasked) && position.whiteCastleKingside
        val wq = isPresent(lcBitsMasked) && position.whiteCastleQueenside
        val bk = position.blackCastleKingside
        val bq = position.blackCastleQueenside
        val mc = position.movesCounter + 1

        // half moves counter
        val hm = 0
        // en passant
        val ep = -1
        // add new tuple
        val newPosition = WhitePosition(bitboards, ep, wk, wq, bk, bq, mc, hm)
        children.add(Pair(newPosition, m))
    }
}

internal fun generatePromotionTuplesBlack(
    moves: List<Move>,
    pieceType: Int,
    square: Int,
    position: Position,
    children: MutableList<Pair<Position, Move>>
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

        // castle
        val scMask = castleMask[0][0]
        val lcMask = castleMask[0][1]
        val scSquares = castleSquares[0][0]
        val lcSquares = castleSquares[0][1]
        val rookBits = bitboards[Piece.BR.ordinal - 1]
        val kingBits = bitboards[Piece.BK.ordinal - 1]
        val scBitsMasked = ((rookBits and scMask[1]) ushr scSquares[1]) and ((kingBits and scMask[0]) ushr scSquares[0])
        val lcBitsMasked = ((rookBits and lcMask[1]) ushr lcSquares[1]) and ((kingBits and lcMask[0]) ushr lcSquares[0])
        val wk = position.whiteCastleKingside
        val wq = position.whiteCastleQueenside
        val bk = isPresent(scBitsMasked) && position.blackCastleKingside
        val bq = isPresent(lcBitsMasked) && position.blackCastleQueenside
        val mc = position.movesCounter + 1

        // half moves counter
        val hm = 0
        // en passant
        val ep = -1
        // add new tuple
        val newPosition = BlackPosition(bitboards, ep, wk, wq, bk, bq, mc, hm)
        children.add(Pair(newPosition, m))
    }
}

internal fun generateEnPassantCaptureTuple(
    m: Move,
    pieceType: Int,
    originSquare: Int,
    position: Position,
    children: MutableList<Pair<Position, Move>>
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
    val newPosition = positionOf(
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
    children.add(Pair(newPosition, m))
}

internal fun generateCastleTuples(
    moves: List<Move>,
    kingPiece: Int,
    square: Int,
    position: Position,
    children: MutableList<Pair<Position, Move>>
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
        val newPosition = positionOf(
            bitboards,
            wm,
            ep,
            wk,
            wq,
            bk,
            bq,
            mc,
            hm)
        children.add(Pair(newPosition, m))
    }
}