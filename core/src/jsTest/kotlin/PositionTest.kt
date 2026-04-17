package io.github.lunalobos.chess4kt.js

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class PositionTest {

    @Test
    fun factory(){
        try {
            val position = positionOf("rnrnbqkbnr/pp2pppp/3p4/8/3NP3/8/PPP2PPP/RNBQKB1R b KQkq - 0 4")
        } catch(e: Throwable){
            assertTrue(e.message?.endsWith("has more than 8 characters") ?: false)
        }
    }
}