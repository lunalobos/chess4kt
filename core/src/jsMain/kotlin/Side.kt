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
package io.github.lunalobos.chess4kt.js

/**
 * This enum represents the two sides (colors) in a chess game: White or Black.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Side private constructor(val name: String) {
    companion object {
        val entries = io.github.lunalobos.chess4kt.Side.entries.map { Side(it.name) }
        /**
         * White Player
         */
        val WHITE = entries[0]
        /**
         * Black Player
         */
        val BLACK = entries[1]
        private val map = io.github.lunalobos.chess4kt.Side.entries.associate { it.name to Side(it.name) }
        fun get(name: String) = map[name]
    }
}