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
 * Represents a score in a tournament.
 * This class uses an internal integer [value] to represent scores in increments of 0.5.
 * For example, a [value] of 1 represents a score of 0.5, and 2 represents 1.0.
 * @property value The internal raw integer representation of the score.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
class Score internal constructor(val value: Int) : Comparable<Score> {

    /**
     * Combines this score with another and returns a new [Score].
     */
    fun addScore(other: Score): Score {
        return Score(value + other.value)
    }

    /**
     * Returns the human-readable string representation of the score.
     * Whole numbers are returned as integers (e.g., "1"),
     * while half-points include the ".5" suffix (e.g., "1.5").
     */
    override fun toString(): String {
        return if (value % 2 == 0) {
            "${(value / 2).toString()}.0"
        } else {
            "${(value / 2)}.5"
        }
    }

    /**
     * Converts the internal representation to a [Double].
     */
    fun toDouble(): Double {
        return value / 2.0
    }

    override fun hashCode(): Int = value.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Score) return false
        return value == other.value
    }

    override fun compareTo(other: Score): Int {
        return value.compareTo(other.value)
    }
}