package io.github.lunalobos.chess4kt.js



/**
 * Represents a score in a tournament.
 * This class uses an internal integer value to represent scores in increments of 0.5.
 * For example, a value of 1 represents a score of 0.5, and 2 represents 1.0.
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS
 * modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Score internal constructor(internal val backedScore: io.github.lunalobos.chess4kt.Score) : Comparable<Score> {

    /**
     * Converts the internal representation to a [Double].
     */
    fun toNumber(): Double = backedScore.toDouble()

    /**
     * Combines this score with another and returns a new [Score].
     */
    fun addScore(other: Score): Score = Score(backedScore.addScore(other.backedScore))

    override fun hashCode(): Int {
        return backedScore.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Score && backedScore == other.backedScore
    }

    /**
     * Returns the human-readable string representation of the score.
     * Whole numbers are returned as integers (e.g., "1"),
     * while half-points include the ".5" suffix (e.g., "1.5").
     */
    override fun toString(): String {
        return backedScore.toString()
    }

    override fun compareTo(other: Score): Int {
        return backedScore.compareTo(other.backedScore)
    }
}