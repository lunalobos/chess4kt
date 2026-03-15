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

class HeapTournamentTest {

    @Test
    fun playersOrder() {
        val heap = Heap<Player>(10, Comparator<Player> { p1, p2 -> p1.compareTo(p2) }
            .thenComparator { a, b -> defaultTiebreakerComparator.compare(a, b) })
        val pro = Player("Pro Player", 2000).apply {
            score = Score(10)
            blackScore = 5
        }
        val lucky = Player("Lucky Player", 1500).apply {
            score = Score(4)
            blackScore = 1
        }
        val tired = Player("Tired Player", 1500).apply {
            score = Score(4)
            blackScore = 3
        }
        val beginner = Player("Beginner", 1000).apply {
            score = Score(0)
            blackScore = 0
        }
        heap += tired
        heap += pro
        heap += beginner
        heap += lucky
        assertEquals(pro, heap.pop())
        assertEquals(tired, heap.pop())
        assertEquals(lucky, heap.pop())
        assertEquals(beginner, heap.pop())
    }
}