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
import kotlin.test.assertEquals
import io.github.lunalobos.chess4kt.Square.*

class VisibleMetricTest {

    @Test
    fun visibleQueen() {
        val friends = Bitboard.fromSquares(D4, G1, C1).value
        val enemies = Bitboard.fromSquares(B3, G4).value
        val expected = Bitboard.fromSquares(C2, B3, E2, F3, G4, E1, F1, D2, D3).value
        val result = visibleSquaresQueen(D1.ordinal, friends, enemies)
        assertEquals(expected, result)
    }
}