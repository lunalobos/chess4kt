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
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import io.github.lunalobos.chess4kt.Square.*
import io.github.lunalobos.chess4kt.Piece.*

class CheckmateMetricsTest {
    @Test
    fun checkmate(){
        val bitboards = bitboardsOf(WK to E1, BK to E3, BQ to H1)
        val wm = true
        val legalMoves = 0L
        assertTrue(isCheckmate(bitboards, wm, legalMoves))
    }

    @Test
    fun noCheckmate(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, BQ to E4)
        val wm = true
        val legalMoves = Bitboard.fromSquares(F1, F2, D1, D2).value
        assertFalse(isCheckmate(bitboards, wm, legalMoves))
    }
}