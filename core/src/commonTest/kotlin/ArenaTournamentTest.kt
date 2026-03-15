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

import kotlin.collections.forEach
import kotlin.math.min
import kotlin.random.Random
import kotlin.test.Test

class ArenaTournamentTest {
    companion object {
        private val logger = getLogger("TournamentTest")
        private val mocker = MockGenerator()
    }

    @Test
    fun randomTournament() {
        val tournament = ArenaTournament()
        tournament.playersComparator = tiebreakerComparatorOf("fidePerformance", "buchholz", "sonnebornBerger")
        val tiebreakers = listOf("fidePerformance", "buchholz", "sonnebornBerger").map { tiebreakerOf(it) }
        val gamesNumber = 400
        mocker.chessPlayers(120, 1800, 2200)
            .map{ (name, elo) -> Player(name, elo) }
            .forEach { tournament.addPlayer(it) }
        logger.debug("${tournament.leaderboard}")
        logger.debug("total games ${tournament.activeMatches.size + tournament.finishedMatches.size}")
        val currentGamesHeap = Heap<Match>(50){ g1, g2 ->
            val w1 = g1.white?.matches?.size ?: Int.MAX_VALUE
            val b1 = g1.black?.matches?.size ?: Int.MAX_VALUE
            val m1 = min(w1, b1)
            val w2 = g1.white?.matches?.size ?: Int.MAX_VALUE
            val b2 = g1.black?.matches?.size ?: Int.MAX_VALUE
            val m2 = min(w2, b2)
            m1.compareTo(m2)
        }
        while ((tournament.finishedMatches.size + tournament.activeMatches.size) < gamesNumber) {
            tournament.nextRound()
            tournament.activeMatches.forEach { currentGamesHeap += it }
            logger.trace("total games ${tournament.activeMatches.size + tournament.finishedMatches.size}")
            logger.trace("${tournament.leaderboard}")
            if (currentGamesHeap.isNotEmpty()) {
                (0..currentGamesHeap.size / 4 * 3).forEach { _ ->
                    runGame(currentGamesHeap.pop()!!)
                }
            }
            currentGamesHeap.clear()
        }
        val activeMatches = ArrayDeque(tournament.activeMatches)
        activeMatches.forEach { runGame(it) }
        tournament.completed = true

        logger.debug(
            "\n" + tournament.leaderboard.map {
                "${it.name} (${it.currentElo}) - Score: ${it.score} - Games: ${it.matches.size} - ${
                    tiebreakerScoreOf(
                        tiebreakers,
                        it
                    )
                }"
            }.joinToString("\n")
        )
    }

    fun tiebreakerScoreOf(tiebreakers: List<Tiebreaker>, player: Player): String {
        return tiebreakers
            .map { "${it.name}: ${it.getValue(player)}" }
            .joinToString(" - ")
    }

    fun runGame(game: Match) {

        val ratio = (game.white!!.initialElo.toDouble() - game.black!!.initialElo.toDouble()) / 100.0
        logger.debug("ending game $game")
        if (game.white!!.initialElo >= game.black!!.initialElo) {
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