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

import io.github.lunalobos.chess4kt.Game.Node
import io.github.lunalobos.chess4kt.Piece.*
import kotlin.math.sign


internal val COLS = arrayOf("a", "b", "c", "d", "e", "f", "g", "h")

internal fun getColLetter(square: Int): String {
    val colNum: Int = getCol(square)
    return COLS[colNum]
}

internal fun getCol(square: Int): Int {
    return square and 7
}

internal fun getRow(square: Int): Int {
    return square shr 3
}

internal fun friendsAndEnemies(bitboards: LongArray, isWhiteMove: Boolean): LongArray {
    val black = bitboards[6] or bitboards[7] or bitboards[8] or bitboards[9] or bitboards[10] or bitboards[11]
    val white = bitboards[0] or bitboards[1] or bitboards[2] or bitboards[3] or bitboards[4] or bitboards[5]
    val friends: Long
    val enemies: Long
    if (isWhiteMove) {
        friends = white
        enemies = black
    } else {
        friends = black
        enemies = white
    }
    return longArrayOf(friends, enemies)
}

internal fun defenseDirection(kingSquare: Int, pieceSquare: Int): Long {
    val matrix: Array<IntArray> = queenMegamatrix[kingSquare]
    var result = 0L
    var d = 0
    for (i in 1..8) {
        for (square in matrix[i - 1]) {
            val operation1 = (1L shl pieceSquare) and (1L shl square)
            d = d or (if (isPresent(operation1)) {
                -1
            } else {
                0
            } and i)
        }
    }
    val matrix2 = arrayOf(
        intArrayOf(), matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5],
        matrix[6], matrix[7]
    )
    for (square in matrix2[d]) {
        result = result or (1L shl square)
    }
    return result
}

internal fun isPresent(bitboard: Long): Boolean {
    val signum = bitboard.sign
    return (signum * signum) == 1
}

internal fun stringRepresentation(squares: IntArray, fen: String): String {
    val sb = StringBuilder()
    sb.append("\n+---+---+---+---+---+---+---+---+ \n")
    for (i in 7 downTo 0) {
        for (j in 0..7) {
            when (Piece.entries[squares[i * 8 + j]]) {
                WP -> sb.append("| P ")
                WN -> sb.append("| N ")
                WB -> sb.append("| B ")
                WR -> sb.append("| R ")
                WQ -> sb.append("| Q ")
                WK -> sb.append("| K ")
                BP -> sb.append("| p ")
                BN -> sb.append("| n ")
                BB -> sb.append("| b ")
                BR -> sb.append("| r ")
                BQ -> sb.append("| q ")
                BK -> sb.append("| k ")
                else -> sb.append("|   ")
            }
            if (j == 7) {
                val row = i + 1
                sb.append("| ").append(row).append("\n")
                sb.append("+---+---+---+---+---+---+---+---+ \n")
            }
        }
    }
    sb.append("  a   b   c   d   e   f   g   h \n")
    sb.append("Fen: ").append(fen)
    return sb.toString()
}

internal fun toSquares(b: LongArray): IntArray {
    val squares = IntArray(64)
    for (i in 0..63) {
        var piece = 0
        for (j in 1..12) {
            piece += j * (((1L shl i) and b[j - 1]) ushr i).toInt()
        }
        squares[i] = piece
    }
    return squares
}

