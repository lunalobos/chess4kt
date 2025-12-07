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

import io.github.lunalobos.chess4kt.Game.*


// dependency injection chain

//--------------------------------------------------logging functions--------------------------------------------------

private val loggers: MutableMap<String, Logger> = mutableMapOf()

private val defaultFilterLevel = Level.WARN

internal fun getLogger(name: String): Logger {
    return loggers.getOrPut(name) { Logger(name, defaultFilterLevel) }
}

private val logger = getLogger("io.github.lunalobos.chess4kt.factory")

//---------------------------------------------------------------------------------------------------------------------

//------------------------------------------------zobrits hash functions-----------------------------------------------
internal val computeZobristHash = yieldComputeZobristHash()

//---------------------------------------------------------------------------------------------------------------------

//---------------------------------------------bitboard factory functions----------------------------------------------

internal fun bitboardsOf(vararg pairs: Pair<Piece, Square>): LongArray {
    val bitboards = Array(12) { Bitboard.empty() }
    pairs.forEach { (piece, square) ->
        bitboards[piece.ordinal - 1] = bitboards[piece.ordinal - 1] or Bitboard.fromSquares(square)
    }
    return bitboards.map { it.value }.toLongArray()
}

//---------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------move factory functions------------------------------------------------

private val moves = yieldMoves()

private val movesMap = moves.asSequence()
    .filter { it != null }
    .fold(mutableMapOf<String, Move>()) { map, m ->
        map[m.toString()] = m!!
        map
    }

internal val moveFromOriginTarget = yieldMoveFromOriginTarget(moves)

/**
 * Creates a [Move] object from the specified origin and target squares (using Int as the squares types).
 *
 * @param origin the origin square as an Int
 * @param target the target square as an Int
 * @return the corresponding [Move] for origin and target squares
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun moveOf(origin: Int, target: Int) = moveFromOriginTarget(origin, target)

internal val moveFromOriginTargetPromotion = yieldMoveFromOriginTargetPromotion(moves)

/**
 * Creates a [Move] object from the specified origin and target squares (using Int as the squares types) and the
 * promotion piece.
 *
 * @param origin the origin square as an Int
 * @param target the target square as an Int
 * @param promotionPiece the promotion piece as an Int
 * @return the corresponding [Move] for origin and target squares
 * @throws [MoveException] for invalid moves
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun moveOf(origin: Int, target: Int, promotionPiece: Int) = moveFromOriginTargetPromotion(
    origin, target, promotionPiece
)

internal val moveFromOriginTargetObjects = yieldMoveFromOriginTargetObjects(moves)

/**
 * Creates a [Move] object from the origin square and target square [Square] enum constants.
 *
 * @param origin the origin [Square]
 * @param target the target [Square]
 * @return the corresponding [Move] for origina and target squares.
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun moveOf(origin: Square, target: Square) = moveFromOriginTargetObjects(origin, target)

internal val moveFromOriginTargetPromotionObjects = yieldMoveFromOriginTargetPromotionObjects(moves)

/**
 * Creates a [Move] object from the origin square, target square, and the promotion piece, using [Square] and [Piece]
 * enum constants.
 *
 * @param origin the origin [Square]
 * @param target the target [Square]
 * @param promotionPiece the promotion [Piece]
 * @return the corresponding [Move] for origin and target squares and promotion piece
 * @throws [MoveException] for invalid moves
 * @author lunalobos
 * @since 1.0.0-beta.1
 */
fun moveOf(origin: Square, target: Square, promotionPiece: Piece) = moveFromOriginTargetPromotionObjects(
    origin, target, promotionPiece
)

internal val moveFromString = yieldMoveFromString(movesMap)

