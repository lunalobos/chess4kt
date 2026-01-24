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

private val logger = getLogger("moveFactory")

private fun checkCollision(move: Move, moves: Array<Move?>) {
    val m = moves[move.hashCode()]
    if (m != null && m != move) {
        throw logger.fatal(RuntimeException("hash collision for $m and $move"))
    }
}

private fun hash(origin: Int, target: Int): Int {
    return origin or (target shl 6)
}

private fun hash(origin: Int, target: Int, promotionPiece: Int): Int {
    return origin or (target shl 6) or (promotionPiece shl 12)
}


internal fun yieldMoves(): Array<Move?> {
    val moves = arrayOfNulls<Move?>(65535)
    val moveList = mutableListOf<Move>()
    val cols = arrayOf("a", "b", "c", "d", "e", "f", "g", "h")
    val white = arrayOf(WQ, WR, WN, WB)

    cols
        .map {it.uppercase()}
        .flatMap { col ->
            white
                .map {
                    Move(
                        Bitboard.fromSquares(Square.valueOf("${col}8")).value,
                        Square.valueOf("${col}7").ordinal,
                        it.ordinal
                    )
                }
        }.forEach {
            moveList.add(it)
        }

    cols.copyOfRange(0, 7)
        .map {it.uppercase()}
        .flatMap { col ->
            white
                .map {
                    Move(
                        Bitboard.fromSquares(
                            Square.get(Square.valueOf("${col}8").ordinal + 1)
                        ).value,
                        Square.valueOf("${col}7").ordinal,
                        it.ordinal
                    )
                }
        }.forEach {
            moveList.add(it)
        }

    cols.copyOfRange(1, 8)
        .map {it.uppercase()}
        .flatMap { col ->
            white
                .map {
                    Move(
                        Bitboard.fromSquares(
                            Square.get(Square.valueOf("${col}8").ordinal - 1)
                        ).value,
                        Square.valueOf("${col}7").ordinal,
                        it.ordinal
                    )
                }
        }.forEach {
            moveList.add(it)
        }

    val black = arrayOf(BQ, BR, BN, BB)

    cols
        .map {it.uppercase()}
        .flatMap { col ->
            black
                .map {
                    Move(
                        Bitboard.fromSquares(Square.valueOf("${col}1")).value,
                        Square.valueOf("${col}2").ordinal,
                        it.ordinal
                    )
                }
        }.forEach {
            moveList.add(it)
        }

    cols.copyOfRange(0, 7)
        .map {it.uppercase()}
        .flatMap { col ->
            black
                .map {
                    Move(
                        Bitboard.fromSquares(
                            Square.get(Square.valueOf("${col}1").ordinal + 1)
                        ).value,
                        Square.valueOf("${col}2").ordinal,
                        it.ordinal
                    )
                }
        }.forEach {
            moveList.add(it)
        }

    cols.copyOfRange(1, 8)
        .map {it.uppercase()}
        .flatMap { col ->
            black
                .map {
                    Move(
                        Bitboard.fromSquares(
                            Square.get(Square.valueOf("${col}1").ordinal - 1)
                        ).value,
                        Square.valueOf("${col}2").ordinal,
                        it.ordinal
                    )
                }
        }.forEach {
            moveList.add(it)
        }


    // regular moves, some are not possible
    for (i in 0..63) {
        for (j in 0..63) {
            if (j != i) {
                moveList.add(
                    Move(
                        Bitboard.fromSquares(Square.get(i)).value,
                        Square.get(j).ordinal
                    )
                )
            }
        }
    }

    // create a perfect hashMap
    moveList.forEach {
        checkCollision(it, moves)
        moves[it.hashCode()] = it
    }
    return moves
}

internal fun yieldMoveFromOriginTarget(moves: Array<Move?>): (origin: Int, target: Int) -> Move{
    return {origin: Int, target: Int ->
        moves[hash(origin, target)]
            ?: throw logger.fatal(MoveException("move for origin=$origin, target=$target not found"))
    }
}

internal fun yieldMoveFromOriginTargetPromotion(moves: Array<Move?>): (origin: Int, target: Int, promotionPiece: Int) -> Move{
    return { origin: Int, target: Int, promotionPiece: Int ->
        if(promotionPiece == -1){
            moves[hash(origin, target)]
                ?: throw logger.fatal(MoveException("move for origin=$origin, target=$target not found"))
        } else {
            moves[hash(origin, target, promotionPiece)]
                ?: throw logger.fatal(MoveException("move for origin=$origin, target=$target not found, promotionPiece=$promotionPiece"))
        }
    }
}

internal fun yieldMoveFromOriginTargetObjects(moves: Array<Move?>): (origin: Square, target: Square) -> Move{
    return {
        origin: Square, target: Square ->
        moves[hash(origin.ordinal, target.ordinal)]
            ?: throw logger.fatal(MoveException("move for origin=$origin, target=$target not found"))
    }
}

internal fun yieldMoveFromOriginTargetPromotionObjects(moves: Array<Move?>):
            (origin: Square, target: Square, promotionPiece: Piece) -> Move{
    return { origin: Square, target: Square, promotionPiece: Piece ->
        moves[hash(origin.ordinal, target.ordinal, promotionPiece.ordinal)]
            ?: throw logger.fatal(MoveException(
                "move for origin=$origin, target=$target, promotion=$promotionPiece not found"))
    }
}

internal fun yieldMoveFromString(
    map: Map<String, Move>
): (move: String) -> Move{
    return { move: String ->
        map[move] ?: throw MoveException("$move is not a valid move string")
    }
}