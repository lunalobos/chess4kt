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
import kotlin.test.assertTrue

class SwissTournamentTest {
    companion object {
        private val logger = getLogger("TournamentTest")
        private val mocker = MockGenerator()
    }
    @Test
    fun randomTournament() {
        val tournament = SwissTournament(EloCalculator())
        tournament.playersComparator = tiebreakerComparatorOf("fidePerformance", "buchholz", "sonnebornBerger")
        val tiebreakers = listOf("fidePerformance", "buchholz", "sonnebornBerger").map { tiebreakerOf(it) }
        mocker.chessPlayers(500, 1800, 2200)
            .map{ (name, elo) -> Player(name, elo) }
            .forEach { tournament.addPlayer(it) }
        val firstRound = tournament.nextRound()
        firstRound.forEach { runGame(it) }
        while(!tournament.completed){
            val round = tournament.nextRound()
            round.forEach { runGame(it) }
        }
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
        assertTrue { tournament.completed }
    }

    fun tiebreakerScoreOf(tiebreakers: List<Tiebreaker>, player: Player): String {
        return tiebreakers
            .map { "${it.name}: ${it.getValue(player)}" }
            .joinToString(" - ")
    }

    fun runGame( game: Match) {
        val ratio = (game.white!!.currentElo.toDouble() - game.black!!.currentElo.toDouble()) / 100.0
        if(game.white!!.currentElo >= game.black!!.currentElo){
            val randomDouble = Random.nextDouble()
            if(randomDouble < ratio){
                game.outcome = Outcome.WW
            } else if (randomDouble == ratio){
                game.outcome = Outcome.DRAW
            } else {
                game.outcome = Outcome.BW
            }
        } else {
            val randomDouble = Random.nextDouble()
            if(randomDouble < -ratio){
                game.outcome = Outcome.BW
            } else if (randomDouble == -ratio){
                game.outcome = Outcome.DRAW
            } else {
                game.outcome = Outcome.WW
            }
        }
    }
}