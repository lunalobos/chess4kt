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


import kotlin.test.Ignore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

/*
 * These unit tests are too expensive and slow to run every time, especially in environments like GitHub Actions,
 * which is why they are marked to be ignored. However, they are executed before pushing each version since they
 * are crucial for detecting bugs.
 */
class GeneratorTest {
    companion object {
        internal val logger = getLogger("io.github.lunalobos.chess4kt.GeneratorTest")
    }

    @Ignore
    @OptIn(ExperimentalTime::class)
    @Test
    fun position1() {
        val position = positionOf()
        val d1 = now()
        assertEquals(20, generationTest(1, position))
        assertEquals(400, generationTest(2, position))
        assertEquals(8902, generationTest(3, position))
        assertEquals(197281, generationTest(4, position))
        assertEquals(4865609, generationTest(5, position))
        val d2 = now()
        logger.debug("GeneratorTest-Position${1} time[ms]: ${d2.toEpochMilliseconds() - d1.toEpochMilliseconds()}")
    }

    @Ignore
    @OptIn(ExperimentalTime::class)
    @Test
    fun numberOfPositions2() {
        val position = positionOf("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1")
        val d1 = now()
        assertEquals(48, generationTest(1, position))
        assertEquals(2039, generationTest(2, position))
        assertEquals(97862, generationTest(3, position))
        assertEquals(4085603, generationTest(4, position))
        assertEquals(193690690, generationTest(5, position))
        val d2 = now()
        logger.debug("GeneratorTest-Position${2} time[ms]: ${d2.toEpochMilliseconds() - d1.toEpochMilliseconds()}")
    }

    @Ignore
    @OptIn(ExperimentalTime::class)
    @Test
    fun numberOfPositions3() {
        val position = positionOf("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1")
        val d1 = now()
        assertEquals(14, generationTest(1, position))
        assertEquals(191, generationTest(2, position))
        assertEquals(2812, generationTest(3, position))
        assertEquals(43238, generationTest(4, position))
        assertEquals(674624, generationTest(5, position))
        assertEquals(11030083, generationTest(6, position))
        val d2 = now()
        logger.debug("GeneratorTest-Position${3} time[ms]: ${d2.toEpochMilliseconds() - d1.toEpochMilliseconds()}")
    }

    @Ignore
    @OptIn(ExperimentalTime::class)
    @Test
    fun numberOfPositions4() {
        val position = positionOf("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1")
        val d1 = now()
        assertEquals(6, generationTest(1, position))
        assertEquals(264, generationTest(2, position))
        assertEquals(9467, generationTest(3, position))
        assertEquals(422333, generationTest(4, position))
        assertEquals(15833292, generationTest(5, position))
        val d2 = now()
        logger.debug("GeneratorTest-Position${4} time[ms]: ${d2.toEpochMilliseconds() - d1.toEpochMilliseconds()}")
    }

    @Ignore
    @OptIn(ExperimentalTime::class)
    @Test
    fun numberOfPositions5() {
        val position = positionOf("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8")
        val d1 = now()
        assertEquals(44, generationTest(1, position))
        assertEquals(1486, generationTest(2, position))
        assertEquals(62379, generationTest(3, position))
        assertEquals(2103487, generationTest(4, position))
        assertEquals(89941194, generationTest(5, position))
        val d2 = now()
        logger.debug("GeneratorTest-Position${5} time[ms]: ${d2.toEpochMilliseconds() - d1.toEpochMilliseconds()}")
    }

    @Ignore
    @OptIn(ExperimentalTime::class)
    @Test
    fun numberOfPositions6() {
        val position = positionOf("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10")
        val d1 = now()
        assertEquals(46, generationTest(1, position))
        assertEquals(2079, generationTest(2, position))
        assertEquals(89890, generationTest(3, position))
        assertEquals(3894594, generationTest(4, position))
        assertEquals(164075551, generationTest(5, position))
        val d2 = now()
        logger.debug("GeneratorTest-Position${6} time[ms]: ${d2.toEpochMilliseconds() - d1.toEpochMilliseconds()}")
    }


    private fun generationTest(depth: Int, position: Position): Int {
        if (depth == 0) return 1
        val children = position.children
        var totalNodes = 0
        for ((pos, _) in children) {
            val nodesInSubtree = generationTest(depth - 1, pos)
            totalNodes += nodesInSubtree
        }
        return totalNodes
    }
}