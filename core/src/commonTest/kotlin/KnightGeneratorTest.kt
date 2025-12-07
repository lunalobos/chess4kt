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

class KnightGeneratorTest {

    @Test
    fun f3(){
        val bitboards = bitboardsOf(
            WN to F3,
            WP to E5,
            WK to E1,
            BK to E8,
            BP to D4
        )
        val expected = Bitboard.fromSquares(D4, D2, G1, H2, H4, G5)
        val actual = knightMoves(
            bitboards[WN.ordinal -1],
            F3.ordinal,
            WN.ordinal,
            Bitboard.fromSquares(E8, D4).value,
            Bitboard.fromSquares(F3, E5, E1).value,
            0L,
            -1L
        ).moves
        assertEquals(expected, Bitboard(actual))
    }
}