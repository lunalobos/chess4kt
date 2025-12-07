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

class PawnGeneratorTest {

    @Test
    fun e4() {
        val bitboards = bitboardsOf(
            WK to E1,
            BK to E8,
            WP to E4,
            BP to D5
        )
        val expected = Bitboard.fromSquares(D5, E5).value
        val actual = pawnMoves(
            bitboards[WP.ordinal - 1],
            E4.ordinal,
            WP.ordinal,
            pawnMatrix1[1],
            pawnMatrix2[1],
            E1.ordinal,
            Bitboard.fromSquares(D5, E8).value,
            Bitboard.fromSquares(E4, E1).value,
            -1,
            true,
            bitboards,
            0L,
            -1L
        ).allMovesBitboard
        assertEquals(expected, actual)
    }

    @Test
    fun e5() {
        val bitboards = bitboardsOf(
            WK to E1,
            BK to E8,
            WP to E5,
            BP to D5
        )
        val expected = Bitboard.fromSquares(D6, E6).value
        val actual = pawnMoves(
            bitboards[WP.ordinal - 1],
            E5.ordinal,
            WP.ordinal,
            pawnMatrix1[1],
            pawnMatrix2[1],
            E1.ordinal,
            Bitboard.fromSquares(D5, E8).value,
            Bitboard.fromSquares(E5, E1).value,
            D5.ordinal,
            true,
            bitboards,
            0L,
            -1L
        ).allMovesBitboard
        assertEquals(Bitboard(expected), Bitboard(actual))
    }

    @Test
    fun e2() {
        val bitboards = bitboardsOf(
            WK to E1,
            BK to E8,
            WP to E2,
            BP to D5
        )
        val expected = Bitboard.fromSquares(E3, E4).value
        val actual = pawnMoves(
            bitboards[WP.ordinal - 1],
            E2.ordinal,
            WP.ordinal,
            pawnMatrix1[1],
            pawnMatrix2[1],
            E1.ordinal,
            Bitboard.fromSquares(D5, E8).value,
            Bitboard.fromSquares(E2, E1).value,
            -1,
            true,
            bitboards,
            0L,
            -1L
        ).allMovesBitboard
        assertEquals(Bitboard(expected), Bitboard(actual))
    }


    @Test
    fun e7() {
        val bitboards = bitboardsOf(
            WK to D7,
            BK to H8,
            WP to E7
        )
        val expected = Bitboard.fromSquares(E8).value
        val actual = pawnMoves(
            bitboards[WP.ordinal - 1],
            E7.ordinal,
            WP.ordinal,
            pawnMatrix1[1],
            pawnMatrix2[1],
            D7.ordinal,
            Bitboard.fromSquares(H8).value,
            Bitboard.fromSquares(E7, D7).value,
            -1,
            true,
            bitboards,
            0L,
            -1L
        ).allMovesBitboard
        assertEquals(Bitboard(expected), Bitboard(actual))
    }
}