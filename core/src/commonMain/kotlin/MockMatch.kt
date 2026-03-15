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
 * Represents a "Bye" or a mock pairing where a player has no opponent.
 * In tournament logic, this occurs when there is an odd number of participants.
 * The [white] player is awarded the [outcome] (usually a win) without playing a game.
 *
 * @property outcome The predetermined result of this mock match.
 * @property white The player who is receiving the bye.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
internal class MockMatch(
    override var outcome: Outcome,
    override val white: Player,
) : Match {

    /** Always null, as a mock match represents a round without an opponent. */
    override val black = null

    /**
     * Provides a descriptive status of the bye.
     * @return A string explaining that the player has no match.
     */
    override fun toString(): String {
        return "Player ${white.name} has no match and wins a point by default."
    }
}