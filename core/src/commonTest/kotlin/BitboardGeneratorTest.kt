/*
 * Copyright 2025 Miguel Angel Luna Lobos
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

import kotlin.test.Test
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals
import io.github.lunalobos.chess4kt.Piece.*
import io.github.lunalobos.chess4kt.Square.*
import kotlin.test.assertFalse

class BitboardGeneratorTest {

    companion object {
        internal val logger = getLogger("io.github.lunalobos.chess4kt.BitboardGeneratorTest")
    }

    @Test
    fun positions() {
        arrayOf(
            positionsJson1,
            positionsJson2,
            positionsJson3,
            positionsJson4,
            positionsJson5,
            positionsJson6,
            positionsJson7,
            positionsJson8,
            positionsJson9,
            positionsJson10
        ).flatMap { Json.decodeFromString<List<Pos>>(it) }.also { logger.debug("positions array length ${it.size}") }
            .asSequence().map { posToBasicPos(it) }.forEach { (bitboards, wm, ep, wk, wq, bk, bq, moves) ->
                val mi = movesInfo(bitboards, wm, wk, wq, bk, bq, ep)
                val actual = mi.movesList
                assertEquals(
                    moves.toSet(), actual.toSet(), "position ${fen(bitboards, wm, ep, wk, wq, bk, bq)}"
                )
            }
    }

    fun fen(
        bitboards: LongArray, wm: Boolean, ep: Int, wk: Boolean, wq: Boolean, bk: Boolean, bq: Boolean
    ): String {
        return toFen(
            toSquares(bitboards), wm, wk, wq, bk, bq, ep, 0, 1
        )
    }

    @Test
    fun checkInfo() {
        val bitboards = bitboardsOf(
            WP to A2,
            WP to B6,
            WP to C5,
            WP to E2,
            WP to F2,
            WP to H4,
            WN to A6,
            WN to F3,
            WB to C1,
            WB to F1,
            WR to A1,
            WR to H1,
            WQ to B3,
            WK to E1,
            BP to B7,
            BP to D4,
            BP to E4,
            BP to G5,
            BN to H6,
            BB to E5,
            BB to G6,
            BR to C6,
            BQ to E6,
            BK to G8
        )

        val (inCheck, inCheckMask) = checkInfo(
            BK.ordinal, bitboards, false, blackPawnMatrix2[G8.ordinal]
        )

        assertFalse(inCheck)

        assertEquals(
            Bitboard(-1L).toSquares().toSet(),
            Bitboard(inCheckMask).toSquares().toSet()
        )
    }

    @Test
    fun movesInfo(){
        val bitboards = bitboardsOf(
            WP to A2,
            WP to B6,
            WP to C5,
            WP to E2,
            WP to F2,
            WP to H4,
            WN to A6,
            WN to F3,
            WB to C1,
            WB to F1,
            WR to A1,
            WR to H1,
            WQ to B3,
            WK to E1,
            BP to B7,
            BP to D4,
            BP to E4,
            BP to G5,
            BN to H6,
            BB to E5,
            BB to G6,
            BR to C6,
            BQ to E6,
            BK to G8
        )
        val wm = false
        val wk = true
        val wq = false
        val bk = false
        val bq = false
        val ep = H4.ordinal
        val mi = movesInfo(
            bitboards,
            wm,
            wk,
            wq,
            bk,
            bq,
            ep
        )

        val moves = arrayOf(
            "d4d3",
            "e4e3",
            "e4f3",
            "g5h4",
            "g5g4",
            "b7a6",
            "h6g4",
            "h6f5",
            "h6f7",
            "g6f5",
            "g6h5",
            "g6h7",
            "g6f7",
            "g6e8",
            "e5f4",
            "e5g3",
            "e5h2",
            "e5f6",
            "e5g7",
            "e5h8",
            "e5d6",
            "e5c7",
            "e5b8",
            "c6b6",
            "c6c5",
            "c6c7",
            "c6c8",
            "c6d6",
            "e6f7",
            "e6d5",
            "e6c4",
            "e6b3",
            "g8f7",
            "g8f8",
            "g8g7",
            "g8h7",
            "g8h8"
        ).map { moveOf(it) }.toSet()

        assertEquals(moves, mi.movesList.toSet())
    }
}

