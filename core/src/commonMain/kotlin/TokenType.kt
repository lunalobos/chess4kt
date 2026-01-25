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

internal enum class TokenType {
    // Delimiters (Self-terminating)
    LBRACKET,   // [
    RBRACKET,   // ]
    LPAREN,     // ( (RAV begin)
    RPAREN,     // ) (RAV end)
    PERIOD,     // . (Move number indication separator)

    // Annotations & Markers
    NAG,        // Numeric Annotation Glyph ($dd)
    STAR,       // * (Game termination marker)

    // Keywords/Results (Symbols) [cite: 195]
    RESULT,     // "1-0", "0-1", "1/2-1/2"

    // Core Data Types [cite: 101, 121]
    STRING,     // "..." (Used for Tag Values)
    SYMBOL,     // identifier (Used for Tag Names, SAN moves, and custom symbols)
    INTEGER,    // 1, 2, 3... (Used for move number indication) [cite: 106]
    COMMENT, // used for regurlar comments
    END_LINE_COMMENT, // used for end line comments

    // Special Markers
    EOF // end of file
}