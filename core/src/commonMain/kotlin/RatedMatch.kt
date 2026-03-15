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
 * Represents a standard competitive match between two [Player]s.
 * This class handles the automation of score accumulation, Elo rating updates,
 * and opponent tracking once an outcome is reached.
 *
 * @property white The player assigned the white pieces.
 * @property black The player assigned the black pieces.
 * @property eloCalculator The logic used to adjust ratings post-match.
 *
 * @author lunalobos
 * @since 1.0.0-beta.8
 */
open class RatedMatch(
    override val white: Player,
    override val black: Player,
    val eloCalculator: EloCalculator,
) : Match {

    /**
     * The current state/result of the match.
     * Setting this value (except to SUSPENDED) triggers:
     * 1. Mutual opponent registration.
     * 2. Incrementing black piece count for the black player.
     * 3. Elo rating recalculation.
     * 4. Score accumulation and history logging for both participants.
     */
    override var outcome: Outcome = Outcome.IN_GAME
        set(value) {
            field = value
            if (value.ordinal != Outcome.SUSPENDED.ordinal) {
                white.against.add(black)
                black.against.add(white)
                black.addAsBlackGame()
                eloCalculator.calculate(white, black, value)
                white.matches.add(this)
                black.matches.add(this)
            }
            white.score = white.score.addScore(value.whiteScore)
            black.score = black.score.addScore(value.blackScore)
            white.accumulateScore()
            black.accumulateScore()
        }

    override fun toString(): String = "$white - $black => $outcome"

    /**
     * Calculates the Sonneborn-Berger partial score for a specific [player].
     * @param player The player for whom the tie-break score is calculated.
     * @return The opponent's score (on win), half the opponent's score (on draw),
     * or 0.0 (on loss/ongoing).
     */
    fun sonnebornBergerPartialScore(player: Player): Score {
        return if (player == white && outcome != Outcome.IN_GAME) {
            when (outcome) {
                Outcome.WW -> black.score
                Outcome.DRAW -> Score(black.score.value / 2)
                else -> scoreOf("0.0")
            }
        } else if (player == black && outcome != Outcome.IN_GAME) {
            when (outcome) {
                Outcome.BW -> white.score
                Outcome.DRAW -> Score(white.score.value / 2)
                else -> scoreOf("0.0")
            }
        } else scoreOf("0.0")
    }
}