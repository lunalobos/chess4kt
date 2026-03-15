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
 * Represents the final or current result of a tournament match.
 * Each outcome defines the points awarded to both the white and black players,
 * as well as a standard string representation used for display.
 * @property whiteScore The [Score] assigned to the white player.
 * @property blackScore The [Score] assigned to the black player.
 * @property representation The official notation for the result (e.g., "1-0").
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
enum class Outcome(
    val whiteScore: Score,
    val blackScore: Score,
    val representation: String
) {
    /** White Wins: 1 point for White, 0 for Black. */
    WW(scoreOf("1.0"), scoreOf("0.0"), "1-0"),

    /** Black Wins: 0 points for White, 1 for Black. */
    BW(scoreOf("0.0"), scoreOf("1.0"), "0-1"),

    /** Draw: 0.5 points for both players. */
    DRAW(scoreOf("0.5"), scoreOf("0.5"), "1/2-1/2"),

    /** The match was suspended; no points are awarded. */
    SUSPENDED(scoreOf("0.0"), scoreOf("0.0"), "suspended"),

    /** The match is ongoing; points remain at 0.0 until finished. */
    IN_GAME(scoreOf("0.0"), scoreOf("0.0"), "in game");

    /** Returns the standard chess notation of the result. */
    override fun toString(): String = representation
}