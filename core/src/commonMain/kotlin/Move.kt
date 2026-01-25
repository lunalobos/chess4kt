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

/**
 * Represents a single player's move on the board. It does not store information about the origin position,
 * as its primary purpose is move representation. Its string representation is the full algebraic notation
 * of the move as used in the UCI protocol (e.g., "e2e4", "a7a8q"). **It is immutable.**
 *
 * This class leverages bitwise operations for efficient storage and manipulation.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
open class Move {
    /**
     * The move encoded as a bitmask (Long), where a single bit is set for the target square.
     * The position of the set bit determines the target square index.
     */
    internal val move: Long

    /**
     * The zero-based index (0-63) of the move's origin square.
     */
    val origin: Int

    /**
     * The ordinal value of the promotion piece, or -1 if no promotion occurs.
     * The ordinal corresponds to the [Piece] enum.
     */
    val promotionPiece: Int

    /**
     * The zero-based index (0-63) of the move's target square.
     */
    val target: Int

    /**
     * Internal constructor for creating a Move using its bitboard representation and origin square.
     *
     * @param move The Long bitmask representing the target square (only one bit set).
     * @param origin The zero-based index (0-63) of the origin square.
     * @param promotionPiece The ordinal of the promotion piece, defaults to -1.
     * @throws RuntimeException if the move mask is zero, the origin is invalid, or the promotion piece is invalid.
     */
    internal constructor(move: Long, origin: Int, promotionPiece: Int = -1){
        if(move == 0L){
            throw logger.fatal(RuntimeException("Move mask cannot be zero."))
        }
        if(origin !in 0..63){
            throw logger.fatal(RuntimeException("Origin square index must be between 0 and 63 (inclusive), but was $origin."))
        }
        if(promotionPiece != -1 && promotionPiece !in (listOf(WN, WB, WR, WQ, BN, BB, BR, BQ).map{ it.ordinal })){
            throw logger.fatal(
                RuntimeException("Promotion piece ordinal must be -1 or a valid promotable piece ordinal, but was $promotionPiece."
                ))
        }
        this.move = move
        this.origin = origin
        this.promotionPiece = promotionPiece
        // Calculates the target square index (0-63) by counting trailing zeros in the bitmask.
        target = move.countTrailingZeroBits()
    }

    /**
     * Returns the origin square of the move as a [Square] enum constant.
     *
     * @return The [Square] of origin.
     *
     * @since 1.0.0-beta.1
     */
    fun origin(): Square {
        return Square.get(origin)
    }

    /**
     * Returns the target square of the move as a [Square] enum constant.
     *
     * @return The [Square] of the target.
     *
     * @since 1.0.0-beta.1
     */
    fun target(): Square {
        return Square.get(target)
    }

    /**
     * Returns the promotion piece of the move as a [Piece] enum constant.
     * Returns [Piece.EMPTY] if no promotion occurs.
     *
     * @return The [Piece] being promoted to, or [Piece.EMPTY].
     *
     * @since 1.0.0-beta.1
     */
    fun promotionPiece(): Piece {
        return if (promotionPiece < 0) EMPTY else { entries[promotionPiece] }
    }

    /**
     * Generates a 16-bit hash code based on the move's origin, target, and promotion piece.
     */
    override fun hashCode(): Int {
        // 16 bits hash calculation: origin (6 bits) | target (6 bits) | promotion (4 bits)
        return origin or (move.countTrailingZeroBits() shl 6) or (if (promotionPiece == -1) 0 else (promotionPiece shl 12))
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is Move) return false
        return origin == other.origin && move == other.move && promotionPiece == other.promotionPiece
    }

    /**
     * Returns the move in standard UCI notation (e.g., "e2e4", "a7a8q" for promotion).
     */
    override fun toString(): String {
        val sb = StringBuilder()
        // Appends origin square (e.g., "e2") and target square (e.g., "e4")
        sb.append(origin().name.lowercase()).append(target().name.lowercase())

        return if (promotionPiece == -1) {
            sb.toString()
        } else {
            // Appends the lowercase letter of the promotion piece (e.g., 'q' for WQ)
            sb.append(promotionPiece().name.substring(1).lowercase()).toString()
        }
    }

    internal companion object {
        val logger = getLogger("io.github.lunalobos.chess4kt.Move");
    }

}