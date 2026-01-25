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
 * This enum represents the squares on the chessboard. The order is set such that when calling [ordinal],
 * the integer 0 corresponds to A1, 1 corresponds to B1, and so on, following the file-major order
 * (A1, B1, C1... H1, A2, B2... H8), with 63 corresponding to H8.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 *
 */
enum class Square {
    A1, B1, C1, D1, E1, F1, G1, H1,
    A2, B2, C2, D2, E2, F2, G2, H2,
    A3, B3, C3, D3, E3, F3, G3, H3,
    A4, B4, C4, D4, E4, F4, G4, H4,
    A5, B5, C5, D5, E5, F5, G5, H5,
    A6, B6, C6, D6, E6, F6, G6, H6,
    A7, B7, C7, D7, E7, F7, G7, H7,
    A8, B8, C8, D8, E8, F8, G8, H8;

    /**
     * Returns the zero-based index of the column (file) for this square.
     * The index starts at 0 for the A-file and ends at 7 for the H-file.
     *
     * Note: Requires an external function [getCol] to be defined.
     *
     * @return The column index (0-7).
     *
     * @since 1.0.0-beta.1
     */
    fun getCol(): Int{
        return getCol(this.ordinal)
    }

    /**
     * Returns the zero-based index of the row (rank) for this square.
     * The index starts at 0 for the 1st rank and ends at 7 for the 8th rank.
     *
     * Note: Requires an external function [getRow] to be defined.
     *
     * @return The row index (0-7).
     *
     * @since 1.0.0-beta.1
     */
    fun getRow(): Int{
        return getRow(this.ordinal)
    }

    companion object{
        /**
         * Returns the [Square] enum constant corresponding to the given zero-based index (0-63).
         * This uses the standard file-major ordering (A1=0, H8=63).
         *
         * @param index The zero-based index of the square.
         * @return The corresponding [Square] enum constant.
         * @throws IndexOutOfBoundsException if the index is outside the 0-63 range.
         *
         * @since 1.0.0-beta.1
         */
        fun get(index: Int):Square {
            return entries[index];
        }
    }
}