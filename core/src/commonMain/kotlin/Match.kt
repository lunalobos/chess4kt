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
 * Represents a single match or pairing within a tournament.
 * This interface tracks the two participants (White and Black) and the
 * final outcome of their game.
 *
 * @since 1.0.0-beta.8
 * @author lunalobos
 */
interface Match {
    /**
     * The result of the match.
     * Usually initialized as a 'Pending' state and updated once the game is over.
     */
    var outcome: Outcome

    /**
     * The player competing with the White pieces.
     * Can be null.
     */
    val white: Player?

    /**
     * The player competing with the Black pieces.
     * Can be null.
     */
    val black: Player?
}