package io.github.lunalobos.chess4kt.js

import kotlin.test.Test
import kotlin.test.assertEquals

class VisibleSquaresTest {

    @OptIn(ExperimentalJsCollectionsApi::class)
    @Test
    fun queen() {
        val expected = Bitboard.fromSquares(E2, F3, G4, H5)
        val game = analysisGame { null }
        val position = game.root.appendMove("e4").appendMove("e5").position
        val actual = visibleSquares("wq", "d1", position)
        assertEquals(expected, actual)
    }
}