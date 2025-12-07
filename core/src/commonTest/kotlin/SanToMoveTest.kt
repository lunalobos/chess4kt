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

import io.github.lunalobos.chess4kt.Square.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SanToMoveTest {

    @Test
    fun pawnMoves() {
        val position = positionOf()

        // e4 (Pawn push 2 squares)
        var result = sanToMove(position, "e4")
        assertEquals(E2.ordinal, result.origin, "Origin for e4 must be E2")
        assertEquals(E4.ordinal, result.target, "Target for e4 must be E4")

        // d3 (Pawn push 1 square)
        result = sanToMove(position, "d3")
        assertEquals(D2.ordinal, result.origin, "Origin for d3 must be D2")
        assertEquals(D3.ordinal, result.target, "Target for d3 must be D3")
    }

    @Test
    fun knightMoves() {
        val position = positionOf()

        // Nf3 (Development)
        val result = sanToMove(position, "Nf3")
        assertEquals(G1.ordinal, result.origin, "Origin for Nf3 must be G1")
        assertEquals(F3.ordinal, result.target, "Target for Nf3 must be F3")

        // Na3 (Less common development)
        val result2 = sanToMove(position, "Na3")
        assertEquals(B1.ordinal, result2.origin, "Origin for Na3 must be B1")
        assertEquals(A3.ordinal, result2.target, "Target for Na3 must be A3")
    }

    @Test
    fun castleMoves() {
        val position = positionOf("r2q1rk1/ppp2ppp/2npbn2/2b1p3/2B1P3/2NP1N2/PPPBQPPP/R3K2R w KQ - 4 8") //TODO

        // O-O (King-side)
        var result = sanToMove(position, "O-O")
        assertEquals(E1.ordinal, result.origin, "Origin for O-O must be E1")
        assertEquals(G1.ordinal, result.target, "Target for O-O must be G1")

        // O-O-O (Queen-side)
        result = sanToMove(position, "O-O-O")
        assertEquals(E1.ordinal, result.origin, "Origin for O-O-O must be E1")
        assertEquals(C1.ordinal, result.target, "Target for O-O-O must be C1")
    }

    @Test
    fun bxc5() {
        val sanMove = "bxc5"
        val position = positionOf("rn2r1k1/5b1p/p1q5/2b5/1P2pQ2/P3P3/1B3PPP/3R2K1 w - - 0 28")
        val uciMove = sanToMove(position, sanMove)
        val expectedUCIMove = "b4c5"
        assertEquals(expectedUCIMove, uciMove.toString())
    }

    @Test
    fun e8q() {
        val sanMove = "e8=Q"
        val position = Position("8/1k1KP3/8/8/8/8/8/8 w - - 0 0")
        val uciMove = sanToMove(position, sanMove)
        val expectedUCIMove = "e7e8q"
        assertEquals(expectedUCIMove, uciMove.toString())
    }
}