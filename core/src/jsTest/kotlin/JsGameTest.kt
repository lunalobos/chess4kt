package io.github.lunalobos.chess4kt.js

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsGameTest {
    @Test
    fun tagsTest(){
        val game = strictMatch { "someId" }
        game.setTag("event", "foobared event")
        game.setTag("white", "foo")
        game.setTag("black", "bar")
        println(game.tags)
        assertEquals("foobared event", game.tags["event"])
        assertEquals("foo", game.tags["white"])
        assertEquals("bar", game.tags["black"])
    }
}