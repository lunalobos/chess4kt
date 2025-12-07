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
import kotlin.test.assertTrue

internal class BitboardTest {
    companion object{
        val logger = getLogger("io.github.lunalobos.chess4kt.BitboardTest")
    }

    //@Test
    fun helloBitboard(){
        val bitboard = Bitboard.fromSquares(E4,D4)
        logger.debug(bitboard.toString())
        assertTrue(true)
    }
}