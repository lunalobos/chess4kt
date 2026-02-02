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
package io.github.lunalobos.chess4kt

/**
 * A container class representing the information about a chess opening based on the
 * Encyclopedia of Chess Openings (ECO). **It is immutable.**
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
class EcoInfo internal constructor(
    /**
     * The ECO code (e.g., "E57", "B40").
     */
    val eco: String,
    /**
     * The name of the opening or variation (e.g., "Nimzo-Indian, 4.e3 O-O 5.Bd3 d5 6.Nf3 c5").
     */
    val name: String
) {

    override fun hashCode() = genericHashCode(arrayOf(eco, name))

    override fun equals(other: Any?): Boolean {
        return if (other == null) {
            false
        } else if (other === this) {
            true
        } else if (other is EcoInfo) {
            eco == other.eco && name == other.name
        } else {
            false
        }
    }

    override fun toString() = "EcoInfo(eco=\"$eco\", name=\"$name\")"
}