internal fun toFen(
    squares: IntArray,
    whiteMove: Boolean,
    whiteCastleKingside: Boolean,
    whiteCastleQueenside: Boolean,
    blackCastleKingside: Boolean,
    blackCastleQueenside: Boolean,
    enPassant: Int,
    halfMovesCounter: Int,
    movesCounter: Int
): String {
    val fenSB = StringBuilder()

    for (i in 7 downTo 0) {
        val rowFenSB = StringBuilder()
        var emptyCounter = 0
        for (j in i * 8..<i * 8 + 8) {
            if (squares[j] == EMPTY.ordinal) emptyCounter++
            else if (emptyCounter != 0) {
                rowFenSB.append(emptyCounter as Int?)
                emptyCounter = 0
            }
            if (squares[j] == EMPTY.ordinal && j == i * 8 + 7) rowFenSB.append(emptyCounter as Int?)
            when (entries[squares[j]]) {
                WP -> rowFenSB.append("P")
                WN -> rowFenSB.append("N")
                WB -> rowFenSB.append("B")
                WR -> rowFenSB.append("R")
                WQ -> rowFenSB.append("Q")
                WK -> rowFenSB.append("K")
                BP -> rowFenSB.append("p")
                BN -> rowFenSB.append("n")
                BB -> rowFenSB.append("b")
                BR -> rowFenSB.append("r")
                BQ -> rowFenSB.append("q")
                BK -> rowFenSB.append("k")
                else -> {}
            }
        }
        if (i == 7) fenSB.append(rowFenSB)
        else fenSB.append("/").append(rowFenSB)
    }
    val sideToMove = if (whiteMove) "w" else "b"
    var castleAbility = ""

    if (whiteCastleKingside) castleAbility += "K"
    if (whiteCastleQueenside) castleAbility += "Q"
    if (blackCastleKingside) castleAbility += "k"
    if (blackCastleQueenside) castleAbility += "q"

    if (!whiteCastleKingside && !whiteCastleQueenside && !blackCastleKingside && !blackCastleQueenside) castleAbility += "-"
    val ep: String
    if (enPassant == -1) ep = "-"
    else ep = (getColLetter(getCol(enPassant))
            + (if (getRow(enPassant) == 3) 3 else 6))
    val halfMoveClock = "" + halfMovesCounter

    val fullMoveCounter = "" + movesCounter

    fenSB.append(" ").append(sideToMove).append(" ").append(castleAbility).append(" ").append(ep).append(" ")
        .append(halfMoveClock).append(" ").append(fullMoveCounter)

    return fenSB.toString().trim { it <= ' ' }
}

internal fun <T> bitboardToList(
    bitboard: Long,
    entitiesFactory: (Long) -> T
): MutableList<T> {
    var copy = bitboard
    val list = mutableListOf<T>()
    while (copy != 0L) {
        val lowestOneBit = copy and -copy
        list.add(entitiesFactory(lowestOneBit))
        copy = copy and lowestOneBit.inv()
    }
    return list
}

internal fun <T> bitboardToCollectedList(
    bitboard: Long,
    entitiesFactory: (Long) -> List<T>
): MutableList<T> {
    var copy = bitboard
    val list = mutableListOf<T>()
    while (copy != 0L) {
        val lowestOneBit = copy and -copy
        list.addAll(entitiesFactory(lowestOneBit))
        copy = copy and lowestOneBit.inv()
    }
    return list
}


internal fun Bitboard.toSquares(): List<Square> {
    return bitboardToList(value) { Square.get(it.countTrailingZeroBits()) }
}

internal fun <T> bitboardToSequence(
    bitboard: Long,
    entitiesFactory: (Long) -> T
): Sequence<T> = sequence {
    var copy = bitboard
    while (copy != 0L) {
        val lowestOneBit = copy and -copy
        yield(entitiesFactory(lowestOneBit))
        copy = copy and lowestOneBit.inv()
    }
}

internal fun <T> bitboardToCollectedSequence(
    bitboard: Long,
    entitiesFactory: (Long) -> Sequence<T>
): Sequence<T> = sequence {
    var copy = bitboard
    while (copy != 0L) {
        val lowestOneBit = copy and -copy
        yieldAll(entitiesFactory(lowestOneBit))
        copy = copy and lowestOneBit.inv()
    }
}

internal fun getColIndex(column: String): Int {
    return binarySearch(COLS, column)
}

private fun binarySearch(a: Array<String>, key: String): Int {
    var low = 0
    var high = a.size - 1

    while (low <= high) {
        val mid = (low + high) ushr 1
        val midVal = a[mid]
        val cmp = midVal.compareTo(key)

        if (cmp < 0) low = mid + 1
        else if (cmp > 0) high = mid - 1
        else return mid
    }
    return -(low + 1)
}

internal fun getSquareIndex(col: Int, row: Int): Int {
    return col + row * 8
}

