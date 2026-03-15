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

import kotlin.math.log2

/**
 * A swiss style tournament.
 * @property eloCalculator The strategy used for rating adjustments.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
class SwissTournament(
    override val eloCalculator: EloCalculator = EloCalculator(),
) : Tournament {
    /** Total rounds expected for this tournament. */
    var numberOfRounds = 0
    private val names: MutableMap<String, Player> = mutableMapOf()
    private val rounds: MutableList<Round> = mutableListOf()
    private val naturalComparator = (Comparator<Player> { p1, p2 -> p1.compareTo(p2) })

    /** The strategy used to break ties between players with the same score.
     * Updating this field automatically updates the ranking logic.
     */
    override var playersComparator: Comparator<Player> = defaultTiebreakerComparator
        set(value) {
            field = value
            playersHeap.comparator = naturalComparator.thenComparator { a, b -> value.compare(a, b) }
        }
    private val playersHeap: Heap<Player> =
        Heap(10, naturalComparator.thenComparator { a, b -> playersComparator.compare(a, b) })
    private val customFirstRoundPairs: MutableList<Match> = mutableListOf()
    override var completed: Boolean = false
        get() {
            return numberOfRounds != 0 && rounds.size == numberOfRounds && rounds.last().completed
        }

    /** Standings sorted by score and tie-breakers. */
    override val leaderboard: List<Player>
        get() {
            var comparator = (Comparator<Player> { p1, p2 -> -p1.score.compareTo(p2.score) })
            playersComparator.let {
                comparator = comparator.thenComparator { a, b -> playersComparator.compare(a, b) }
            }
            val heap = Heap(playersHeap.size, comparator)
            names.values.forEach { heap += it }
            return heap.toList()
        }

    override val activeMatches: List<Match>
        get() {
            return if (rounds.isNotEmpty()) {
                rounds.last().games.filter { it.outcome == Outcome.IN_GAME }
            } else {
                listOf()
            }
        }

    override val finishedMatches: List<Match>
        get() {
            return if (rounds.isNotEmpty()) {
                rounds.last().games.filter { it.outcome != Outcome.IN_GAME } + rounds.flatMap { it.games }
            } else {
                listOf()
            }
        }

    private fun refreshHeap() {
        playersHeap.clear()
        names.asSequence()
            .filter {
                it.value.active
            }.forEach { (k, v) ->
                playersHeap += v
            }
    }

    /**
     * Adds a player to the tournament registry.
     */
    override fun addPlayer(player: Player) {
        names[player.name] = player
    }

    /**
     * Removes a player from the tournament.
     */
    override fun removePlayer(player: Player) {
        names.remove(player.name)
    }

    /**
     * Generates the next round of the tournament.
     * Pairs players with similar scores who haven't played each other yet.
     * @return A new [Round] with the calculated pairings.
     */
    override fun nextRound(): List<Match> {
        if (rounds.isEmpty()) {
            return firstRound()
        }
        if (!rounds.last().completed) {
            error("last round hasn't been completed")
        }
        val temporalPlayersQueue = ArrayDeque<Player>()
        val games = mutableListOf<Match>()
        refreshHeap()
        while (playersHeap.size > 1) {
            val white = playersHeap.pop()!!
            var black = playersHeap.pop()
            while (black in white.against && playersHeap.isNotEmpty()) {
                black?.let { temporalPlayersQueue.add(it) }
                black = playersHeap.pop()
            }
            temporalPlayersQueue.asSequence().forEach { e -> playersHeap += e }
            temporalPlayersQueue.clear()
            if (black != null) {
                val game = RatedMatch(white, black, eloCalculator)
                games.add(game)
            }
        }
        if (playersHeap.size > 1) {
            while (playersHeap.size > 1) {
                val white = playersHeap.pop()!!
                val black = playersHeap.pop()!!
                val game = RatedMatch(white, black, eloCalculator)
                games.add(game)
            }
        }
        if (playersHeap.size == 1) {
            val lastPlayer = playersHeap.pop()!!
            val game = MockMatch(Outcome.WW, lastPlayer)
            games.add(game)
        }
        rounds.add(Round(games))
        return rounds.last().games
    }

    /**
     * Initializes the tournament and generates the first round.
     * It handles custom pre-defined matches and pairs the rest by rank.
     * @return The first [Round].
     */
    private fun firstRound(): List<Match> {
        numberOfRounds = log2(names.size.toDouble()).toInt() + names.size % 2
        val games = mutableListOf<Match>()
        val pairedPlayers = mutableListOf<Player>()
        if (customFirstRoundPairs.isNotEmpty()) {
            games += customFirstRoundPairs
            for (game in customFirstRoundPairs) {
                if (game.white != null) {
                    pairedPlayers += game.white!!
                }
                if (game.black != null) {
                    pairedPlayers += game.black!!
                }
            }
            pairedPlayers.forEach { it.disqualify() }
            refreshHeap()
        }
        while (playersHeap.size > 1) {
            val white = playersHeap.pop()!!
            val black = playersHeap.pop()!!
            val game = RatedMatch(white, black, eloCalculator)
            games.add(game)
        }
        if (playersHeap.size == 1) {
            val lastPlayer = playersHeap.pop()!!
            val fakeGame = MockMatch(Outcome.WW, lastPlayer)
            games.add(fakeGame)
        }
        pairedPlayers.forEach { it.enable() }
        rounds.add(Round(games))
        return rounds.last().games
    }

    /**
     * Manually defines a pairing for the first round.
     */
    fun addMatchOf(white: Player, black: Player) {
        customFirstRoundPairs.add(RatedMatch(white, black, eloCalculator))
    }

}

