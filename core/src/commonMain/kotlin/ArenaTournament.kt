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
 * An arena style tournament.
 * @property eloCalculator The strategy used for rating adjustments.
 */
class ArenaTournament(
    override val eloCalculator: EloCalculator = EloCalculator()
) : Tournament {
    companion object {
        private val logger = getLogger("ArenaTournament")
    }

    private val names: MutableMap<String, Player> = mutableMapOf()
    override val activeMatches: MutableList<Match> = mutableListOf()
    override val finishedMatches: MutableList<Match> = mutableListOf()
    private val indexGenerator = IndexGenerator()

    override var playersComparator: Comparator<Player> = defaultTiebreakerComparator
        set(value) {
            val comparator = Comparator<ArenaPlayer> { a, b -> a.player.matches.size.compareTo(b.player.matches.size) }
                .thenComparator { a, b -> -a.player.currentElo.compareTo(b.player.currentElo) }
                .thenComparator { a, b -> value.compare(a.player, b.player) }
                .thenComparator { a, b -> a.index.compareTo(b.index) }
            field = value
            playersHeap.comparator = comparator
        }

    private val playersHeap = Heap<ArenaPlayer>(
        10,
        Comparator<ArenaPlayer> { a, b -> a.player.matches.size.compareTo(b.player.matches.size) }
            .thenComparator { a, b -> -a.player.currentElo.compareTo(b.player.currentElo) }
            .thenComparator { a, b -> playersComparator.compare(a.player, b.player) }
            .thenComparator { a, b -> a.index.compareTo(b.index) }
    )

    override var completed: Boolean = false

    override fun addPlayer(player: Player) {
        if (names[player.name] == null) {
            names[player.name] = player
            val arenaPlayer = arenaPlayerOf(player)
            logger.debug("adding player ${player.name}")
            playersHeap += arenaPlayer
        }
    }

    override fun removePlayer(player: Player) {
        names.remove(player.name)
    }

    override fun nextRound(): List<Match> {
        val newMatches = mutableListOf<ArenaMatch>()
        if (completed) return emptyList()
        while (playersHeap.size >= 2) {
            val player1 = playersHeap.pop()!!
            logger.debug("matchmaking white: $player1")
            var player2 = playersHeap.pop()
            if (player2!!.player in player1.player.against) {
                val aux = player2
                player2 = playersHeap.pop()
                if (player2 == null) {
                    player2 = aux
                } else {
                    playersHeap += aux
                }
            }
            logger.debug("matchmaking black: $player2")
            val match = ArenaMatch(player1.player, player2.player, eloCalculator)
            activeMatches.add(match)
            newMatches.add(match)
        }
        if (newMatches.isEmpty()) {
            logger.trace("no matchmaking because of players heap size ${playersHeap.size}")
        }
        return newMatches
    }

    override val leaderboard: List<Player>
        get() {
            val comparator = Comparator<Player> { a, b ->
                -a.score.compareTo(b.score)
            }.thenComparator { a, b -> playersComparator.compare(a, b) }

            val heap = Heap(10, comparator)
            names.values.forEach { heap += it }
            return heap.toList()
        }

    private fun arenaPlayerOf(player: Player): ArenaPlayer {
        return ArenaPlayer(player, indexGenerator.next())
    }

    private data class ArenaPlayer(
        val player: Player,
        val index: Int,
    )

    private class IndexGenerator() {
        private var curr: Int = 0
        fun next(): Int {
            return curr++
        }
    }

    private inner class ArenaMatch(
        white: Player,
        black: Player,
        eloCalculator: EloCalculator
    ) : RatedMatch(white, black, eloCalculator) {

        override var outcome: Outcome
            set(value) {
                super.outcome = value
                logger.debug("removing match $this from activeMatches")
                activeMatches.remove(this)
                finishedMatches.add(this)
                logger.debug("players heap ${playersHeap.toList()}")
                if (!completed) {
                    if (white.active && (white.name !in playersHeap.toList().map { it.player.name })) {
                        playersHeap += arenaPlayerOf(white)
                    } else {
                        logger.debug("player $white not added to the heap")
                    }
                    if (black.active && (black.name !in playersHeap.toList().map { it.player.name })) {
                        playersHeap += arenaPlayerOf(black)
                    } else {
                        logger.debug("player $black not added to the heap")
                    }
                }
            }
            get() {
                return super.outcome
            }

    }
}