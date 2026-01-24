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
 * This enum represents the types of move notation supported by this library.
 * This enum is used as an argument in functions related to move validation and generation logic.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
enum class Notation {
    /**
     * The long algebraic notation used in the Universal Chess Interface (UCI) protocol (e.g., e2e4, a7a8q).
     */
    UCI,

    /**
     * The Standard Algebraic Notation (SAN) used in books and PGN files (e.g., Nf3, O-O, cxd4).
     */
    SAN
}