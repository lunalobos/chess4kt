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

import io.github.lunalobos.chess4kt.js.Square.Companion.entries_


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
        @OptIn(ExperimentalJsCollectionsApi::class)
        val entries = io.github.lunalobos.chess4kt.Square.entries.map { Square(it.ordinal, it.name) }
            .asJsReadonlyArrayView()
        internal val entries_ = io.github.lunalobos.chess4kt.Square.entries.map { Square(it.ordinal, it.name) }

        private val map =
            io.github.lunalobos.chess4kt.Square.entries.associate { it.name to Square(it.ordinal, it.name) }
        fun get(name: String) = map[name]
        fun indexToSquare(index: Int) = entries_[index]
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

@OptIn(ExperimentalJsExport::class)
@JsExport
val A1 = entries_[0]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B1 = entries_[1]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C1 = entries_[2]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D1 = entries_[3]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E1 = entries_[4]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F1 = entries_[5]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G1 = entries_[6]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H1 = entries_[7]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A2 = entries_[8]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B2 = entries_[9]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C2 = entries_[10]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D2 = entries_[11]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E2 = entries_[12]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F2 = entries_[13]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G2 = entries_[14]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H2 = entries_[15]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A3 = entries_[16]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B3 = entries_[17]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C3 = entries_[18]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D3 = entries_[19]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E3 = entries_[20]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F3 = entries_[21]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G3 = entries_[22]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H3 = entries_[23]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A4 = entries_[24]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B4 = entries_[25]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C4 = entries_[26]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D4 = entries_[27]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E4 = entries_[28]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F4 = entries_[29]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G4 = entries_[30]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H4 = entries_[31]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A5 = entries_[32]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B5 = entries_[33]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C5 = entries_[34]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D5 = entries_[35]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E5 = entries_[36]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F5 = entries_[37]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G5 = entries_[38]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H5 = entries_[39]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A6 = entries_[40]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B6 = entries_[41]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C6 = entries_[42]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D6 = entries_[43]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E6 = entries_[44]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F6 = entries_[45]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G6 = entries_[46]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H6 = entries_[47]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A7 = entries_[48]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B7 = entries_[49]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C7 = entries_[50]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D7 = entries_[51]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E7 = entries_[52]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F7 = entries_[53]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G7 = entries_[54]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H7 = entries_[55]

@OptIn(ExperimentalJsExport::class)
@JsExport
val A8 = entries_[56]

@OptIn(ExperimentalJsExport::class)
@JsExport
val B8 = entries_[57]

@OptIn(ExperimentalJsExport::class)
@JsExport
val C8 = entries_[58]

@OptIn(ExperimentalJsExport::class)
@JsExport
val D8 = entries_[59]

@OptIn(ExperimentalJsExport::class)
@JsExport
val E8 = entries_[60]

@OptIn(ExperimentalJsExport::class)
@JsExport
val F8 = entries_[61]

@OptIn(ExperimentalJsExport::class)
@JsExport
val G8 = entries_[62]

@OptIn(ExperimentalJsExport::class)
@JsExport
val H8 = entries_[63]