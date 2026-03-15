package io.github.lunalobos.chess4kt.js

/**
 * Calculates a bitboard of all squares "visible" or attacked by a specific piece
 * from a given square.
 *
 * **Note on Pawn Behavior:** For [WP] and [BP], this returns only the
 * squares targeted for capture (the diagonals). It does not include forward
 * movement squares, as those are not considered "attacks" or "visible"
 * interactions with other pieces.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun visibleSquares(piece: String, square: String, position: Position): Bitboard {
    return Bitboard(io.github.lunalobos.chess4kt.visibleSquares(
        io.github.lunalobos.chess4kt.Piece.valueOf(piece.uppercase()),
        io.github.lunalobos.chess4kt.Square.valueOf(square.uppercase()),
        position.backedPosition
    ))
}