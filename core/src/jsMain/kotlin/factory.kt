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

import io.github.lunalobos.chess4kt.EloCalculator
import io.github.lunalobos.chess4kt.tiebreakerComparatorOf
import kotlin.js.collections.JsReadonlyArray

private val initialPosition = Position(io.github.lunalobos.chess4kt.positionOf())

/**
 * Returns the standard starting [Position] (the startpos FEN).
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun startPos() = initialPosition

/**
 * Factory function to create a [Position] object from a FEN string. Throws an exception if the FEN string is invalid
 * or leads to an illegal position.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 *
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun positionOf(fen: String) = Position(io.github.lunalobos.chess4kt.positionOf(fen))


/**
 * Creates a [Move] object from its UCI notation string (e.g., "e7e8q").
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 *
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun moveOf(move: String) = Move(io.github.lunalobos.chess4kt.moveOf(move))


/**
 * Creates a new [Game] instance configured for analysis. Uses ANALYSIS with
 * AWARE, making the game fully mutable.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun analysisGame(idSupplier: () -> Any? = { null }): Game {
    return Game(io.github.lunalobos.chess4kt.analysisGame(idSupplier))
}

/**
 * Creates a new Game instance configured for strict competitive match play. Uses MATCH with strict
 * enforcement for the three-fold repetition and 50-move rules.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun strictMatch(idSupplier: () -> Any? = { null }): Game {
    return Game(io.github.lunalobos.chess4kt.strictMatch(idSupplier))
}

/**
 * Creates a new [Game] instance with fully customizable parameters. Allows setting the game mode, rule enforcement,
 * initial FEN, and PGN tags.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun customGame(
    gameMode: String,
    threeRepetitionsMode: String,
    fiftyMovesRuleMode: String,
    initialFen: String? = null,
    idSupplier: () -> Any? = { null }
): Game {
    return Game(
        io.github.lunalobos.chess4kt.customGame(
            gameMode = io.github.lunalobos.chess4kt.Game.GameMode.valueOf(gameMode),
            threeRepetitionsMode = io.github.lunalobos.chess4kt.Game.ThreeRepetitionsMode.valueOf(threeRepetitionsMode),
            fiftyMovesRuleMode = io.github.lunalobos.chess4kt.Game.FiftyMovesRuleMode.valueOf(fiftyMovesRuleMode),
            initialFen = initialFen,
            idSupplier = idSupplier
        )
    )
}

/**
 * Parses a string containing one or more games in Portable Game Notation (PGN) format.
 * Games are returned in ANALYSIS mode, making them mutable for subsequent use.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS
 * modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class, ExperimentalJsCollectionsApi::class)
@JsExport
fun parseGames(pgnInput: String, idSupplier: () -> Any? = { null }): JsReadonlyArray<Game> {
    return io.github.lunalobos.chess4kt.parseGames(pgnInput, idSupplier).map { Game(it) }.asJsReadonlyArrayView()
}

/**
 * Parses a string representation of a score into a [Score]. The input must be a string ending in either ".0"
 * (for whole points) or ".5" (for half points).
 */
@JsExport
fun scoreOf(score: String): Score {
    return Score(io.github.lunalobos.chess4kt.scoreOf(score))
}

/**
 * Creates a new [Player] instance from a given name and initial elo.
 */
@JsExport
fun playerOf(name: String, initialElo: Int): Player {
    return Player(io.github.lunalobos.chess4kt.Player(name, initialElo))
}

/**
 * Creates a match between two players with the given elo calculation parameters or with default values.
 */
@JsExport
fun matchOf(
    white: Player,
    black: Player,
    impactFactor: Double = 32.0,
    rangeFactor: Double = 400.0,
    logisticBase: Double = 10.0,
): Match {
    return Match(
        io.github.lunalobos.chess4kt.RatedMatch(
            white.backedPlayer, black.backedPlayer, EloCalculator(impactFactor, rangeFactor, logisticBase)
        )
    )
}

/**
 * Factory function to create a [Tournament] instance based on the specified type.
 *
 * @param type The tournament format to create. Supported values: "arena", "swiss".
 * @param tiebreakers an array with the tiebreakers names in order. Supported values for each name are: "blackGames",
 * "progressive", "sonnebornBerger", "fidePerformance", "linearPerformance", or "buchholz"
 * @param impactFactor The K-factor that determines how much a single match affects the rating. A higher value leads
 * to faster rating changes. Default is 32.0.
 * @param rangeFactor The scale factor used to determine win probability. In standard Elo (like Chess), this is
 * typically 400.0.
 * @param logisticBase The base of the exponent in the logistic function.
 * @return A [Tournament] instance.
 * @throws IllegalStateException if the [type] provided is not recognized.
 * @sample
 * val myTournament = tournament(
 * type = "swiss",
 * comparator = tiebreakerComparatorOf("buchholz", "blackGames")
 * )
 */
@JsExport
fun tournament(
    type: String,
    tiebreakers: JsArray<String> = arrayOf("fidePerformance", "buchholz", "progressive", "sonnebornBerger"),
    impactFactor: Double = 32.0,
    rangeFactor: Double = 400.0,
    logisticBase: Double = 10.0,
): Tournament {
    return Tournament(
        io.github.lunalobos.chess4kt.tournament(
            type,
            EloCalculator(impactFactor, rangeFactor, logisticBase),
            tiebreakerComparatorOf(*tiebreakers)
        )
    )
}