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

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi

class SwissTournamentTest {
    companion object {
        private val logger = getLogger("TournamentTest")
        private val mocker = MockGenerator()
    }

    @Test
    fun randomTournament() {
        val tournament = SwissTournament(EloCalculator())
        tournament.playersComparator = tiebreakerComparatorOf("sonnebornBerger", "fidePerformance", "buchholz")
        val tiebreakers = listOf("sonnebornBerger", "fidePerformance", "buchholz").map { tiebreakerOf(it) }
        mocker.chessPlayers(13, 1800, 2200)
            .map { (name, elo) -> Player(name, elo) }
            .forEach { tournament.addPlayer(it) }
        val firstRound = tournament.nextRound()
        assertEquals(13 / 2 + 1, firstRound.size)
        firstRound.filterIsInstance<MockMatch>().forEach {
            assertEquals(it.white.score, scoreOf("1.0"))
        }
        assertEquals(scoreOf("1.0"), tournament.leaderboard.first().score)
        firstRound.filter { it.black != null }.forEach {
            assertEquals(Outcome.IN_GAME, it.outcome)
            runGame(it)
        }

        while (!tournament.completed) {
            val round = tournament.nextRound()
            round.filter { it.black != null }.forEach {
                assertEquals(Outcome.IN_GAME, it.outcome)
                runGame(it)
            }
        }
        logger.warn(
            "\n" + tournament.leaderboard.map {
                "${it.name} (${it.currentElo}) - Score: ${it.score} - Games: ${it.matches.size} - ${
                    tiebreakerScoreOf(
                        tiebreakers,
                        it
                    )
                }"
            }.joinToString("\n")
        )
        assertTrue { tournament.finishedMatches.isNotEmpty() }
        assertTrue { tournament.completed }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun tiebreakersSorting() {
        val nicholas = Player("Nicholas Taylor", 2098)
        val harrison = Player("Harrison Stanton", 1781)
        val luke = Player("Luke White", 1280)
        val thomas = Player("Thomas Rodriguez", 1766)
        val xavier = Player("Xavier Stokes", 1538)
        val lachlan = Player("Lachlan Johnston", 1882)
        val matthew = Player("Matthew Tromp", 1559)
        val noah = Player("Noah Garden", 1681)
        val jayden = Player("Jayden Swift", 1549)
        val cooper = Player("Cooper Johnson", 1519)
        val players = listOf(
            nicholas,
            harrison,
            luke,
            thomas,
            xavier,
            lachlan,
            matthew,
            noah,
            jayden,
            cooper,
        )
        val comparator = tiebreakerComparatorOf("sonnebornBerger", "progressive", "fidePerformance", "buchholz")
        val tiebreakers =
            listOf("sonnebornBerger", "progressive", "fidePerformance", "buchholz").map { tiebreakerOf(it) }

        val firstRoundResults = listOf(
            matchOf(nicholas, cooper).apply { this.outcome = Outcome.WW },
            matchOf(jayden, noah).apply { this.outcome = Outcome.DRAW },
            matchOf(matthew, lachlan).apply { this.outcome = Outcome.BW },
            matchOf(xavier, thomas).apply { this.outcome = Outcome.BW },
            matchOf(luke, harrison).apply { this.outcome = Outcome.BW },
        )

        val secondRoundResults = listOf(
            matchOf(harrison, thomas).apply { this.outcome = Outcome.WW },
            matchOf(lachlan, nicholas).apply { this.outcome = Outcome.BW },
            matchOf(noah, cooper).apply { this.outcome = Outcome.WW },
            matchOf(jayden, xavier).apply { this.outcome = Outcome.DRAW },
            matchOf(luke, matthew).apply { this.outcome = Outcome.BW },
        )

        val thirdRoundResults = listOf(
            matchOf(harrison, nicholas).apply { this.outcome = Outcome.BW },
            matchOf(noah, thomas).apply { this.outcome = Outcome.BW },
            matchOf(lachlan, jayden).apply { this.outcome = Outcome.WW },
            matchOf(matthew, xavier).apply { this.outcome = Outcome.WW },
            matchOf(cooper, luke).apply { this.outcome = Outcome.WW },
        )
        val naturalComparator = Comparator<Player> { p1, p2 -> p2.score.compareTo(p1.score) }
        val heap = Heap(10, naturalComparator.thenComparator { a, b -> comparator.compare(a, b) })
        players.forEach { heap += it }
        val leaderboard = mutableListOf<Player>()
        while (heap.isNotEmpty()) {
            leaderboard.add(heap.pop()!!)
        }
        logger.debug("\n" + leaderboard.joinToString("\n") {
            "${it.name} (${it.currentElo}) - Score: ${it.score} - Games: ${it.matches.size} - ${
                tiebreakerScoreOf(
                    tiebreakers,
                    it
                )
            }"
        })
        assertEquals(10, leaderboard.size)
        assertEquals(nicholas.name, leaderboard[0].name)
        assertEquals(lachlan.name, leaderboard[1].name)
        assertEquals(harrison.name, leaderboard[2].name)
        assertEquals(thomas.name, leaderboard[3].name)
        assertEquals(matthew.name, leaderboard[4].name)
        assertEquals(noah.name, leaderboard[5].name)
        assertEquals(jayden.name, leaderboard[6].name)
        assertEquals(cooper.name, leaderboard[7].name)
        assertEquals(xavier.name, leaderboard[8].name)
        assertEquals(luke.name, leaderboard[9].name)
    }

    fun tiebreakerScoreOf(tiebreakers: List<Tiebreaker>, player: Player): String {
        return tiebreakers
            .map { "${it.name}: ${it.getValue(player)}" }
            .joinToString(" - ")
    }

    fun runGame(game: Match) {
        val ratio = (game.white!!.currentElo.toDouble() - game.black!!.currentElo.toDouble()) / 100.0
        if (game.white!!.currentElo >= game.black!!.currentElo) {
            val randomDouble = Random.nextDouble()
            if (randomDouble < ratio) {
                game.outcome = Outcome.WW
            } else if (randomDouble == ratio) {
                game.outcome = Outcome.DRAW
            } else {
                game.outcome = Outcome.BW
            }
        } else {
            val randomDouble = Random.nextDouble()
            if (randomDouble < -ratio) {
                game.outcome = Outcome.BW
            } else if (randomDouble == -ratio) {
                game.outcome = Outcome.DRAW
            } else {
                game.outcome = Outcome.WW
            }
        }
    }
}