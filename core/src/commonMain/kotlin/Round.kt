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
 * Represents a single round in a tournament, consisting of multiple matches.
 * This class provides mechanisms to track the completion status of the round
 * and to update match outcomes in a batch.
 *
 * @property games The list of matches (pairings) scheduled for this round.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
internal class Round internal constructor(
    val games: MutableList<Match> = mutableListOf()
) : Iterable<Match> {

    /**
     * Returns true if all games in the round have a final result.
     * A round is incomplete if any match is still marked as [Outcome.IN_GAME].
     */
    val completed: Boolean
        get() = !games.any { it.outcome == Outcome.IN_GAME }

    /**
     * Updates the outcomes for all games in the round simultaneously.
     * @param outcomes A variable number of [Outcome] objects.
     * The order must match the order of the [games] list.
     * @throws IllegalArgumentException If the number of outcomes provided does not
     * match the number of games in the round.
     */
    fun setOutcomes(vararg outcomes: Outcome) {
        if (outcomes.size != games.size) {
            throw IllegalArgumentException("Arguments count (${outcomes.size}) differs from games count (${games.size})")
        }
        for (i in 0..<games.size) {
            games[i].outcome = outcomes[i]
        }
    }

    /**
     * Allows iterating over the matches in this round.
     */
    override fun iterator(): Iterator<Match> {
        return games.iterator()
    }

    /**
     * Returns a formatted string displaying the status of all matches in the round,
     * each on a new line.
     */
    override fun toString(): String {
        val sb = StringBuilder()
        for (game in games) {
            sb.append(game.toString()).append("\n")
        }
        return sb.toString()
    }
}