internal fun positionFromFen(fen: String): Position {
    // fen format
    if (!isValidFenFormat(fen)) {
        throw IllegalArgumentException("invalid fen $fen")
    }

    val position = Position(fen)

    val bitboards = Position(fen).bitboards

    // side to move in check
    val validCheck = !inCheck(bitboards, !position.whiteMove)

    // pawns in 8th rank
    val wpBitboards = bitboards[Piece.WP.ordinal - 1]
    val bpBitboards = bitboards[Piece.BP.ordinal - 1]
    val pawnsBitboard = wpBitboards or bpBitboards
    val rank = -0x100000000000000L or 0xFFL
    val noPawnsIn8thRank = (pawnsBitboard and rank) == 0L

    // castle
    var validWk = true
    if (position.whiteCastleKingside) {
        val rookBitboard = 1L shl 7
        val kingBitboard = 1L shl 4
        validWk = ((bitboards[WR.ordinal - 1] and rookBitboard).countOneBits() == 1)
                && ((bitboards[WK.ordinal - 1] and kingBitboard).countOneBits() == 1)
    }

    var validWq = true
    if (position.whiteCastleQueenside) {
        val rookBitboard = 1L
        val kingBitboard = 1L shl 4
        validWq = ((bitboards[WR.ordinal - 1] and rookBitboard).countOneBits() == 1)
                && ((bitboards[WK.ordinal - 1] and kingBitboard).countOneBits() == 1)
    }

    var validBk = true
    if (position.blackCastleKingside) {
        val rookBitboard = 1L shl 63
        val kingBitboard = 1L shl 60
        validBk = ((bitboards[BR.ordinal - 1] and rookBitboard).countOneBits() == 1)
                && ((bitboards[BK.ordinal - 1] and kingBitboard).countOneBits() == 1)
    }

    var validBq = true
    if (position.blackCastleQueenside) {
        val rookBitboard = 1L shl 56
        val kingBitboard = 1L shl 60
        validBq = ((bitboards[BR.ordinal - 1] and rookBitboard).countOneBits() == 1)
                && ((bitboards[BK.ordinal - 1] and kingBitboard).countOneBits() == 1)
    }

    // en passant
    val enPassantSquare = position.enPassant
    val pawnBitboard = bitboards[(if (position.whiteMove) BP.ordinal else WP.ordinal) - 1]
    val isValidEnPassant = ((pawnBitboard and (1L shl enPassantSquare)).countOneBits() == 1) || (enPassantSquare == -1)

    if (validCheck && noPawnsIn8thRank && validWk && validWq && validBk && validBq && isValidEnPassant) {
        return position
    } else {
        throw IllegalArgumentException(
            """
            fen provided $fen derives in an illegal position validCheck=$validCheck, noPawnsIn8thRank=$noPawnsIn8thRank,
            validWk=$validWk, validWq=$validWq, validBk=$validBk, validBq=$validBq, validEnPassant=$isValidEnPassant
            """.trimIndent()
        )
    }
}

private val WK_PATTERN = Regex("K")
private val BK_PATTERN = Regex("k")
private val INVALID_ROW_PATTERN = Regex("[^PNBRQKpnbrqk12345678]")
private val VALID_CASTLE_ABILITY_PATTERN = Regex($$"^([-]{1}|([K]?[Q]?[k]?[q]?))$")
private val VALID_EN_PASSANT_PATTERN = Regex($$"^[-]{1}$|^[abcdefgh][36]$")
private val VALID_HALF_MOVE_CLOCK_PATTERN = Regex($$"^[0123456789]+$")
private val VALID_FULL_MOVE_COUNTER_PATTERN = Regex($$"^[1-9][0-9]*$")

