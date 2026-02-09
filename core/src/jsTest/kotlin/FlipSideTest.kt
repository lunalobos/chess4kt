package io.github.lunalobos.chess4kt.js

import kotlin.test.Test
import kotlin.test.assertFalse

class FlipSideTest {

    @Test
    fun initialPosition() {
        val position = startPos().flipSide()
        assertFalse(position.whiteMove)
    }
}