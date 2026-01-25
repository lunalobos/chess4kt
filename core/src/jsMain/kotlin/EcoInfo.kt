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
 * A container class representing the information about a chess opening based on the
 * Encyclopedia of Chess Openings (ECO). **It is immutable.**
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class EcoInfo internal constructor(internal val backedEcoInfo: io.github.lunalobos.chess4kt.EcoInfo) {
    /**
     * The name of the opening or variation (e.g., "Nimzo-Indian, 4.e3 O-O 5.Bd3 d5 6.Nf3 c5").
     */
    val name get() = backedEcoInfo.name

    /**
     * The ECO code (e.g., "E57", "B40").
     */
    val eco get() = backedEcoInfo.eco
}