private fun isValidFenFormat(fen: String): Boolean {
    val parts = fen.split(" ")

    if (parts.size != 6) return false

    val piecesString = fen.split(" ")[0]

    // has 8 rows
    val rows = piecesString.split("/")
    val has8Rows = rows.size == 8
    if(!has8Rows){
        throw IllegalArgumentException("$fen has no 8 rows")
    }

    // valid rows
    var validRows = true

    for (row in rows) {
        validRows = validRows && !INVALID_ROW_PATTERN.containsMatchIn(row)
        if(!validRows){
            throw IllegalArgumentException("$fen has an invalid row $row")
        }
    }

    // valid side to move
    val sideToMove = parts[1]
    val validSideToMove = sideToMove == "w" || sideToMove == "b"

    if(!validSideToMove){
        throw IllegalArgumentException("$fen has no valid side to move $sideToMove")
    }

    // valid castle ability
    val castleAbility = parts[2]
    val validCastleAbility = VALID_CASTLE_ABILITY_PATTERN.containsMatchIn(castleAbility)

    if(!validCastleAbility){
        throw IllegalArgumentException("$fen has no valid castle ability $castleAbility")
    }

    // valid en passant
    val enPassant = parts[3]
    val validEnPassant = VALID_EN_PASSANT_PATTERN.containsMatchIn(enPassant)

    if(!validEnPassant){
        throw IllegalArgumentException("$fen has no valid en passant $enPassant")
    }

    // valid half move clock
    val halfMoveClock = parts[4]
    val validHalfMoveClock = VALID_HALF_MOVE_CLOCK_PATTERN.containsMatchIn(halfMoveClock)

    if(!validHalfMoveClock){
        throw IllegalArgumentException("$fen has no valid half move clock $halfMoveClock")
    }

    // valid full move counter
    val fullMoveCounter = parts[5]
    val validFullMoveCounter = VALID_FULL_MOVE_COUNTER_PATTERN.containsMatchIn(fullMoveCounter)

    if(!validFullMoveCounter){
        throw IllegalArgumentException("$fen has no valid full move counter $fullMoveCounter")
    }

    // Kings presence
    val kingsPresence = WK_PATTERN.containsMatchIn(piecesString) &&
            BK_PATTERN.containsMatchIn(piecesString)

    if(!kingsPresence){
        throw IllegalArgumentException("$fen has no king presence")
    }

    return has8Rows && validRows && validSideToMove && validCastleAbility && validEnPassant
            && validHalfMoveClock && validFullMoveCounter && kingsPresence
}

