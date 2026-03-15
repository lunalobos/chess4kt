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

/**
 * Represents a participant in a tournament, tracking their score,
 * opponents, and eligibility.
 * @property name The player's name.
 * @property initialElo The Elo rating at the start of the tournament.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
class Player(
    val name: String,
    val initialElo: Int
) : Comparable<Player> {

    /**
     * The current elo.
     */
    var currentElo = initialElo

    /**
     * The score for the tournament.
     */
    var score = scoreOf("0.0")

    /**
     * The games with black pieces counter.
     */
    var blackScore = 0

    /**
     * Set of players already faced.
     */
    val against: MutableSet<Player> = HashSet()

    /**
     * Whether the player still active in the tournament.
     */
    var active = true

    /** Historical record of scores per round. */
    val roundScores = mutableListOf<Score>()

    /** List of matches played by this participant. */
    val matches = mutableListOf<Match>()

    /** Increments the count of games played with black pieces. */
    fun addAsBlackGame() {
        blackScore++
    }

    /** Records the current total score into the [roundScores] history. */
    fun accumulateScore() {
        roundScores.add(score)
    }

    /**
     * Compares two players to determine tournament standing.
     * Higher scores come first. If scores are equal, the player with
     * the lower blackScore (fewer games as black) is prioritized.
     */
    override fun compareTo(other: Player): Int {
        val scoreCompare = other.score.compareTo(this.score)
        return if (scoreCompare != 0) scoreCompare
        else -this.blackScore.compareTo(other.blackScore)
    }

    override fun hashCode(): Int {
        return name.hashCode() + initialElo + 31
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Player

        return initialElo == other.initialElo &&
                currentElo == other.currentElo &&
                blackScore == other.blackScore &&
                active == other.active &&
                name == other.name &&
                score == other.score &&
                against == other.against &&
                roundScores == other.roundScores &&
                matches == other.matches
    }

    override fun toString(): String = "$name ($currentElo)"

    /**
     * Disqualify the player from the tournament.
     */
    fun disqualify() {
        active = false
    }

    /**
     * Enable the player.
     */
    fun enable() {
        active = true
    }
}