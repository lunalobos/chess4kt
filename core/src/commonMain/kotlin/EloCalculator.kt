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

import kotlin.math.pow
import kotlin.math.round

/**
 * Calculates and updates player ratings based on the Elo rating system.
 * This class uses a logistic distribution to predict the outcome of a match between
 * two players and adjusts their ratings based on the difference between the
 * actual and expected results.
 *
 * @property impactFactor The K-factor that determines how much a single match
 * affects the rating. A higher value leads to faster rating changes. Default is 32.0.
 * @property rangeFactor The scale factor used to determine win probability.
 * Default is 400.0.
 * @property logisticBase The base of the exponent in the logistic function.
 * Standard Elo uses base 10.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
open class EloCalculator(
    var impactFactor: Double = 32.0,
    var rangeFactor: Double = 400.0,
    var logisticBase: Double = 10.0,
) {

    /**
     * Calculates the new Elo ratings for both participants and updates their
     * [Player.currentElo] properties directly.
     *
     * @param white The player playing as white.
     * @param black The player playing as black.
     * @param outcome The result of the match (e.g., White Win, Black Win, or Draw).
     */
    fun calculate(white: Player, black: Player, outcome: Outcome) {
        val wExponent = (black.currentElo - white.currentElo).toDouble() / rangeFactor
        val wExpected = 1 / (1 + logisticBase.pow(wExponent))
        val bExponent = (white.currentElo - black.currentElo).toDouble() / rangeFactor
        val bExpected = 1 / (1 + logisticBase.pow(bExponent))
        white.currentElo = round(white.currentElo + impactFactor * (outcome.whiteScore.value.toDouble() - wExpected)).toInt()
        black.currentElo = round(black.currentElo + impactFactor * (outcome.blackScore.value.toDouble() - bExpected)).toInt()
    }
}