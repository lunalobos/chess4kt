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
import io.github.lunalobos.chess4kt.Square.*
import io.github.lunalobos.chess4kt.Piece.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LackOfMaterialTest {
    @Test
    fun kvk(){
        val bitboards = bitboardsOf(WK to E1, BK to E8)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun knvkn(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WN to G1, BN to G8)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun knvk(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WN to G1)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun kvkn(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, BN to G1)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun knvkb(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WN to G1, BB to F8)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun kbvkn(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WB to F1, BN to G8)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun kbvkb(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WB to F1, BB to F8)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun kbvk(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WB to F1)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun kvkb(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, BB to F8)
        assertTrue(isLackOfMaterial(bitboards))
    }

    @Test
    fun kbbk(){
        val bitboards = bitboardsOf(WK to E1, BK to E8, WB to F1, WB to C1)
        assertFalse(isLackOfMaterial(bitboards))
    }
}