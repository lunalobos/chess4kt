package io.github.lunalobos.chess4kt.js

import io.github.lunalobos.chess4kt.Outcome

/**
 * This class represents a match between two players.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS
 * modules of any KMP project.
 */
@JsExport
class Match internal constructor(internal val backedMatch: io.github.lunalobos.chess4kt.Match) {
    /**
     * The player competing with the White pieces.
     * Can be null.
     */
    val white: Player?
        get() = backedMatch.white?.let { Player(backedMatch.white!!) }

    /**
     * The player competing with the Black pieces.
     * Can be null.
     */
    val black: Player?
        get() = backedMatch.black?.let { Player(backedMatch.black!!) }

    /**
     * The result of the match.
     * Usually initialized as a 'Pending' state and updated once the game is over.
     */
    var outcome: String
        get() = backedMatch.outcome.representation
        set(value) {
            when (value) {
                "1-0" -> backedMatch.outcome = Outcome.WW
                "0-1" -> backedMatch.outcome = Outcome.BW
                "1/2-1/2" -> backedMatch.outcome = Outcome.DRAW
                "in game" -> backedMatch.outcome = Outcome.IN_GAME
                "suspended" -> backedMatch.outcome = Outcome.SUSPENDED
                else -> error("Unexpected tournamentOutcome: $value")
            }
        }

    override fun toString(): String {
        return backedMatch.toString()
    }
}