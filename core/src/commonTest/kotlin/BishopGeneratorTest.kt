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

class BishopGeneratorTest {

    @Test
    fun f1(){
        val bitboards = bitboardsOf(
            WB to F1,
            WK to E1,
            WP to D3,
            BP to G2,
            BK to E8
        )
        val expected = Bitboard.fromSquares(E2, G2)
        val actual = Bitboard(bishopMoves(
            bitboards[WB.ordinal - 1],
            F1.ordinal,
            WN.ordinal,
            E1.ordinal,
            Bitboard.fromSquares(G2, E8).value,
            Bitboard.fromSquares(E1, F1, D3).value,
            0L,
            -1L
        ).moves)
        assertEquals(expected, actual)
    }
}