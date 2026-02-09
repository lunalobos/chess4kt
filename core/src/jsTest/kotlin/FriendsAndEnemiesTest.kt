package io.github.lunalobos.chess4kt.js

import kotlin.test.Test
import kotlin.test.assertEquals

class FriendsAndEnemiesTest {

    @Test
    fun friends() {
        val position = startPos();
        assertEquals(Bitboard.fromSquares(A1,B1,C1,D1,E1,F1,G1,H1,A2,B2,C2,D2,E2,F2,G2,H2), position.friends)
    }

    @Test
    fun enemies() {
        val position = startPos();
        assertEquals(Bitboard.fromSquares(A8,B8,C8,D8,E8,F8,G8,H8,A7,B7,C7,D7,E7,F7,G7,H7), position.enemies)
    }
}