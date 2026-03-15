package io.github.lunalobos.chess4kt.js

import kotlin.js.collections.JsReadonlyArray
import kotlin.js.collections.JsReadonlySet

/**
 * This class represents a tournament player.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS
 * modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Player internal constructor(internal val backedPlayer: io.github.lunalobos.chess4kt.Player) {

    /**
     * The player's name.
     */
    val name get() = backedPlayer.name

    /**
     * The Elo rating at the start of the tournament.
     */
    val initialElo get() = backedPlayer.initialElo

    /**
     * The current elo.
     */
    var currentElo: Int
        get() = backedPlayer.currentElo
        set(value) {
            backedPlayer.currentElo = value
        }

    /**
     * The score for the tournament.
     */
    var score: Score
        get() = Score(backedPlayer.score)
        set(value) {
            backedPlayer.score = value.backedScore
        }

    /**
     * The games with black pieces counter.
     */
    var blackScore: Int
        get() = backedPlayer.blackScore
        set(value) {
            backedPlayer.blackScore = value
        }

    /**
     * Set of players already faced.
     */
    @OptIn(ExperimentalJsCollectionsApi::class)
    val against: JsReadonlySet<Player>
        get() = backedPlayer.against.map { Player(it) }.toSet().asJsReadonlySetView()

    /**
     * Whether the player still active in the tournament.
     */
    var active: Boolean
        get() = backedPlayer.active
        set(value) {
            backedPlayer.active = value
        }

    /** Historical record of scores per round. */
    @OptIn(ExperimentalJsCollectionsApi::class)
    val roundScores: JsReadonlyArray<Score>
        get() = backedPlayer.roundScores.map { Score(it)}.asJsReadonlyArrayView()

    /** List of matches played by this participant. */
    @OptIn(ExperimentalJsCollectionsApi::class)
    val matches: JsReadonlyArray<Match>
        get() = backedPlayer.matches.map { Match(it) }.asJsReadonlyArrayView()

    override fun toString(): String {
        return backedPlayer.toString()
    }

}