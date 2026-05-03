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
     * Unique ID generator for assigning identifiers to match objects.
     */
    val idGenerator: (() -> Any)?

    /**
     * The unique identifier for this tournament.
     *
     * @since v1.0.0-beta9c
     */
    val id: Any?

    /**
     * The display name of the tournament.
     *
     * @since v1.0.0-beta9c
     */
    val name: String?

    /**
     * The specific time settings for the matches (e.g., "3+2", "10|0").
     * Returns null if no specific time control has been defined.
     *
     * @since v1.0.0-beta9c
     */
    val timeControl: String?

    /**
     * The category of the tournament based on the time control (e.g., "blitz", "bullet", "rapid").
     *
     * @since v1.0.0-beta9c
     */
    val type: String?

    /**
     * Adds a player to the tournament. The player's name must be unique.
     *
     * @param player The player to be added.
     */
    fun addPlayer(player: Player)

    /**
     * Removes a player from the tournament.
     *
     * @param player The player to remove.
     */
    fun removePlayer(player: Player)

    /**
     * Generates a list of new pairings.
     *
     * @return a list containing the new pairings.
     */
    fun nextRound(): List<Match>

    /**
     * Adds a match to the tournament for the specified round. This method is ideal for cases
     * where external logic is being used or an instance is being reconstructed.
     *
     * @param white the white player
     * @param black the black player or null if not applicable
     * @param outcome the outcome as a string (WW, BW, DRAW, SUSPENDED, IN_GAME)
     * @param id the id for the match
     * @param round the round number, or null if not applicable (0-based)
     *
     * @return a boolean indicating whether the match was successfully added
     *
     * @since v1.0.0-beta.9c
     */
    fun addMatch(white: String, black: String?, outcome: String, id: String?, round: Int?): Boolean
}