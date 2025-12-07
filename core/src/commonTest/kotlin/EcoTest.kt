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

class EcoTest {

    @Test
    fun movesD14(){
        val ecoInfo = ecoInfo("d4 d5 c4 c6 Nf3 Nf6 cxd5 cxd5 Nc3 Nc6 Bf4 Bf5 e3 e6 Qb3 Bb4")
        assertEquals("D14", ecoInfo?.eco)
    }

    @Test
    fun positionD14(){
        val position = positionOf("r2qk2r/pp3ppp/2n1pn2/3p1b2/1b1P1B2/1QN1PN2/PP3PPP/R3KB1R w KQkq - 2 9")
        val ecoInfo = ecoInfo(position)
        assertEquals("D14", ecoInfo?.eco)
    }

    @Test
    fun movesA06(){
        val ecoInfo = ecoInfo("Nf3 d5 b3 c5 e4 dxe4 Ne5")
        assertEquals("A06", ecoInfo?.eco)
    }
}