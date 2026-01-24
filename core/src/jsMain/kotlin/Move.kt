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
 * Represents a single player's move on the board. It does not store information about the origin position,
 * as its primary purpose is move representation. Its string representation is the full algebraic notation
 * of the move as used in the UCI protocol (e.g., "e2e4", "a7a8q"). **It is immutable.**
 *
 * This class leverages bitwise operations for efficient storage and manipulation.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Move internal constructor(val backedMove: io.github.lunalobos.chess4kt.Move) {
    /**
     * The zero-based index (0-63) of the move's origin square.
     */
    val origin get() = backedMove.origin
    /**
     * The ordinal value of the promotion piece, or -1 if no promotion occurs.
     * The ordinal corresponds to the [Piece] enum.
     */
    val target get() = backedMove.target
    /**
     * The zero-based index (0-63) of the move's target square.
     */
    val promotionPiece get() = backedMove.promotionPiece

    override fun hashCode(): Int{
        return backedMove.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if(other is Move){
            backedMove == other.backedMove
        } else {
            false
        }
    }

    override fun toString(): String{
        return backedMove.toString()
    }
}