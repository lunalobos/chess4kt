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
package io.github.lunalobos.chess4kt.js


/**
 * This enum represents the squares on the chessboard. The order is set such that when calling [ordinal],
 * the integer 0 corresponds to A1, 1 corresponds to B1, and so on, following the file-major order
 * (A1, B1, C1... H1, A2, B2... H8), with 63 corresponding to H8.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Square private constructor(val ordinal: Int, val name: String) {
    companion object {
        val entries = io.github.lunalobos.chess4kt.Square.entries.map { Square(it.ordinal, it.name) }
        val A1 = entries[0]
        val B1 = entries[1]
        val C1 = entries[2]
        val D1 = entries[3]
        val E1 = entries[4]
        val F1 = entries[5]
        val G1 = entries[6]
        val H1 = entries[7]
        val A2 = entries[8]
        val B2 = entries[9]
        val C2 = entries[10]
        val D2 = entries[11]
        val E2 = entries[12]
        val F2 = entries[13]
        val G2 = entries[14]
        val H2 = entries[15]
        val A3 = entries[16]
        val B3 = entries[17]
        val C3 = entries[18]
        val D3 = entries[19]
        val E3 = entries[20]
        val F3 = entries[21]
        val G3 = entries[22]
        val H3 = entries[23]
        val A4 = entries[24]
        val B4 = entries[25]
        val C4 = entries[26]
        val D4 = entries[27]
        val E4 = entries[28]
        val F4 = entries[29]
        val G4 = entries[30]
        val H4 = entries[31]
        val A5 = entries[32]
        val B5 = entries[33]
        val C5 = entries[34]
        val D5 = entries[35]
        val E5 = entries[36]
        val F5 = entries[37]
        val G5 = entries[38]
        val H5 = entries[39]
        val A6 = entries[40]
        val B6 = entries[41]
        val C6 = entries[42]
        val D6 = entries[43]
        val E6 = entries[44]
        val F6 = entries[45]
        val G6 = entries[46]
        val H6 = entries[47]
        val A7 = entries[48]
        val B7 = entries[49]
        val C7 = entries[50]
        val D7 = entries[51]
        val E7 = entries[52]
        val F7 = entries[53]
        val G7 = entries[54]
        val H7 = entries[55]
        val A8 = entries[56]
        val B8 = entries[57]
        val C8 = entries[58]
        val D8 = entries[59]
        val E8 = entries[60]
        val F8 = entries[61]
        val G8 = entries[62]
        val H8 = entries[63]

        private val map =
            io.github.lunalobos.chess4kt.Square.entries.associate { it.name to Square(it.ordinal, it.name) }
        fun get(name: String) = map[name]
        fun indexToSquare(index: Int) = entries[index]
    }

    /**
     * Returns the zero-based index of the column (file) for this square.
     * The index starts at 0 for the A-file and ends at 7 for the H-file.
     *
     * Note: Requires an external function [getCol] to be defined.
     *
     */
    fun getCol(): Int {
        return io.github.lunalobos.chess4kt.getCol(this.ordinal)
    }

    /**
     * Returns the zero-based index of the row (rank) for this square.
     * The index starts at 0 for the 1st rank and ends at 7 for the 8th rank.
     *
     * Note: Requires an external function [getRow] to be defined.
     */
    fun getRow(): Int{
        return io.github.lunalobos.chess4kt.getRow(this.ordinal)
    }
}