/**
 * The square exposed to an en passant capture, if one exists. Returns null if no en passant capture is possible in
 * the current position.
 *
 * @return the piece's square exposed to en passant capture as an enum or null
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
val Position.enPassantSquare: Square?
    get() {
        return if (enPassant == -1) {
            null
        } else {
            Square.valueOf("${getColLetter(getCol(enPassant))?.uppercase() ?: ""}${(if (getRow(enPassant) == 3) 3 else 6)}")
        }
    }

/**
 * True if the position is a forced draw. This is true if the position results in a stalemate or lackOfMaterial. False
 * otherwise.
 *
 * @return true if the position is a forced draw
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
val Position.draw: Boolean get() = stalemate || lackOfMaterial

/**
 * True if the game state is concluded (terminal position), either due to a forced draw or checkmate. False otherwise.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
val Position.gameOver: Boolean get() = checkmate || stalemate || lackOfMaterial

/**
 * The side ([Side.WHITE] or [Side.BLACK]) whose turn it is to move.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
val Position.sideToMove: Side get() = if (whiteMove) Side.WHITE else Side.BLACK

/**
 * A list of tuples, where each tuple represents a legal move from this position and the resulting new position
 * (Tuple<Position, Move>). This list effectively defines the legal branches of the game tree from the current position.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
val Position.children: List<Tuple<Position, Move>> get() = generateChildren(mi, this)

/**
 * Retrieves the new [Position] that results from executing the provided legal [Move]. Throws a MoveException if the
 * provided move is not legal in the current position.
 *
 * @param move the move to play
 *
 * @return the new [Position] that results from executing the move
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Position.move(move: Move): Position {
    return children.find { it.v2 == move }?.v1 ?: throw MoveException("illegal move $move")
}

/**
 * Retrieves the new [Position] that results from executing the move specified in the given notation. Throws a
 * MoveException if the move is not legal. If no notation is provided, [Notation.UCI] is assumed.
 *
 * @param move the move string
 * @param notation the move string notation
 *
 * @return the new [Position] that results from executing the move
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Position.move(move: String, notation: Notation = Notation.UCI): Position {
    return when (notation) {
        Notation.UCI -> moveFromString(move).let { m -> children.find { it.v2 == m }?.v1 }
            ?: throw MoveException("illegal move $move")

        Notation.SAN -> sanToMove(this, move).let { m -> children.find { it.v2 == m }?.v1 }
            ?: throw MoveException("illegal move $move")
    }

}

/**
 * Returns true if the evaluated [Move] is legal in the current position, and false otherwise.
 *
 * @param move the move to evaluate
 *
 * @return true if the move is a legal move, false otherwise
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Position.isLegal(move: Move): Boolean {
    val m = moveFromOriginTargetPromotion(move.origin, move.target, move.promotionPiece)
    return children.any { it.v2 == m }
}

/**
 * Returns true if the move specified in the given notation is legal in the current position. If no notation is
 * specified, [Notation.UCI] is assumed.
 *
 * @param move the move string
 * @param notation the move string notation
 * @return true if the move is a legal move, false otherwise
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Position.isLegal(move: String, notation: Notation = Notation.UCI): Boolean {
    return when (notation) {
        Notation.UCI -> children.any { it.v2.toString() == move }
        Notation.SAN -> children.any { toSan(this, it.v2) == move }
    }
}


private val PIECES = arrayOf("", "", "N", "B", "R", "Q", "K", "", "N", "B", "R", "Q", "K")

internal fun toSan(position: Position, move: Move): String {
    require(position.isLegal(move)) { "illegal move $move for position ${position.fen}" }
    var sbSAN = StringBuilder()

    // Identify the piece being moved.
    val piece = entries[position.squares[move.origin]]
    sbSAN.append(PIECES[piece.ordinal])

    // Determinate if the piece is a pawn.
    val isPawn = (piece == WP) || (piece == BP)

    // Determine if there are one or more pieces of the same type that can move to the same destination.
    val moves: List<Move> = position.children.asSequence()
        .map { it.v2 }
        .filter { m -> m.target == move.target }
        .filter { m -> entries[position.squares[m.origin]] === entries[position.squares[move.origin]] }
        .filter { m -> m.origin != move.origin }
        .toList()

    // If such pieces exist, determine if they are in the same column.
    val sameColumn = moves.any { m -> getCol(m.origin) == getCol(move.origin) }

    // If such pieces exist, determine if they are in the same row.
    val sameRow: Boolean = moves.any { m -> getRow(m.origin) == getRow(move.origin) }

    // If they are in the same row but not in the same column, append the column letter.
    if (sameRow && !sameColumn && !isPawn) {
        sbSAN.append(getColLetter(move.origin))
    }

    // If they are in the same column but not in the same row, append the row number.
    if (!sameRow && sameColumn) {
        sbSAN.append(getRow(move.origin) + 1)
    }

    // If they are in the same column and row, append the origin square.
    if (sameRow && sameColumn) {
        sbSAN.append(getColLetter(move.origin)).append(getRow(move.origin) + 1)
    }

    // If they are not in the same row nor the same column, append the column letter.
    if (!sameRow && !sameColumn && !moves.isEmpty() && !isPawn) sbSAN.append(getColLetter(move.origin))

    // Determining if a piece is captured
    val capture = position.squares[move.target] != Piece.EMPTY.ordinal
    if (capture) {
        if (isPawn) {
            sbSAN.append(getColLetter(move.origin))
        }
        sbSAN.append("x")
    }

    // Append target square
    sbSAN.append(getColLetter(move.target)).append(getRow(move.target) + 1)

    // Determine if it is a promotion. If so, append "=" + promotedPiece to the destination square.
    val isPromotion = move.promotionPiece != -1 && (isPromotion(move.target) == 1L)
    if (isPromotion) {
        sbSAN.append("=").append(PIECES[move.promotionPiece])
    }

    // Check if the move is a castling move
    val isCastle = ((piece === WK) or (piece === BK)) && (move.toString() in listOf("e1g1", "e1c1", "e8g8", "e8c8"))
    if (isCastle) {
        sbSAN = StringBuilder()
        sbSAN.append(
            if (move.toString().startsWith("g1", 2) or move.toString().startsWith("g8", 2))
                "O-O"
            else
                "O-O-O"
        )
    }

    // Determine if it is a check or checkmate
    val p = position.move(move)

    if (p.checkmate) {
        sbSAN.append("#")
    } else if (p.check) {
        sbSAN.append("+")
    }

    return sbSAN.toString()
}

internal fun genericHashCode(a: Array<Any?>?): Int {
    if (a == null) return 0
    var result = 1
    for (element in a) result = 31 * result + (element?.hashCode() ?: 0)
    return result
}

internal fun getSquareIndex(square: String): Int {
    val chars = square.toCharArray()
    val collum: Int = getColIndex(chars[0].toString())
    val row = chars[1].toString().toInt() - 1
    return getSquareIndex(collum, row)
}

internal fun getSquare(square: String): Square {
    return Square.entries[getSquareIndex(square)]
}

internal val MOVE_PATTERN = Regex(
    "(?<move>(?<regular>(?<piece>[KQBNR])?(?<originCol>[a-h])?(?<originRow>[1-8])?x?(?<targetCol>[a-h])(?<targetRow>[1-8])=?(?<promotion>[QBNR])?)|(?<castle>O-O(-O)?))(?<check>[+#])?"
)

internal fun sanToMove(position: Position, sanMove: String): Move {

    val san = sanMove.trim()
    val matchResult: MatchResult = MOVE_PATTERN.find(san)
        ?: throw IllegalArgumentException("The given expression is not in the standard algebraic notation format: $sanMove")

    // We use the index operator [] and the Elvis operator (?:) for safe access to named groups
    val castleGroupValue = matchResult.groups["castle"]?.value
    val isWhiteMove = position.whiteMove

    // 1. Handling Castling
    if (castleGroupValue != null) {
        return when (castleGroupValue) {
            "O-O" -> { // King-side castling
                val (origin, target) = if (isWhiteMove) Pair(Square.E1, Square.G1) else Pair(Square.E8, Square.G8)
                moveOf(origin, target)
            }

            "O-O-O" -> { // Queen-side castling
                val (origin, target) = if (isWhiteMove) Pair(Square.E1, Square.C1) else Pair(Square.E8, Square.C8)
                moveOf(origin, target)
            }

            else -> null // This should not happen with the current pattern
        } ?: throw MoveException("Invalid castling move: $sanMove")
    }

    // 2. Processing the regular move
    val originCol = matchResult.groups["originCol"]?.value.orEmpty()
    val originRow = matchResult.groups["originRow"]?.value.orEmpty()
    val origin = originCol + originRow

    val targetCol = matchResult.groups["targetCol"]?.value
        ?: throw MoveException("Missing target column in SAN move: $sanMove")
    val targetRow = matchResult.groups["targetRow"]?.value
        ?: throw MoveException("Missing target row in SAN move: $sanMove")
    val target = targetCol + targetRow

    val promotionPieceChar = matchResult.groups["promotion"]?.value.orEmpty()
    val promotionPieceSAN = promotionPieceChar.uppercase()

    // 3. Non-pawn and non-ambiguous move (Full origin, e.g., 'Ng1e2' or 'a7a8=Q')
    if (origin.length == 2) {
        return if (promotionPieceSAN.isNotEmpty()) {
            moveOf(
                getSquare(origin),
                getSquare(target),
                Piece.valueOf("${if (isWhiteMove) "W" else "B"}$promotionPieceSAN")
            )
        } else {
            moveOf(getSquare(origin), getSquare(target))
        }
    }

    // 4. Ambiguous or pawn move (Unspecified or partial origin)

    // Determining the Piece (default 'P' for Pawn)
    val pieceChar = matchResult.groups["piece"]?.value.orEmpty()
    val pieceSAN = if (pieceChar.isEmpty()) "P" else pieceChar.uppercase()
    val piece = Piece.valueOf((if (isWhiteMove) "W" else "B") + pieceSAN)

    // Destructuring the target to get the index
    val xDestiny = getColIndex(targetCol)
    val yDestiny = targetRow.toInt() - 1 // Board row (1-8) to index (0-7)
    val destinySquare = getSquareIndex(xDestiny, yDestiny)

    // Searching within the position's possible moves (children).
    return position.mi.movesList.asSequence()
        .filter { m -> m.target == destinySquare } // Filter by target square
        .filter { m ->
            // Filter by piece type (the moved piece must match the determined one)
            val movePiece = entries[position.squares[m.origin]]
            movePiece == piece
        }
        .filter { m ->
            // Handle ambiguity (partial origin: only rank or only file)
            when (origin.length) {
                1 -> {
                    if (origin.first().isDigit()) { // It's a Rank (e.g., '6')
                        val row = origin.toInt() - 1
                        getRow(m.origin) == row
                    } else { // It's a File (e.g., 'h')
                        val col = getColIndex(origin)
                        getCol(m.origin) == col
                    }
                }

                0 -> true // Unspecified origin (non-ambiguous or simple pawn move)
                else -> false // Case already handled above (length == 2)
            }
        }.firstOrNull { m ->
            // Filter by promoted piece
            if (m.promotionPiece != -1) {
                val promotedPieceChar = entries[m.promotionPiece].toString().substring(1, 2)
                promotedPieceChar == promotionPieceSAN
            } else {
                promotionPieceSAN.isEmpty() // Must only match if there is no promotion in SAN
            }
        } ?: throw MoveException("The move '$sanMove' is not a legal move in the current position ${position.fen}")
}

internal fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

internal fun String.unCapitalize(): String {
    return this.replaceFirstChar {
        if (it.isUpperCase()) it.lowercase() else it.toString()
    }
}

/**
 * Removes the last node in the main line, effectively undoing the last move, and returns the parent node.
 * This function only operates on the primary variation (main line).
 *
 * @return The parent node of the removed move.
 * @throws Game.UnexpectedGameInternalError if the last move node has a null parent.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Game.unDo(): Node {
    val lastNode = this.iterator().asSequence().last()
    if (lastNode !is Game.MoveNode) {
        return lastNode
    }
    val parent = lastNode.parent ?: throw Game.UnexpectedGameInternalError("node parent can't be null")
    parent.removeChild(lastNode)
    repetitions.remove(lastNode)
    return parent
}

/**
 * Deletes all moves (the main line continuation and any variations) that follow the provided node.
 * The node provided remains in the game.
 *
 * @param node The node from which subsequent moves will be deleted (exclusive).
 * @return The provided node.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Game.deleteFromExclusive(node: Node): Node {
    node.children.clear()
    return node
}

/**
 * Deletes the provided node and all subsequent moves/variations in its subtree.
 * The move represented by the node is effectively removed from the game.
 *
 * @param node The node to be deleted (inclusive).
 * @return The parent node of the deleted node.
 * @throws Game.UnexpectedGameInternalError if the node is a root node or has a null parent.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Game.deleteFromInclusive(node: Node): Node {
    if (node !is Game.MoveNode) {
        // RootNode cannot be deleted.
        return node
    }
    val parent = node.parent
        ?: throw Game.UnexpectedGameInternalError("Cannot delete node; parent cannot be null for a MoveNode.")
    parent.removeChild(node)
    // Note: This removal should ideally trigger cleanup in the 'repetitions' counter for the entire removed subtree,
    // but the current Repetitions class doesn't support recursive cleanup easily.
    return parent
}

/**
 * Deletes all moves that preceded the provided node in the main line.
 * The node (and its position) becomes the new effective start of the game, creating a new RootNode.
 *
 * @param node The node that will become the first move of the new game history.
 * @return The provided node, which is now the first Node of the game.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun Game.deleteBefore(node: Node): Node {
    val oldParent = node.parent ?: return node
    val newParent = this.RootNode(oldParent.position).apply { children.add(node) }
    node.parent = newParent
    this.root = newParent
    val last = this.iterator().asSequence().last()
    checkGameOver(last)
    tags["fen"] = newParent.position.fen
    return node
}

/**
 * Determines if White has insufficient material to win the game.
 * Returns true if the current pieces for White cannot potentially lead to a checkmate.
 * @return `true` if White lacks winning material, `false` otherwise.
 * @author lunalobos
 * @since 1.0.0-beta.2
 */
fun Position.whiteLacksOfMaterial(): Boolean {
    return isWhiteLackOfMaterial(bitboards)
}

/**
 * Determines if Black has insufficient material to win the game.
 * Returns true if the current pieces for Black cannot potentially lead to a checkmate.
 * @return `true` if Black lacks winning material, `false` otherwise.
 * @author lunalobos
 * @since 1.0.0-beta.2
 */
fun Position.blackLacksOfMaterial(): Boolean {
    return isBlackLackOfMaterial(bitboards)
}