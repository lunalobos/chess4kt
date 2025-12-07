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

internal class MovesInfo(
    val pawnMoves: List<PawnMoves>,
    val knightMoves: List<RegularPieceMoves>,
    val bishopMoves: List<RegularPieceMoves>,
    val rookMoves: List<RegularPieceMoves>,
    val queenMoves: List<RegularPieceMoves>,
    val kingMoves: KingMoves) {
    val moves: Long by lazy {
        val pm = pawnMoves.asSequence().map{it.allMovesBitboard}.fold(0L){acc, lng -> acc or lng}
        val nm = knightMoves.asSequence().map{it.moves}.fold(0L){acc, lng -> acc or lng}
        val bm = bishopMoves.asSequence().map{it.moves}.fold(0L){acc, lng -> acc or lng}
        val rm = rookMoves.asSequence().map{it.moves}.fold(0L){acc, lng -> acc or lng}
        val qm = queenMoves.asSequence().map{it.moves}.fold(0L){acc, lng -> acc or lng}
        val km = kingMoves.allMovesBitboard
        pm or nm or bm or rm or qm or km
    }
    val movesList: List<Move> by lazy {
        sequence {
            yieldAll(pawnMoves.asSequence().flatMap{ it.allMovesList.asSequence() })
            yieldAll(knightMoves.asSequence().flatMap { it.allMovesList.asSequence() })
            yieldAll(bishopMoves.asSequence().flatMap { it.allMovesList.asSequence() })
            yieldAll( rookMoves.asSequence().flatMap { it.allMovesList.asSequence() })
            yieldAll( queenMoves.asSequence().flatMap { it.allMovesList.asSequence() })
            yieldAll( kingMoves.allMovesList.asSequence())
        }.toList()
    }

    override fun toString(): String{
        return "MovesInfo(moves = $movesList)"
    }
}