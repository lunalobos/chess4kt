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

import io.github.lunalobos.chess4kt.Side.*

/**
 * This class represents the chess pieces on the board. Each piece has a corresponding side (color).
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
enum class Piece(
    /**
     * The side (color) of the piece. This is null for [EMPTY].
     */
    val side: Side?
) {
    /**
     * Represents an empty square on the board.
     */
    EMPTY(null),

    /**
     * White Pawn
     */
    WP(WHITE),

    /**
     * White Knight
     */
    WN(WHITE),

    /**
     * White Bishop
     */
    WB(WHITE),

    /**
     * White Rook
     */
    WR(WHITE),

    /**
     * White Queen
     */
    WQ(WHITE),

    /**
     * White King
     */
    WK(WHITE),

    /**
     * Black Pawn
     */
    BP(Side.BLACK),

    /**
     * Black Knight
     */
    BN(BLACK),

    /**
     * Black Bishop
     */
    BB(BLACK),

    /**
     * Black Rook
     */
    BR(BLACK),

    /**
     * Black Queen
     */
    BQ(BLACK),

    /**
     * Black King
     */
    BK(BLACK);

    companion object {
        /**
         * Returns the [Piece] enum constant corresponding to the given zero-based index.
         * The index order is typically 0 to 12, starting with [EMPTY].
         *
         * @param index The zero-based index of the piece.
         * @return The corresponding [Piece] enum constant.
         * @throws IndexOutOfBoundsException if the index is outside the valid range.
         *
         * @since 1.0.0-beta.1
         */
        fun get(index: Int): Piece {
            return entries[index]
        }
    }
}