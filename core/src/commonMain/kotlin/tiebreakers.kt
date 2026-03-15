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

import kotlin.math.min
import kotlin.math.round

private class BlackGames() : Tiebreaker {
    override val name = "Black Games"
    override fun getValue(player: Player): Score {
        return Score(player.blackScore * 2)
    }
}

private class Buchholz : Tiebreaker {
    override val name = "Buchholz"
    private fun buchholz(player: Player): Score {
        val heap = Heap<Player>(10) { p1, p2 -> p1.score.compareTo(p2.score) }
        player.against.forEach { heap += it }
        val deque = ArrayDeque<Player>()
        deque += heap.toList()
        if (deque.isNotEmpty()) {
            deque.removeFirst()
        }
        if (deque.isNotEmpty()) {
            deque.removeLast()
        }
        return deque.fold(scoreOf("0.0")) { ac, curr -> ac.addScore(curr.score) }
    }

    override fun getValue(player: Player): Score {
        return buchholz(player)
    }
}

private class SonnebornBerger : Tiebreaker {
    override val name = "Sonneborn Berger"
    private fun sonnebornBerger(player: Player): Score {
        return player.matches
            .map { (it as RatedMatch).sonnebornBergerPartialScore(player) }
            .fold(scoreOf("0.0")) { ac, curr -> ac.addScore(curr) }
    }

    override fun getValue(player: Player): Score {
        return sonnebornBerger(player)
    }
}

private class Progressive : Tiebreaker {
    override val name = "Progressive"
    private fun progressive(player: Player): Score {
        return player.roundScores.fold(scoreOf("0.0")) { ac, curr -> ac.addScore(curr) }
    }

    override fun getValue(player: Player): Score {
        return progressive(player)
    }
}

private class FidePerformance : Tiebreaker {
    override val name = "Fide Performance"
    private fun fidePerformance(player: Player): Score {
        val dp = arrayOf(
            0, 7, 14, 21, 29, 36, 43, 50, 57, 65, 72, 80, 87, 95, 102, 110, 117, 125, 133, 141, 149,
            158, 166, 175, 184, 193, 202, 211, 220, 230, 240, 251, 262, 273, 284, 296, 309, 322, 336, 351, 366, 383,
            401, 422, 444, 470, 501, 538, 589, 677, 800
        )
        val percentage = if (player.against.isEmpty()) {
            0.0
        } else {
            round(50.0 * player.score.toDouble() / player.against.size.toDouble())
        }
        var average = player.against.map { p -> p.currentElo }.average()
        if (average.isNaN()) {
            average = 0.0
        }
        if (percentage >= 50.0) {
            return Score(
                round(
                    average + dp[min(
                        percentage.toInt() - 50,
                        dp.size - 1
                    )].toDouble()
                ).toInt() * 2
            )
        } else {
            return Score(
                round(
                    average - dp[min(
                        50 - percentage.toInt(),
                        dp.size - 1
                    )].toDouble()
                ).toInt() * 2
            )
        }
    }

    override fun getValue(player: Player): Score {
        return fidePerformance(player)
    }
}

private class LinearPerformance : Tiebreaker {

    override val name = "Linear Performance"
    private fun linearPerformance(player: Player): Score {
        var average = player.against.map { p -> p.currentElo }.average()
        if (average.isNaN()) {
            average = 0.0
        }
        return Score(round(average + player.score.toDouble() * 400.0 - 400.0).toInt() * 2)
    }

    override fun getValue(player: Player): Score {
        return linearPerformance(player)
    }
}

/**
 * Factory function that maps a string identifier to a concrete [Tiebreaker] strategy.
 *
 * This is used internally to build composite comparators from configuration strings.
 *
 * @param name The unique identifier for the tie-breaker.
 * Must be one of: "blackGames", "progressive", "sonnebornBerger",
 * "fidePerformance", "linearPerformance", or "buchholz".
 * @return An instance of the requested [Tiebreaker].
 * @throws IllegalStateException if the provided [name] does not match any supported tie-breaker.
 */
fun tiebreakerOf(name: String): Tiebreaker {
    return when (name) {
        "blackGames" -> BlackGames()
        "progressive" -> Progressive()
        "sonnebornBerger" -> SonnebornBerger()
        "fidePerformance" -> FidePerformance()
        "linearPerformance" -> LinearPerformance()
        "buchholz" -> Buchholz()
        else -> error("unsupported tiebreaker $name")
    }
}

/**
 * Factory function that creates a composite [Comparator] for [Player]s
 * based on a prioritized list of tie-breaker criteria.
 * The function chains the comparators in the order provided. If the first tie-breaker
 * results in a tie, it moves to the second, and so on.
 *
 * ### Supported Tie-breaker Names:
 * | Name | Logic | Description |
 * | :--- | :--- | :--- |
 * | **"blackGames"** | `blackScore` | Prioritizes players who played more games with Black pieces. |
 * | **"buchholz"** | Median-Buchholz | Sum of opponents' scores, excluding the highest and lowest (outliers). |
 * | **"sonnebornBerger"** | Quality of Win | Sum of defeated opponents' scores + 50% of drawn opponents' scores. |
 * | **"progressive"** | Cumulative Sum | Sum of the player's total score after each individual round. |
 * | **"fidePerformance"** | FIDE Rp | Performance Rating based on FIDE's dp table and opponents' average Elo. |
 * | **"linearPerformance"** | Linear Rp | Simplified performance: `AvgElo + (Score * 400) - 400`. |
 *
 * @param names A variable list of strings matching the names above (case-insensitive).
 * @return A [Comparator] used to rank players beyond their primary match score.
 * @throws IllegalStateException if a name is provided that does not match the list above.
 * @sample
 * // Ranks players primarily by FIDE Performance, then by Buchholz.
 * val comparator = tiebreakerComparatorOf("fidePerformance", "buchholz")
 * playerList.sortWith(comparator)
 */
fun tiebreakerComparatorOf(vararg names: String): Comparator<Player> {
    return names.asSequence()
        .map { tiebreakerOf(it).comparator }
        .reduce { ac, curr -> ac.thenComparator { a, b -> -curr.compare(a, b) } }
}

internal val defaultTiebreakerComparator =
    tiebreakerComparatorOf("fidePerformance", "buchholz", "progressive", "sonnebornBerger")