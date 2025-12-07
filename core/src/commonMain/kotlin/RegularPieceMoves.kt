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

internal class RegularPieceMoves(
    val piece: Int, val square: Int, val enemies: Long, val moves: Long,
    private val moveFunction: (origin: Int, target: Int) -> Move
) {
    val allMovesList: List<Move> by lazy {
        bitboardToList(moves) {
            moveFunction(square, it.countTrailingZeroBits())
        }
    }

    override fun toString(): String {
        return "RegularPieceMoves(piece=${Piece.entries[piece]}, origin=${Square.entries[square]}, enemies=${
            Bitboard(
                enemies
            ).toSquares()
        }, moves=$allMovesList)"
    }
}