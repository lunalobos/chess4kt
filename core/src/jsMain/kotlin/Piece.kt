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
package io.github.lunalobos.chess4kt.js

/**
 * This class represents the chess pieces on the board. Each piece has a corresponding side (color).
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Piece private constructor(val ordinal: Int, val name: String) {
    companion object {
        val entries = io.github.lunalobos.chess4kt.Piece.entries.map { Piece(it.ordinal, it.name) }

        /**
         * Represents an empty square on the board.
         */
        val EMPTY = entries[0]

        /**
         * White Pawn
         */
        val WP = entries[1]

        /**
         * White Knight
         */
        val WN = entries[2]

        /**
         * White Bishop
         */
        val WB = entries[3]

        /**
         * White Rook
         */
        val WR = entries[4]

        /**
         * White Queen
         */
        val WQ = entries[5]

        /**
         * White King
         */
        val WK = entries[6]

        /**
         * Black Pawn
         */
        val BP = entries[7]

        /**
         * Black Knight
         */
        val BN = entries[8]

        /**
         * Black Bishop
         */
        val BB = entries[9]

        /**
         * Black Rook
         */
        val BR = entries[10]

        /**
         * Black Queen
         */
        val BQ = entries[11]

        /**
         * Black King
         */
        val BK = entries[12]
        private val map =
            io.github.lunalobos.chess4kt.Square.entries.associate { it.name to Piece(it.ordinal, it.name) }

        fun get(name: String) = map[name]
        fun indexToPiece(index: Int) = entries[index]
    }
}