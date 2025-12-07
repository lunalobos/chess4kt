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


class QueenGeneratorTest {
    @Test
    fun d1(){
        val bitboards = bitboardsOf(
            WQ to D1,
            WK to E1,
            WN to B1,
            BK to E8,
            BP to G4
        )
        val expected = Bitboard.fromSquares(D2, D3, D4, D5, D6, D7, D8, C1, E2, F3, G4, C2, B3, A4)
        val actual = Bitboard(
            queenMoves(
                bitboards[WQ.ordinal - 1],
                D1.ordinal,
                WQ.ordinal,
                E1.ordinal,
                Bitboard.fromSquares(E1, B1).value,
                Bitboard.fromSquares(E8, G4).value,
                0L,
                -1L
            ).moves
        )
        assertEquals(expected, actual)
    }
}