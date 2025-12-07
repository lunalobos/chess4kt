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
import io.github.lunalobos.chess4kt.Piece.*
import io.github.lunalobos.chess4kt.Square.*
import kotlin.test.assertEquals

class KingGeneratorTest {

    @Test
    fun e1() {
        val bitboards = bitboardsOf(
            WK to E1,
            WR to H1,
            WR to A1,
            BK to E8,
            BR to D8
        )
        val expected = Bitboard.fromSquares(G1, E2, F2, F1)
        val actual = Bitboard(
            kingMoves(
                E1.ordinal,
                WK.ordinal,
                Bitboard.fromSquares(E8, D8).value,
                Bitboard.fromSquares(H1, A1, E1).value,
                false,
                bitboards,
                true,
                1L,
                1L,
                0L,
                0L
            ).allMovesBitboard
        )
        assertEquals(expected, actual)
    }
}