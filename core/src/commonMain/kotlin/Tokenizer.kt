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

internal class Tokenizer(private val pgnInput: String) {
    private var pos = 0
    private var line = 1
    private val tokens = mutableListOf<Token>()

    fun tokenize(): List<Token> {
        while (pos < pgnInput.length) {
            val char = pgnInput[pos]
            when {
                char.isWhitespace() -> consumeWhitespace()
                char == '[' -> addToken(TokenType.LBRACKET)
                char == ']' -> addToken(TokenType.RBRACKET)
                char == '(' -> addToken(TokenType.LPAREN)
                char == ')' -> addToken(TokenType.RPAREN)
                char == '.' -> addToken(TokenType.PERIOD)
                char == '*' -> addToken(TokenType.STAR)
                char == '"' -> consumeString()
                char == '{' -> consumeBraceComment()
                char == ';' -> consumeSemicolonComment()
                char == '$' -> consumeNAG()
                char.isDigit() -> consumeNumberOrSymbol()
                char.isLetter() || char == '_' || char == '+' || char == '#' || char == '=' || char == ':' || char == '-' -> consumeSymbol()
                else -> {
                    pos++
                }
            }
        }
        tokens.add(Token(TokenType.EOF, "", line))
        return tokens
    }

    // --- Private Consumption Methods (Simplified, based on PGN rules) ---
    private fun addToken(type: TokenType, value: String = "") {
        tokens.add(Token(type, value.ifEmpty { pgnInput[pos].toString() }, line))
        pos++
    }

    private fun consumeWhitespace() {
        while (pos < pgnInput.length && pgnInput[pos].isWhitespace()) {
            if (pgnInput[pos] == '\n') line++
            pos++
        }
        // Whitespace tokens are ignored by not being added to the list.
    }

    private fun consumeString() {
        val start = ++pos // Consume opening quote
        var value = ""
        while (pos < pgnInput.length) {
            val char = pgnInput[pos]
            if (char == '"') {
                value = pgnInput.substring(start, pos)
                pos++ // Consume closing quote
                tokens.add(Token(TokenType.STRING, value, line))
                return
            }
            if (char == '\n' || char == '\r' || char.code <= 31) {
                // Non-printing characters not permitted in strings
                // For simplicity, we'll allow multiline strings here but a real parser would throw.
                if (char == '\n') line++
            }
            pos++
        }
        throw Parser.ParserException("Unclosed string literal.")
    }

    // Comments are usually ignored by the parser, but tokenized here to be skipped.
    private fun consumeBraceComment() { // { ... }
        val start = pos
        pos++
        while (pos < pgnInput.length && pgnInput[pos] != '}') {
            if (pgnInput[pos] == '\n') line++
            pos++
        }
        if (pos < pgnInput.length) pos++ // Consume closing brace
        tokens.add(
            Token(
                TokenType.COMMENT,
                pgnInput.substring(start + 1, if ((pos - 1) >= start + 1) (pos - 1) else (start + 1)).trim(),
                line
            )
        )
    }

    private fun consumeSemicolonComment() { // ; rest of line
        val start = pos
        while (pos < pgnInput.length && pgnInput[pos] != '\n' && pgnInput[pos] != '\r') {
            pos++
        }
        tokens.add(Token(TokenType.END_LINE_COMMENT, pgnInput.substring(start + 1, pos - 1).trim(), line))
    }

    private fun consumeNAG() { // $d
        val start = ++pos // Consume '$'
        while (pos < pgnInput.length && pgnInput[pos].isDigit()) {
            pos++
        }
        tokens.add(Token(TokenType.NAG, pgnInput.substring(start, pos), line))
    }

    private fun consumeNumberOrSymbol() {
        val start = pos
        var currentPos = pos

        // 1. Consumir la parte numérica
        while (currentPos < pgnInput.length && pgnInput[currentPos].isDigit()) {
            currentPos++
        }

        // 2. Verificar si es una indicación de número de movimiento (ej., "1.", "20.")
        if (currentPos < pgnInput.length && pgnInput[currentPos] == '.') {
            // Es un número de movimiento: tokenizamos solo los dígitos (INTEGER)
            pos = currentPos
            tokens.add(Token(TokenType.INTEGER, pgnInput.substring(start, pos), line))
            // El '.' se tokenizará por separado en el bucle principal
        } else {
            // No es un número de movimiento (puede ser un resultado, un SAN, o un símbolo).
            // Hacemos rollback y dejamos que consumeSymbol() procese el token completo.
            pos = start
            consumeSymbol()
        }
    }


    private fun consumeSymbol() {
        val start = pos
        while (pos < pgnInput.length) {
            val char = pgnInput[pos]
            // CLAVE: Añadir '/' a los continuadores de símbolo para que "1/2-1/2" se lea como uno solo.
            val isSymbolContinuation =
                char.isLetterOrDigit() || char == '_' || char == '+' || char == '#' || char == '=' || char == ':' || char == '-' || char == '/'

            if (isSymbolContinuation) {
                pos++
            } else {
                break
            }
        }

        val value = pgnInput.substring(start, pos)

        // 3. Clasificar el valor completo
        val type = when (value) {
            "1-0", "0-1", "1/2-1/2" -> TokenType.RESULT
            else -> TokenType.SYMBOL
        }

        tokens.add(Token(type, value, line))
    }
}
