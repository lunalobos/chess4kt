package io.github.lunalobos.chess4kt.js

import io.github.lunalobos.chess4kt.tiebreakerComparatorOf
import kotlin.js.collections.JsReadonlyArray

/**
 * Instances of this class can manage tournament logic.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS
 * modules of any KMP project.
 */
@OptIn(kotlin.js.ExperimentalJsExport::class)
@JsExport
class Tournament internal constructor(internal val backedTournament: io.github.lunalobos.chess4kt.Tournament) {

    /**
     * The K-factor that determines how much a single match affects the rating. A higher value leads to faster rating
     * changes. Default is 32.0.
     */
    var impactFactor: Double
        get() = backedTournament.eloCalculator.impactFactor
        set(value) {
            backedTournament.eloCalculator.impactFactor = value
        }

    /**
     * The scale factor used to determine win probability. Default is 400.0.
     */
    var rangeFactor: Double
        get() = backedTournament.eloCalculator.rangeFactor
        set(value) {
            backedTournament.eloCalculator.rangeFactor = value
        }

    /**
     * The base of the exponent in the logistic function. Default is 10.
     */
    var logisticBase: Double
        get() = backedTournament.eloCalculator.logisticBase
        set(value) {
            backedTournament.eloCalculator.logisticBase = value
        }

    /**
     *  |
     */
    @OptIn(ExperimentalWasmJsInterop::class)
    var tiebreakers: JsArray<String> = arrayOf("fidePerformance", "buchholz", "progressive", "sonnebornBerger")
        set(value) {
            backedTournament.playersComparator = tiebreakerComparatorOf(*value)
            field = value
        }

    /**
     * Current player standings, ordered by score and tie-breakers.
     */
    @OptIn(ExperimentalJsCollectionsApi::class, ExperimentalJsExport::class)
    val leaderboard: JsReadonlyArray<Player>
        get() = backedTournament.leaderboard.map { Player(it) }.asJsReadonlyArrayView()

    /**
     * Whether the tournament has reached its conclusion.
     */
    var completed: Boolean
        get() = backedTournament.completed
        set(value) {
            backedTournament.completed = value
        }

    /**
     * Current matches being played.
     */
    @OptIn(ExperimentalJsCollectionsApi::class, ExperimentalJsExport::class)
    val activeMatches: JsReadonlyArray<Match>
        get() = backedTournament.activeMatches.map { Match(it) }.asJsReadonlyArrayView()

    /**
     * All concluded matches in the tournament history.
     */
    @OptIn(ExperimentalJsCollectionsApi::class, ExperimentalJsExport::class)
    val finishedMatches: JsReadonlyArray<Match>
        get() = backedTournament.finishedMatches.map { Match(it) }.asJsReadonlyArrayView()

    /**
     * Adds a player to the tournament. Player's name most be unique.
     */
    fun addPlayer(player: Player) = backedTournament.addPlayer(player.backedPlayer)

    /**
     * Removes a player from the tournament
     */
    fun removePlayer(player: Player) = backedTournament.removePlayer(player.backedPlayer)

    /**
     * Generates a list of new pairings.
     */
    @OptIn(ExperimentalJsCollectionsApi::class, ExperimentalJsExport::class)
    fun nextRound(): JsReadonlyArray<Match> = backedTournament.nextRound().map { Match(it) }
        .asJsReadonlyArrayView()

}