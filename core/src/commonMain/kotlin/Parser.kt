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

import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

internal class Parser(val tokens: List<Token>) {
    internal companion object {
        private val logger = getLogger("io.github.lunalobos.chess4kt.Parser")
    }

    private var currentToken: Token
    private var currentTags = mutableMapOf<String, String>()
    private var currentGame: Game? = null
    private val iterator = tokens.iterator()

    private val games = mutableListOf<Game>()

    init {
        currentToken = if (iterator.hasNext()) iterator.next() else throw ParserException("no tokens to parse")
    }

    fun parseGames(idSupplier: () -> Any? = { null }): List<Game> {
        do {
            parseGame(idSupplier)
            currentToken = nextTokenOrFail("error in main function ")
        } while (currentToken.type != TokenType.EOF)
        return games
    }

    private fun parseGame(idSupplier: () -> Any?) {
        if (currentToken.type != TokenType.LBRACKET) {
            throw logger.error(ParserException(malformedPgn()))
        }
        while (currentToken.type == TokenType.LBRACKET) {
            parseTag()
        }
        currentGame = customGame(
            Game.GameMode.ANALYSIS,
            Game.ThreeRepetitionsMode.AWARE,
            Game.FiftyMovesRuleMode.AWARE,
            null,
            currentTags,
            idSupplier
        )
        parseMoves(currentGame?.root ?: throw logger.error(ParserException("current game can't be null")))
        if (currentToken.type == TokenType.RESULT) {
            currentGame?.apply {
                result = when (currentToken.value) {
                    "1-0" -> Game.Result.WHITE_WIN
                    "0-1" -> Game.Result.BLACK_WIN
                    "1/2-1/2" -> Game.Result.DRAW
                    else -> throw logger.error(ParserException(malformedPgn()))
                }
            }
        } else if (currentToken.type == TokenType.STAR) {
            currentGame?.apply {
                result = null
            }
        }

        currentGame?.let {
            it.updateEco()
            games.add(it)
        }

        reset()
    }

    private fun reset() {
        currentGame = null
        currentTags = mutableMapOf()
    }

    private fun malformedPgn(): String {
        return "malformed pgn at line ${currentToken.line} token ${currentToken.value}"
    }

    private fun nextTokenOrFail(failMsg: String): Token {
        return if (iterator.hasNext()) iterator.next() else throw logger.error(ParserException(failMsg))
    }

    private fun malformedTag(): String {
        return "malformed tag at line ${currentToken.line} token ${currentToken.value}"
    }

    private fun parseTag() {
        currentToken = nextTokenOrFail(malformedTag())
        if (currentToken.type != TokenType.SYMBOL) {
            throw logger.error(ParserException(malformedTag()))
        }
        var tagName = currentToken.value.lowercase()
        if(tagName == "eco"){
            tagName = "ECO"
        }
        currentToken = nextTokenOrFail(malformedTag())
        if (currentToken.type != TokenType.STRING) {
            throw logger.error(ParserException(malformedTag()))
        }
        val tagValue = currentToken.value
        currentTags[tagName] = tagValue
        currentToken = nextTokenOrFail(malformedTag())
        if (currentToken.type == TokenType.RBRACKET) {
            currentToken = nextTokenOrFail(malformedTag())
            return
        } else {
            throw logger.error(ParserException(malformedTag()))
        }
    }

    private fun malformedMoveText(): String {
        return "malformed move text at line ${currentToken.line} token ${currentToken}"
    }

    @OptIn(ExperimentalTime::class)
    private fun parseMoves(node: Game.Node): Game.Node {

        var move: String? = null
        var initialComment: String? = null
        var comment: String? = null
        var endLineComment: String? = null
        val suffixAnnotations: MutableList<Int> = mutableListOf()
        if (currentToken.type == TokenType.LPAREN) {
            currentToken = nextTokenOrFail(malformedMoveText())
            parseMoves(node.parent ?: throw logger.error(ParserException("node ${node.move} has no parent")))
        }
        while (move == null) {
            var tokenCounter = 0
            when (currentToken.type) {
                TokenType.INTEGER -> {}
                TokenType.PERIOD -> {}
                TokenType.COMMENT -> {
                    initialComment = currentToken.value
                }

                TokenType.SYMBOL -> {
                    move = currentToken.value
                }

                else -> throw logger.error(ParserException(malformedMoveText()))
            }

            currentToken = nextTokenOrFail(malformedMoveText())
            tokenCounter++
            if (tokenCounter > 10) {
                throw logger.error(ParserException(malformedMoveText()))
            }
        }

        while (
            currentToken.type != TokenType.INTEGER &&
            currentToken.type != TokenType.SYMBOL &&
            currentToken.type != TokenType.RESULT &&
            currentToken.type != TokenType.STAR
        ) {
            when (currentToken.type) {
                TokenType.COMMENT -> {
                    comment = currentToken.value
                }

                TokenType.END_LINE_COMMENT -> {
                    endLineComment = currentToken.value
                }

                TokenType.NAG -> {
                    suffixAnnotations.add(currentToken.value.toInt())
                }

                TokenType.LPAREN -> {
                    break;
                }

                TokenType.RPAREN -> {
                    break
                }

                else -> throw logger.error(ParserException(malformedMoveText()))
            }
            currentToken = nextTokenOrFail(malformedMoveText())
        }

        val nextNode = node.appendMove(
            move,
            initialComment,
            comment,
            endLineComment,
            suffixAnnotations.ifEmpty { null }
        )

        return when (currentToken.type) {
            TokenType.INTEGER -> parseMoves(nextNode)
            TokenType.SYMBOL -> parseMoves(nextNode)
            TokenType.LPAREN -> {
                currentToken = nextTokenOrFail(malformedMoveText())
                parseMoves(node)
                parseMoves(nextNode)
            }

            TokenType.RPAREN -> {
                currentToken = nextTokenOrFail(malformedMoveText())
                return nextNode
            }

            TokenType.RESULT -> nextNode
            TokenType.STAR -> nextNode
            else -> throw logger.error(ParserException(malformedMoveText()))
        }
    }

    internal class ParserException(msg: String) : RuntimeException(msg)
}

