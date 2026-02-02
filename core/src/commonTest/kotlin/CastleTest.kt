package io.github.lunalobos.chess4kt

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CastleTest {

    @Test
    fun whiteCantCastle() {
        val position = positionOf("r1bqkb1r/pppp1ppp/2n2n2/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4")
        val move = moveOf("e1g1")
        val positionAfterCastle = position.move(move)
        println(positionAfterCastle.bitboards.toList())
        assertFalse(positionAfterCastle.whiteCastleKingside)
        assertFalse(positionAfterCastle.whiteCastleQueenside)
    }

    @Test
    fun castleInfo() {
        val bitboards = longArrayOf(
            268496640L,
            2097154L,
            8589934596L,
            33L,
            8L,
            64L,
            67272588153323520L,
            39582418599936L,
            2594073385365405696L,
            -9151314442816847872L,
            576460752303423488L,
            1152921504606846976L
        )

        val castleInfo = CastleInfo(wk = true, wq = true, bk = true, bq = true)
        castleInfo.applyCastleRules(bitboards, true)
        assertFalse(castleInfo.wk)
        assertFalse(castleInfo.wq)
        assertTrue(castleInfo.bk)
        assertTrue(castleInfo.bq)
    }
}