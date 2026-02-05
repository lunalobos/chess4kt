package io.github.lunalobos.chess4kt.js

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsNodeTest {

    @OptIn(ExperimentalJsCollectionsApi::class)
    @Test
    fun hasChildren() {
        val game = strictMatch { null }
        game.root.appendMove("e4").appendMove("e5")
        assertTrue( game.root.hasChildren())
        assertTrue( game.root.backedNode.children[0].hasChildren())
    }

    @OptIn(ExperimentalJsCollectionsApi::class)
    @Test
    fun hasNoChildren() {
        val game = strictMatch { null }
        assertFalse( game.root.hasChildren())
    }
}