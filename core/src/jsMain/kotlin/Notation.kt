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

import io.github.lunalobos.chess4kt.js.Notation.Companion.get

/**
 * This class represents the types of move notation supported by this library.
 * This class is used as an argument in functions related to move validation and generation logic.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Notation internal constructor(val name: String) {
    companion object {
        @OptIn(ExperimentalJsCollectionsApi::class)
        val entries = io.github.lunalobos.chess4kt.Notation.entries.map { Notation(it.name) }.asJsReadonlyArrayView()


        private val map = io.github.lunalobos.chess4kt.Notation.entries.associate { it.name to Notation(it.name) }
        fun get(name: String) = map[name]
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
/**
 * The long algebraic notation used in the Universal Chess Interface (UCI) protocol (e.g., e2e4, a7a8q).
 */
val UCI = get("UCI")!!

@OptIn(ExperimentalJsExport::class)
@JsExport
/**
 * The Standard Algebraic Notation (SAN) used in books and PGN files (e.g., Nf3, O-O, cxd4).
 */
val SAN = get("SAN")!!