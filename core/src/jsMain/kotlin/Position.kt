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

import io.github.lunalobos.chess4kt.blackLacksOfMaterial
import io.github.lunalobos.chess4kt.children
import io.github.lunalobos.chess4kt.draw
import io.github.lunalobos.chess4kt.enPassantSquare
import io.github.lunalobos.chess4kt.gameOver
import io.github.lunalobos.chess4kt.isLegal
import io.github.lunalobos.chess4kt.move
import io.github.lunalobos.chess4kt.positionOf
import io.github.lunalobos.chess4kt.sideToMove
import io.github.lunalobos.chess4kt.whiteLacksOfMaterial
import kotlin.js.collections.JsReadonlyArray
import kotlin.js.collections.toList
import kotlin.js.json

/**
 * Class to represent a position on the chessboard. **It is immutable.**
 *
 * This class provides all the necessary information to perform conventional logical operations based on a position,
 * primarily through bitboard representations. This class cannot be instantiated directly but must be accessed
 * via the factory function [positionOf], which ensures the provided FEN string (if any) is well-formed and valid.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Position internal constructor(private val backedPosition: io.github.lunalobos.chess4kt.Position) {
    /**
     * Array of **Bitboards**. The index in the array is equal to the ordinal value minus one
     * of the corresponding piece (e.g., Black Knight distribution is at index `BN.ordinal - 1`).
     *
     * Each Bitboard has 64 bits, where each bit represents a square in the board, following the [Square] ordinal order
     * (A1 is bit 0, B1 is bit 1, up to H8 which is bit 63). A bit set to 1 indicates the presence of the piece
     * corresponding to the array index, and 0 indicates absence.
     */
    @OptIn(ExperimentalJsCollectionsApi::class)
    val bitboards: JsReadonlyArray<Bitboard> = backedPosition.bitboards.map { Bitboard(it) }.asJsReadonlyArrayView()

    /**
     * True if it is White's turn to move. False if it is Black's turn.
     */
    val whiteMove = backedPosition.whiteMove

    /**
     * The square index (0-63) where a pawn can be captured en passant.
     * Returns -1 if no en passant capture is possible.
     */
    val enPassant = backedPosition.enPassant

    /**
     * True if White can castle kingside (short castling). False otherwise.
     */
    val whiteCastleKingside = backedPosition.whiteCastleKingside

    /**
     * True if White can castle queenside (long castling). False otherwise.
     */
    val whiteCastleQueenside = backedPosition.whiteCastleQueenside

    /**
     * True if Black can castle kingside (short castling). False otherwise.
     */
    val blackCastleKingside = backedPosition.blackCastleKingside

    /**
     * True if Black can castle queenside (long castling). False otherwise.
     */
    val blackCastleQueenside = backedPosition.blackCastleQueenside

    /**
     * The full move number in the game (starts at 1 and is incremented after Black's move).
     */
    val movesCounter= backedPosition.movesCounter

    /**
     * The number of half-moves since the last pawn move or capture.
     * This is used for the 50-move rule.
     */
    val halfMovesCounter = backedPosition.halfMovesCounter


    /**
     * True if the current side to move is in check. False otherwise.
     */
    val check get() = backedPosition.check

    /**
     * True if the position is a checkmate (the current side is in check and has no legal moves). False otherwise.
     */
    val checkmate get() = backedPosition.checkmate

    /**
     * True if the position is a stalemate (the current side is not in check but has no legal moves). False otherwise.
     */
    val stalemate get() = backedPosition.stalemate

    /**
     * True if the position is a draw due to insufficient mating material (e.g., King vs. King). False otherwise.
     */
    val lackOfMaterial get() = backedPosition.lackOfMaterial

    /**
     * True if the position has reached or exceeded 50 half-moves without a pawn move or capture.
     */
    val fiftyMoves get() = backedPosition.fiftyMoves

    /**
     * The Zobrist hash key of the position. This is used for efficient position lookup and repetition detection.
     */
    val zobrist get() = Bitboard(backedPosition.zobrist)

    /**
     * A 64-element array where the index corresponds to the square order defined in [Square] (A1=0, H8=63).
     * The value of each element is the ordinal of the [Piece] occupying that square (0 for EMPTY, 1 for WP, and so on).
     */
    val squares get() = backedPosition.squares

    /**
     * The FEN (Forsyth-Edwards Notation) string representation of the position.
     */
    val fen get() = backedPosition.fen


    /**
     * A list of tuples, where each tuple represents a legal move from this position and the resulting new position
     * (Tuple<Position, Move>). This list effectively defines the legal branches of the game tree from the current position.
     *
     */
    @OptIn(ExperimentalJsCollectionsApi::class)
    val children
        get() = backedPosition.children.map {
            val obj = js("{}")
            Tuple.of(Position(it.v1), Move(it.v2))
        }.asJsReadonlyArrayView()

    /**
     * True if the position is a forced draw. This is true if the position results in a stalemate or lackOfMaterial. False
     * otherwise.
     */
    val draw get() = backedPosition.draw

    /**
     * The square exposed to an en passant capture, if one exists. Returns null if no en passant capture is possible in
     * the current position.
     */
    val enPassantSquare get() = backedPosition.enPassantSquare?.let { Square.indexToSquare(it.ordinal) }

    /**
     * True if the game state is concluded (terminal position), either due to a forced draw or checkmate. False otherwise.
     *
     */
    val gameOver get() = backedPosition.gameOver

    /**
     * The side (WHITE or BLACK) whose turn it is to move.
     *
     */
    val sideToMove get() = Side.get(backedPosition.sideToMove.name)!!

    /**
     * Determines if White has insufficient material to win the game.
     * Returns true if the current pieces for White cannot potentially lead to a checkmate.
     */
    fun whiteLacksOfMaterial() = backedPosition.whiteLacksOfMaterial()

    /**
     * Determines if Black has insufficient material to win the game.
     * Returns true if the current pieces for Black cannot potentially lead to a checkmate.
     */
    fun blackLacksOfMaterial() = backedPosition.blackLacksOfMaterial()

    /**
     * Retrieves the piece object that occupies the given square.
     */
    fun pieceAt(square: Square): Piece {
        return Piece.entries_[squares[square.ordinal]]
    }

    /**
     * Returns true if the evaluated [Move] is legal in the current position, and false otherwise.
     */
    fun isLegal(move: Move): Boolean {
        return backedPosition.isLegal(move.backedMove)
    }

    /**
     * Retrieves the new [Position] that results from executing the provided legal [Move]. Throws a MoveException if the
     * provided move is not legal in the current position.
     */
    fun move(move: Move): Position {
        return Position(backedPosition.move(move.backedMove))
    }

    /**
     * Retrieves the new [Position] that results from executing the move specified in the given notation. Throws a
     * MoveException if the move is not legal. If no notation is provided, UCI notation is assumed.
     */
    fun moveFromString(move: String, notation: Notation = UCI): Position {
        return Position(backedPosition.move(move, io.github.lunalobos.chess4kt.Notation.valueOf(notation.name)))
    }

    override fun hashCode() = backedPosition.hashCode()

    override fun equals(other: Any?): Boolean {
        return if(other is Position) {
            backedPosition == other.backedPosition
        } else {
            false
        }
    }

    override fun toString() = backedPosition.toString()


}