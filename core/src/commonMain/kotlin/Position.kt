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

import io.github.lunalobos.chess4kt.Piece.*
import io.github.lunalobos.chess4kt.Square.*

/**
 * Class to represent a position on the chessboard. **It is immutable.**
 *
 * This class provides all the necessary information to perform conventional logical operations based on a position,
 * primarily through bitboard representations. This class cannot be instantiated directly but must be accessed
 * via the factory function [positionOf], which ensures the provided FEN string (if any) is well-formed and valid.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
class Position {

    internal companion object {
        private val emptyPossibilities = listOf('1', '2', '3', '4', '5', '6', '7', '8')
    }

    /**
     * Array of Long integers used as **Bitboards**. The index in the array is equal to the ordinal value minus one
     * of the corresponding piece (e.g., Black Knight distribution is at index `Piece.BN.ordinal - 1`).
     *
     * Each Long has 64 bits, where each bit represents a square in the board, following the [Square] enum order
     * (A1 is bit 0, B1 is bit 1, up to H8 which is bit 63). A bit set to 1 indicates the presence of the piece
     * corresponding to the array index, and 0 indicates absence.
     */
    val bitboards: LongArray get() = field.copyOf()

    /**
     * True if it is White's turn to move. False if it is Black's turn.
     */
    val whiteMove: Boolean

    /**
     * The square index (0-63) where a pawn can be captured en passant.
     * Returns -1 if no en passant capture is possible.
     */
    val enPassant: Int

    /**
     * True if White can castle kingside (short castling). False otherwise.
     */
    val whiteCastleKingside: Boolean

    /**
     * True if White can castle queenside (long castling). False otherwise.
     */
    val whiteCastleQueenside: Boolean

    /**
     * True if Black can castle kingside (short castling). False otherwise.
     */
    val blackCastleKingside: Boolean

    /**
     * True if Black can castle queenside (long castling). False otherwise.
     */
    val blackCastleQueenside: Boolean

    /**
     * The full move number in the game (starts at 1 and is incremented after Black's move).
     */
    val movesCounter: Int

    /**
     * The number of half-moves since the last pawn move or capture.
     * This is used for the 50-move rule.
     */
    val halfMovesCounter: Int

    /**
     * Internal data structure containing pre-calculated legal moves and attack information for the current position.
     */
    internal val mi: MovesInfo get() {
        return movesInfo(
            bitboards,
            whiteMove,
            whiteCastleKingside,
            whiteCastleQueenside,
            blackCastleKingside,
            blackCastleQueenside,
            enPassant
        )
    }
    private val moves: Long by lazy { mi.moves }

    /**
     * True if the current side to move is in check. False otherwise.
     */
    val check: Boolean by lazy { inCheck(bitboards, whiteMove) }

    /**
     * True if the position is a checkmate (the current side is in check and has no legal moves). False otherwise.
     */
    val checkmate: Boolean by lazy { isCheckmate(bitboards, whiteMove, moves) }

    /**
     * True if the position is a stalemate (the current side is not in check but has no legal moves). False otherwise.
     */
    val stalemate: Boolean by lazy { isStalemate(bitboards, whiteMove, moves) }

    /**
     * True if the position is a draw due to insufficient mating material (e.g., King vs. King). False otherwise.
     */
    val lackOfMaterial: Boolean by lazy { isLackOfMaterial(bitboards) }

    /**
     * True if the position has reached or exceeded 50 half-moves without a pawn move or capture.
     */
    val fiftyMoves: Boolean by lazy { halfMovesCounter >= 50 }

    /**
     * The Zobrist hash key of the position. This is used for efficient position lookup and repetition detection.
     */
    val zobrist: Long by lazy {
        computeZobristHash(
            bitboards,
            whiteMove,
            whiteCastleKingside,
            whiteCastleQueenside,
            blackCastleKingside,
            blackCastleQueenside,
            enPassant
        )
    }

    /**
     * A 64-element array where the index corresponds to the square order defined in [Square] (A1=0, H8=63).
     * The value of each element is the ordinal of the [Piece] occupying that square (0 for [Piece.EMPTY], 1 for [Piece.WP], and so on).
     */
    val squares: IntArray get() {
        val sqs = IntArray(64)
        val b = bitboards
        for (i in 0..63) {
            var piece = 0
            for (j in 1..12) {
                piece += j * (((1L shl i) and b[j - 1]) ushr i).toInt()
            }
            sqs[i] = piece
        }
        return sqs
    }

    /**
     * The FEN (Forsyth-Edwards Notation) string representation of the position.
     */
    val fen: String get() {
        return toFen(
            squares,
            whiteMove,
            whiteCastleKingside,
            whiteCastleQueenside,
            blackCastleKingside,
            blackCastleQueenside,
            enPassant,
            halfMovesCounter,
            movesCounter
        )
    }

    // startpos
    internal constructor() {
        bitboards = arrayOf(
            arrayOf(A2, B2, C2, D2, E2, F2, G2, H2),
            arrayOf(B1, G1),
            arrayOf(C1, F1),
            arrayOf(A1, H1),
            arrayOf(D1),
            arrayOf(E1),
            arrayOf(A7, B7, C7, D7, E7, F7, G7, H7),
            arrayOf(B8, G8),
            arrayOf(C8, F8),
            arrayOf(A8, H8),
            arrayOf(D8),
            arrayOf(E8)
        ).map { squares -> Bitboard.fromSquares(*squares).value }.toLongArray()
        whiteMove = true
        enPassant = -1
        whiteCastleKingside = true
        whiteCastleQueenside = true
        blackCastleKingside = true
        blackCastleQueenside = true
        movesCounter = 1
        halfMovesCounter = 0
    }

    // manual constructor
    internal constructor(
        bitboards: LongArray,
        whiteMove: Boolean,
        enPassant: Int,
        whiteCastleKingside: Boolean,
        whiteCastleQueenside: Boolean,
        blackCastleKingside: Boolean,
        blackCastleQueenside: Boolean,
        mc: Int,
        hm: Int
    ) {
        this.bitboards = bitboards
        this.whiteMove = whiteMove
        this.enPassant = enPassant
        this.whiteCastleKingside = whiteCastleKingside
        this.whiteCastleQueenside = whiteCastleQueenside
        this.blackCastleKingside = blackCastleKingside
        this.blackCastleQueenside = blackCastleQueenside
        this.movesCounter = mc
        this.halfMovesCounter = hm
    }

    // fen constructor
    internal constructor(fen: String) {
        val sqrs = IntArray(64)
        val parts = fen.split(" ")
        val rows = parts[0].split("/")
        for (h in 7 downTo 0) {
            val chars = rows[7 - h].toCharArray()
            var i = 0
            for (character in chars) {
                if (character in emptyPossibilities) {
                    i += character.toString().toInt()
                } else {
                    when (character) {
                        'P' -> sqrs[h * 8 + i] = WP.ordinal
                        'N' -> sqrs[h * 8 + i] = WN.ordinal
                        'B' -> sqrs[h * 8 + i] = WB.ordinal
                        'R' -> sqrs[h * 8 + i] = WR.ordinal
                        'Q' -> sqrs[h * 8 + i] = WQ.ordinal
                        'K' -> sqrs[h * 8 + i] = WK.ordinal
                        'p' -> sqrs[h * 8 + i] = BP.ordinal
                        'n' -> sqrs[h * 8 + i] = BN.ordinal
                        'b' -> sqrs[h * 8 + i] = BB.ordinal
                        'r' -> sqrs[h * 8 + i] = BR.ordinal
                        'q' -> sqrs[h * 8 + i] = BQ.ordinal
                        'k' -> sqrs[h * 8 + i] = BK.ordinal
                        else -> IllegalArgumentException("invalid character $character in fen string")
                    }
                    i++
                }
            }
        }

        // squares to bitboards
        val b = LongArray(12)
        for (i in 0..63) {
            if (sqrs[i] > 0) {
                b[sqrs[i] - 1] = b[sqrs[i] - 1] or (1L shl i)
            }
        }
        bitboards = b

        // side to move
        whiteMove = parts[1] == "w"

        // castle rights
        val castlePart = parts[2]
        var wk = false
        var bk = false
        var wq = false
        var bq = false
        if (castlePart != "-") {
            val chars = castlePart.toCharArray()
            for (character in chars) {
                when (character) {
                    'K' -> wk = true
                    'k' -> bk = true
                    'Q' -> wq = true
                    'q' -> bq = true
                }
            }
        }
        this.whiteCastleKingside = wk
        this.blackCastleKingside = bk
        this.whiteCastleQueenside = wq
        this.blackCastleQueenside = bq

        // en passant
        val enPassantPart = parts[3]
        if (enPassantPart == "-") {
            enPassant = -1
        } else {
            val chars = enPassantPart.toCharArray()
            val col = "" + chars[0]
            val x = getColIndex(col)
            val y = if (("" + chars[1]).toInt() == 6) 4 else if (("" + chars[1]).toInt() == 3) 3 else
                throw IllegalArgumentException("invalid en passant string")
            enPassant = getSquareIndex(x, y)
        }

        // half moves
        halfMovesCounter = parts[4].toInt()
        require(!(halfMovesCounter > 50 || halfMovesCounter < 0)) { "Half moves has to be over -1 and bellow 51." }

        // moves counter
        movesCounter = parts[5].toInt()
    }

    override fun hashCode(): Int = zobrist.hashCode()

    override fun toString(): String = stringRepresentation(squares, fen)

    override fun equals(other: Any?): Boolean {
        return if (other == null) {
            false
        } else if (other === this) {
            true
        } else if (other !is Position) {
            false
        } else {
            bitboards.contentEquals(other.bitboards) && (whiteMove == other.whiteMove) &&
                    (enPassant == other.enPassant) && (whiteCastleKingside == other.whiteCastleKingside) &&
                    (whiteCastleQueenside == other.whiteCastleQueenside) &&
                    (blackCastleKingside == other.blackCastleKingside) &&
                    (blackCastleQueenside == other.blackCastleQueenside)
        }
    }
}