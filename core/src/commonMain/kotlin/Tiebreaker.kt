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
 * Internal contract for defining tournament tie-breaking strategies.
 * Implementing classes must define how to calculate a specific score for a player.
 * The interface automatically handles the creation of a descending Comparator
 * based on that score.
 *
 * @author lunalobos
 * @since 1.0.0-beta.8
 */
interface Tiebreaker {

    /**
     *
     */
    val name: String

    /**
     * A [Comparator] that ranks players in descending order (highest score first).
     * It uses the value returned by [getValue] to compare two [Player]s.
     * The negative sign ensures that players with higher tie-breaker values
     * are sorted to the top of the list.
     */
    val comparator: Comparator<Player>
        get() = Comparator { p1, p2 -> -getValue(p1).compareTo(getValue(p2)) }

    /**
     * Calculates the specific tie-breaker value for a given [player].
     * @param player The participant whose performance is being evaluated.
     * @return A [Score] representing the calculated tie-breaker points.
     */
    fun getValue(player: Player): Score
}