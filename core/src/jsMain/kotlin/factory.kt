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
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 *
 */
@OptIn(ExperimentalJsExport::class, ExperimentalJsCollectionsApi::class)
@JsExport
fun parseGames(pgnInput: String, idSupplier: () -> Any? = { null }): JsReadonlyArray<Game> {
    return io.github.lunalobos.chess4kt.parseGames(pgnInput, idSupplier).map { Game(it) }.asJsReadonlyArrayView()
}