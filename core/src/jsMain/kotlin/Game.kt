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

import io.github.lunalobos.chess4kt.deleteBefore
import io.github.lunalobos.chess4kt.deleteFromExclusive
import io.github.lunalobos.chess4kt.deleteFromInclusive

/**
 * Class for manipulating games. It allows starting a game from a configurable initial position and with various
 * configuration options, such as the game type (match or analysis), and how to apply the three-fold repetition
 * and 50-move rules.
 *
 * This class implements [Iterable], allowing easy traversal over the nodes of the main line.
 * The toString function returns the game in PGN format.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Game internal constructor(private val backedGame: io.github.lunalobos.chess4kt.Game) {

    /**
     * The root node of the game tree. The setter is protected if the game is immutable.
     */
    val root = Node(backedGame.root)

    /**
     * Stores the current ECO (Encyclopedia of Chess Openings) information for the main line.
     * Setting this property will be ignored if the game is immutable (i.e., MATCH and result is set).
     */
    var ecoInfo
        get() = backedGame.ecoInfo?.let { EcoInfo(it) }
        set(value) {
            backedGame.ecoInfo = value?.backedEcoInfo
        }

    /**
     * A developer-provided unique identifier for serialization or tracking purposes (e.g., UUID or String).
     */
    val id = backedGame.id

    /**
     * The final result of the game.
     * Setting this property will update the "Result" tag in tags and set the game as immutable if MATCH is used.
     */
    var result: String?
        get() = backedGame.result?.toString()
        set(value) {
            backedGame.result = value?.let { io.github.lunalobos.chess4kt.Game.Result.valueOf(it) }
        }

    /**
     * Indicates whether the 50-move half-move limit has been reached (non-terminal).
     */
    var fiftyMoves
        get() = backedGame.fiftyMoves
        set(value) {
            backedGame.fiftyMoves = value
        }

    /**
     * An optional comment placed immediately after the game's result tag in PGN.
     * This comment is enclosed in braces {}.
     * Setting this property will be ignored if the game is immutable.
     */
    var finalComment
        get() = backedGame.finalComment
        set(value) {
            backedGame.finalComment = value
        }

    /**
     * Determines how the fifty-move rule is enforced for this game instance.
     * Setting this property will be ignored if the game is immutable.
     */
    val fiftyMovesRuleMode get() = backedGame.fiftyMovesRuleMode.toString()

    /**
     * An optional end-of-line comment placed immediately after the game's result tag in PGN.
     * This comment follows a semicolon ; and extends until the end of the line.
     * Setting this property will be ignored if the game is immutable.
     */
    var finalEndLineComment
        get() = backedGame.finalEndLineComment
        set(value) {
            backedGame.finalEndLineComment = value
        }

    /**
     * Indicates whether a position has been repeated five times, leading to an automatic draw according to FIDE rules
     * (terminal).
     */
    var fiveRepetitions
        get() = backedGame.fiveRepetitions
        set(value) {
            backedGame.fiveRepetitions = value
        }

    /**
     * Indicates whether the 75-move half-move limit has been reached, leading to an automatic draw (terminal).
     */
    var seventyFiveMoves
        get() = backedGame.seventyFiveMoves
        set(value) {
            backedGame.seventyFiveMoves = value
        }

    /**
     * Determines how the three-fold repetition rule is enforced for this game instance.
     * Setting this property will be ignored if the game is immutable.
     */
    val threeRepetitionsMode
        get() = backedGame.threeRepetitionsMode.toString()

    /**
     * Indicates that a three-fold repetition draw can be claimed, as the repetition is impending (e.g., the current
     * move will complete the third repetition).
     */
    var threeRepetitionsWarning
        get() = backedGame.threeRepetitionsWarning
        set(value) {
            backedGame.threeRepetitionsWarning = value
        }

    /**
     * Standard PGN tags (e.g., Event, Site, Date, Round, White, Black, Result).
     */
    val tags: dynamic
        get() {
            val p: dynamic = js("({})")
            backedGame.tags.entries.forEach {
                p[it.key] = it.value
            }
            return p
        }


    /**
     * Sets a tag pair (name and value)
     */
    fun setTag(name: String, value: String) {
        backedGame.tags[name] = value
    }

    /**
     * Retrieves the tag's value
     */
    fun getTag(name: String): String? = backedGame.tags[name]

    override fun toString(): String {
        return backedGame.toString()
    }

    /**
     * Creates a deep copy of this game, converting its mode to ANALYSIS and setting both repetition rules
     * to AWARE. This makes the new instance fully mutable for
     * analysis.
     *
     * The idea behind idSupplier is to give the developer the flexibility to provide their own ID generator (idSupplier()).
     * This allows the object's ID to be custom-defined, which aids the developer in various operations such as serializing
     * the object, indexing it in an in-memory or on-disk database, or leveraging the ID in any way that facilitates or
     * enables operations.
     */
    fun toAnalysis(idSupplier: () -> Any? = { null }): Game{
        return Game(backedGame.toAnalysis(idSupplier))
    }

    /**
     * Deletes all moves (the main line continuation and any variations) that follow the provided node.
     * The node provided remains in the game.
     */
    fun deleteFromExclusive(node: Node): Node {
        return Node(backedGame.deleteFromExclusive(node.backedNode))
    }

    /**
     * Deletes all moves (the main line continuation and any variations) that follow the provided node.
     * The move represented by the node is effectively removed from the game.
     */
    fun deleteFromInclusive(node: Node): Node{
        return Node(backedGame.deleteFromInclusive(node.backedNode))
    }

    /**
     * Deletes all moves that preceded the provided node in the main line.
     * The node (and its position) becomes the new effective start of the game, creating a new RootNode.
     *
     */
    fun Game.deleteBefore(node: Node): Node{
        return Node(backedGame.deleteBefore(node.backedNode))
    }

}