package io.github.lunalobos.chess4kt.js


import kotlin.test.Test
import kotlin.test.assertEquals

class JsGameTest {

    @OptIn(ExperimentalJsCollectionsApi::class)
    @Test
    fun e4(){
        val game = strictMatch()
        game.root.appendMove("e4")
        val ecoInfo = game.ecoInfo
        assertEquals("B00", ecoInfo?.eco)
        assertEquals("King's Pawn Opening; B00", ecoInfo?.name)
    }
}