/**
 * Creates a [Move] object from its UCI notation string (e.g., "e7e8q").
 *
 * @param move the move string in UCI notation
 * @return the corresponding [Move] for the move string
 * @throws [MoveException] for invalidad move strings
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun moveOf(move: String) = moveFromString(move)


//---------------------------------------------------------------------------------------------------------------------

//---------------------------------------------visible metrics functions-----------------------------------------------

private val computeVisible = yieldComputeVisible()

private val whitePawnCaptureMoves = yieldWhitePawnCaptureMoves()

internal val visibleSquaresWhitePawn = yieldVisibleSquaresWhitePawn(whitePawnCaptureMoves)

private val blackPawnCaptureMoves = yieldBlackPawnCaptureMoves()

internal val visibleSquaresBlackPawn = yieldVisibleSquaresBlackPawn(blackPawnCaptureMoves)

private val knightMovesMatrix = yieldKnightMovesMatrix()

internal val visibleSquaresKnight = yieldVisibleSquaresKnight(knightMovesMatrix)

internal val visibleSquaresBishop = yieldVisibleSquaresBishopAlternative(computeVisible)

internal val visibleSquaresRook = yieldVisibleSquaresRookAlternative(computeVisible)

internal val visibleSquaresQueen = yieldVisibleSquaresQueen(visibleSquaresBishop, visibleSquaresRook)

private val kingMovesMatrix = yieldKingMovesMatrix()

internal val visibleSquaresKing = yieldVisibleSquaresKing(kingMovesMatrix)

private val calculators = yieldCalculators(
    visibleSquaresWhitePawn,
    visibleSquaresBlackPawn,
    visibleSquaresKnight,
    visibleSquaresBishop,
    visibleSquaresRook,
    visibleSquaresQueen,
    visibleSquaresKing
)

internal val immediateThreats = yieldImmediateThreats(calculators)

internal val threats = yieldThreats(
    visibleSquaresKnight,
    visibleSquaresBishop,
    visibleSquaresRook,
    visibleSquaresQueen,
    visibleSquaresKing,
    calculators
)

internal val visibleSquares = yieldVisibleSquares(computeVisible)

//---------------------------------------------------------------------------------------------------------------------

//---------------------------------------------pawn generator functions------------------------------------------------

internal val pawnMoves = yieldPawmMoves(
    visibleSquaresRook,
    moveFromOriginTarget,
    moveFromOriginTargetPromotion
)

//---------------------------------------------------------------------------------------------------------------------

//--------------------------------------------knight generator functions-----------------------------------------------

internal val knightMoves = yieldKnightMoves(moveFromOriginTarget, knightMovesMatrix)

//---------------------------------------------------------------------------------------------------------------------

//--------------------------------------------bishop generator functions-----------------------------------------------

internal val bishopMoves = yieldBishopMoves(visibleSquaresBishop, moveFromOriginTarget)

//---------------------------------------------------------------------------------------------------------------------

//---------------------------------------------rook generator functions------------------------------------------------

internal val rookMoves = yieldRookMoves(visibleSquaresRook, moveFromOriginTarget)

//---------------------------------------------------------------------------------------------------------------------

//--------------------------------------------queen generator functions------------------------------------------------

internal val queenMoves = yieldQueenMoves(visibleSquaresQueen, moveFromOriginTarget)

//---------------------------------------------------------------------------------------------------------------------

//---------------------------------------------king generator functions------------------------------------------------

internal val kingMoves = yieldKingMoves(kingMovesMatrix, threats, moveFromOriginTarget)

//---------------------------------------------------------------------------------------------------------------------

//-------------------------------------------bitboard generator functions----------------------------------------------

internal val checkInfo = yieldCheckInfo(visibleSquares)

internal val checkMask = yieldCheckMask(visibleSquares)

internal val movesInfo = yieldMovesInfo(
    pawnMoves,
    knightMoves,
    bishopMoves,
    rookMoves,
    queenMoves,
    kingMoves,
    checkMask,
    checkInfo
)

//---------------------------------------------------------------------------------------------------------------------

//--------------------------------------------position factory functions-----------------------------------------------

internal val startpos = Position()

/**
 * Returns the standard starting [Position] (the startpos FEN).
 *
 * @return the start position
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun positionOf(): Position {
    return startpos
}

/**
 * Factory function to create a [Position] object from a FEN string. Throws an exception if the FEN string is invalid
 * or leads to an illegal position.
 *
 * @return the position corresponding to the entered fen string
 * @throws [IllegalArgumentException] if the FEN string is invalid or leads to an illegal position.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun positionOf(fen: String): Position {
    return positionFromFen(fen)
}

//---------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------eco functions-----------------------------------------------------

private val eco = Eco()

internal fun ecoInfo(moves: String): EcoInfo? {
    return eco.movesMap[moves]
}

internal fun ecoInfo(position: Position): EcoInfo? {
    return eco.positionMap[position]
}

//---------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------game factory functions------------------------------------------------

/**
 * Creates a new [Game] instance configured for strict competitive match play. Uses [Game.GameMode.MATCH] with strict
 * enforcement for the three-fold repetition and 50-move rules.
 *
 *
 * @param idSupplier a lambda that provides different IDs each time it is called
 *
 * @return an match [Game]
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun strictMatch(idSupplier: () -> Any? = { null }): Game {
    return Game(
        mutableMapOf(),
        GameMode.MATCH,
        ThreeRepetitionsMode.STRICT,
        FiftyMovesRuleMode.STRICT,
        idSupplier
    )
}

internal fun awareMatch(idSupplier: () -> Any? = { null }): Game {
    return Game(
        mutableMapOf(),
        GameMode.MATCH,
        ThreeRepetitionsMode.AWARE,
        FiftyMovesRuleMode.AWARE,
        idSupplier
    )
}

/**
 * Creates a new [Game] instance configured for analysis. Uses [Game.GameMode.ANALYSIS] with
 * [Game.ThreeRepetitionsMode.AWARE] and [Game.FiftyMovesRuleMode.AWARE], making the game fully mutable.
 *
 *
 * @param idSupplier a lambda that provides different IDs each time it is called
 *
 * @return an analysis [Game]
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun analysisGame(idSupplier: () -> Any? = { null }): Game {
    return Game(
        mutableMapOf(),
        GameMode.ANALYSIS,
        ThreeRepetitionsMode.AWARE,
        FiftyMovesRuleMode.AWARE,
        idSupplier
    )
}

/**
 * Creates a new [Game] instance with fully customizable parameters. Allows setting the game mode, rule enforcement,
 * initial FEN, and PGN tags.
 *
 * @param gameMode the game mode
 * @param threeRepetitionsMode the three repetitions mode
 * @param fiftyMovesRuleMode the fifty moves rule mode
 * @param initialFen the initial fen for thematics games
 * @param initialTags the initial tags of the game
 * @param idSupplier a lambda that provides different IDs each time it is called
 *
 * @return a customized [Game]
 * @throws [IllegalArgumentException] for bad initial fen string
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun customGame(
    gameMode: GameMode,
    threeRepetitionsMode: ThreeRepetitionsMode,
    fiftyMovesRuleMode: FiftyMovesRuleMode,
    initialFen: String? = null,
    initialTags: Map<String, String>? = null,
    idSupplier: () -> Any? = { null }
): Game {
    val tags = mutableMapOf<String, String>()
    initialTags?.let { tags.putAll(initialTags) }
    initialFen?.let { tags["fen"] = it }
    return Game(
        tags,
        gameMode,
        threeRepetitionsMode,
        fiftyMovesRuleMode,
        idSupplier
    )
}

/**
 * Parses a string containing one or more games in Portable Game Notation (PGN) format.
 * Games are returned in [Game.GameMode.ANALYSIS] mode, making them mutable for subsequent use.
 *
 * @param pgnInput the complete string containing one or more PGN games.
 * @param idSupplier a lambda that provides different IDs each time it is called
 *
 * @return A list of successfully parsed [Game] objects.
 * @throws [Parser.ParserException] for bad pgn input
 * @throws [MoveException] for bad documented games
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
fun parseGames(pgnInput: String, idSupplier: () -> Any? = { null }): List<Game> {
    val tokenizer = Tokenizer(pgnInput)
    val tokens = tokenizer.tokenize()
    val parser = Parser(tokens)
    val games = parser.parseGames(idSupplier)
    return games
}