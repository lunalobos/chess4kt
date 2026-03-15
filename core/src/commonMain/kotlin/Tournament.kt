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
 * Contract for tournament management.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
interface Tournament {
    /** The engine used to calculate rating changes. */
    val eloCalculator: EloCalculator

    /** The strategy for breaking ties in the leaderboard. */
    var playersComparator: Comparator<Player>

    /** Current player standings, ordered by score and tie-breakers. */
    val leaderboard: List<Player>

    /** Whether the tournament has reached its conclusion. */
    var completed: Boolean

    /** Current matches being played. */
    val activeMatches: List<Match>

    /** All concluded matches in the tournament history. */
    val finishedMatches: List<Match>

    /**
     * Adds a player to the tournament. Player's name most be unique.
     */
    fun addPlayer(player: Player)

    /**
     * Removes a player from the tournament
     */
    fun removePlayer(player: Player)

    /**
     * Generates a list of new pairings.
     */
    fun nextRound(): List<Match>
}