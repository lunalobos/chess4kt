/*
 * Copyright 2025 Miguel Angel Luna Lobos
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

import kotlin.random.Random
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal fun yieldComputeZobristHash(): (
    bitBoards: LongArray, whiteMove: Boolean, shortCastleWhite: Boolean, longCastleWhite: Boolean,
    shortCastleBlack: Boolean, longCastleBlack: Boolean, enPassant: Int
) -> Long {
    val logger = getLogger("computeZobristHash")
    val t1 = now()
    val zobristTable = Array(12) { Array(64) { 0L } }
    val zobristCastle = Array(4) { 0L }
    val zobristEnPassant = Array(64) { 0L }
    val zobristTurn = Random.nextLong()

    for (piece in 0..<12) {
        for (square in 0..<64) {
            zobristTable[piece][square] = Random.nextLong()
        }
    }
    for (i in 0..<4) {
        zobristCastle[i] = Random.nextLong()
    }
    for (i in 0..<64) {
        zobristEnPassant[i] = Random.nextLong()
    }
    val t2 = now()
    logger.instantiation(t1, t2)
    return {
        bitBoards: LongArray, whiteMove: Boolean, shortCastleWhite: Boolean, longCastleWhite: Boolean,
        shortCastleBlack: Boolean, longCastleBlack: Boolean, enPassant: Int ->
        var zobristHash = 0L
        val bitboardsCopy = bitBoards.copyOf()
        for (piece in 0..11) {
            var bitboard = bitboardsCopy[piece]
            while (bitboard != 0L) {
                val square: Int = bitboard.countTrailingZeroBits()
                zobristHash = zobristHash xor zobristTable[piece][square]
                bitboard = bitboard and bitboard - 1
            }
        }
        if (whiteMove) zobristHash = zobristHash xor zobristTurn
        if (shortCastleWhite) zobristHash = zobristHash xor zobristCastle[0]
        if (longCastleWhite) zobristHash = zobristHash xor zobristCastle[1]
        if (shortCastleBlack) zobristHash = zobristHash xor zobristCastle[2]
        if (longCastleBlack) zobristHash = zobristHash xor zobristCastle[3]
        if (enPassant != -1) zobristHash = zobristHash xor zobristEnPassant[enPassant]
        zobristHash
    }
}