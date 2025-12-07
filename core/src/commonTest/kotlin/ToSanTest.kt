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

class ToSanTest {

    @Test
    fun e2e4(){
        val e2e4 = moveOf("e2e4")
        val pos = positionOf()
        val san = toSan(pos, e2e4)
        assertEquals("e4", san)
    }

    @Test
    fun g1f3(){
        val g1f3 = moveOf("g1f3")
        val san = toSan(positionOf(), g1f3)
        assertEquals("Nf3", san)
    }

    @Test
    fun f3d4(){
        val f3d4 = moveOf("f3d4")
        val san = toSan(
            positionOf("r1bqkb1r/ppp2ppp/3p1n2/4p3/3nP3/3P1N2/PPP1NPPP/R1BQKB1R w KQkq - 0 6"),
            f3d4
        )
        assertEquals("Nfxd4", san)
    }

    @Test
    fun f3d42(){
        val f3d4 = moveOf("f3d4")
        val pos = positionOf("r1bqkb1r/ppp2ppp/3p1n2/4p3/3nP3/1N1P1N2/PPP2PPP/R1BQKB1R w KQkq - 0 6")
        val san = toSan(pos, f3d4)
        assertEquals("Nfxd4", san)
    }

    @Test
    fun f3d43(){
        val f3d4 = moveOf("f3d4")
        val pos = positionOf("r2q1rk1/ppp1bppp/3pbn2/4pN2/3nP3/3P1N2/PPP1BPPP/R1BQK2R w KQ - 0 9")
        val san = toSan(pos, f3d4)
        assertEquals("N3xd4", san)
    }

    @Test
    fun h1e1(){
        val h1e1 = moveOf("h1e1")
        val pos = positionOf("8/8/8/1k6/4Q2Q/8/8/1K5Q w - - 0 1")
        val san = toSan(pos, h1e1)
        assertEquals("Q1e1", san)
    }

    @Test
    fun h4e1(){
        val h4e1 = moveOf("h4e1")
        val pos = positionOf("8/8/8/1k6/4Q2Q/8/8/1K5Q w - - 0 1")
        val san = toSan(pos, h4e1)
        assertEquals("Qh4e1", san)
    }

    @Test
    fun e4e1(){
        val e4e1 = moveOf("e4e1")
        val pos = positionOf("8/8/8/1k6/4Q2Q/8/8/1K5Q w - - 0 1")
        val san = toSan(pos, e4e1)
        assertEquals("Qee1", san)
    }

    @Test
    fun g1e2(){
        val g1e2 = moveOf("g1e2")
        val pos = positionOf("8/8/2k5/8/8/2N3N1/8/1K4N1 w - - 0 1")
        val san = toSan(pos, g1e2)
        assertEquals("N1e2", san)
    }

    @Test
    fun g3e2(){
        val g3e2 = moveOf("g3e2")
        val pos = positionOf("8/8/2k5/8/8/2N3N1/8/1K4N1 w - - 0 1")
        val san = toSan(pos, g3e2)
        assertEquals("Ng3e2", san)
    }

    @Test
    fun c3e2(){
        val c3e2 = moveOf("c3e2")
        val pos = positionOf("8/8/2k5/8/8/2N3N1/8/1K4N1 w - - 0 1")
        val san = toSan(pos, c3e2)
        assertEquals("Nce2", san)
    }

    @Test
    fun h5f7(){
        val h5f7 = moveOf("h5f7")
        val pos = positionOf("rnbqkb1r/ppp2ppp/3p1n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 0 4")
        val san = toSan(pos, h5f7)
        assertEquals("Qxf7#", san)
    }

    @Test
    fun e1g1(){
        val e1g1 = moveOf("e1g1")
        val pos = positionOf("rnb1kb1r/ppp2ppp/4pn2/q7/2B5/2N2N2/PPPP1PPP/R1BQK2R w KQkq - 0 6")
        val san = toSan(pos, e1g1)
        assertEquals("O-O", san)
    }

    @Test
    fun f7f8q(){
        val f7f8q = moveOf("f7f8q")
        val pos = positionOf("2k5/4KP2/8/8/8/8/8/8 w - - 0 1")
        val san = toSan(pos, f7f8q)
        assertEquals("f8=Q+", san)
    }
}