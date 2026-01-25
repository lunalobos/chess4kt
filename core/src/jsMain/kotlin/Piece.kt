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

import io.github.lunalobos.chess4kt.js.Piece.Companion.entries_

/**
 * This class represents the chess pieces on the board. Each piece has a corresponding side (color).
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Piece private constructor(val ordinal: Int, val name: String) {
    companion object {
        @OptIn(ExperimentalJsCollectionsApi::class)
        val entries = io.github.lunalobos.chess4kt.Piece.entries.map { Piece(it.ordinal, it.name) }.asJsReadonlyArrayView()

        internal val entries_ = io.github.lunalobos.chess4kt.Piece.entries.map { Piece(it.ordinal, it.name) }


        private val map =
            io.github.lunalobos.chess4kt.Square.entries.associate { it.name to Piece(it.ordinal, it.name) }

        fun get(name: String) = map[name]
        fun indexToPiece(index: Int) = entries_[index]
    }
}

/**
 * Represents an empty square on the board.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val EMPTY = entries_[0]

/**
 * White Pawn
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val WP = entries_[1]

/**
 * White Knight
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val WN = entries_[2]

/**
 * White Bishop
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val WB = entries_[3]

/**
 * White Rook
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val WR = entries_[4]

/**
 * White Queen
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val WQ = entries_[5]

/**
 * White King
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val WK = entries_[6]

/**
 * Black Pawn
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val BP = entries_[7]

/**
 * Black Knight
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val BN = entries_[8]

/**
 * Black Bishop
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val BB = entries_[9]

/**
 * Black Rook
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val BR = entries_[10]

/**
 * Black Queen
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val BQ = entries_[11]

/**
 * Black King
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
val BK = entries_[12]