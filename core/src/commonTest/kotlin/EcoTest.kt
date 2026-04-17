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
package io.github.lunalobos.chess4kt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EcoTest {

    @Test
    fun movesD14(){
        val ecoInfo = ecoInfo("d4 d5 c4 c6 Nf3 Nf6 cxd5 cxd5 Nc3 Nc6 Bf4 Bf5 e3 e6 Qb3 Bb4")
        assertEquals("D14", ecoInfo?.eco)
    }

    @Test
    fun movesA06(){
        val ecoInfo = ecoInfo("Nf3 d5 b3 c5 e4 dxe4 Ne5")
        assertEquals("A06", ecoInfo?.eco)
    }

    @Test
    fun kingsPawn(){
        val position = positionOf("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1")
        val ecoInfo = ecoInfo(position)
        assertEquals("B00", ecoInfo?.eco)
        assertEquals("King's Pawn Opening; B00", ecoInfo?.name)
    }

    @Test
    fun sicilian(){
        val position = positionOf("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2")
        val ecoInfo = ecoInfo(position)
        assertEquals("B20", ecoInfo?.eco)
        assertEquals("Sicilian Defense; B20", ecoInfo?.name)
    }

    @Test
    fun queensPawn(){
        val position = positionOf("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1")
        val ecoInfo = ecoInfo(position)
        assertEquals("A40", ecoInfo?.eco)
        assertEquals("Queen Pawn Opening; A40", ecoInfo?.name)
    }

    @Test
    fun startpos(){
        val position = positionOf()
        val ecoInfo = ecoInfo(position)
        assertNull(ecoInfo)
    }
}