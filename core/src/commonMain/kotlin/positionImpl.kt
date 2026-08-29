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

import io.github.lunalobos.chess4kt.Piece.*

internal class StarterPosition : Position() {
    override val whiteMove: Boolean = true
    override val mi: MovesInfo
        get() = movesInfoGenerator.movesInfoWhite(
            bitboards, whiteCastleKingside, whiteCastleQueenside, enPassant
        )

}

internal class WhitePosition(
    bitboards: LongArray,
    enPassant: Int,
    whiteCastleKingside: Boolean,
    whiteCastleQueenside: Boolean,
    blackCastleKingside: Boolean,
    blackCastleQueenside: Boolean,
    mc: Int,
    hm: Int
) : Position(
    bitboards, enPassant, whiteCastleKingside, whiteCastleQueenside, blackCastleKingside, blackCastleQueenside, mc, hm
) {
    override val whiteMove: Boolean = true
    override val mi: MovesInfo
        get() = movesInfoGenerator.movesInfoWhite(
            bitboards, whiteCastleKingside, whiteCastleQueenside, enPassant
        )


}

internal class BlackPosition(
    bitboards: LongArray,
    enPassant: Int,
    whiteCastleKingside: Boolean,
    whiteCastleQueenside: Boolean,
    blackCastleKingside: Boolean,
    blackCastleQueenside: Boolean,
    mc: Int,
    hm: Int
) : Position(
    bitboards, enPassant, whiteCastleKingside, whiteCastleQueenside, blackCastleKingside, blackCastleQueenside, mc, hm
) {
    override val whiteMove: Boolean = false
    override val mi: MovesInfo
        get() = movesInfoGenerator.movesInfoBlack(
            bitboards, blackCastleKingside, blackCastleQueenside, enPassant
        )

}


private val emptyPossibilities = listOf('1', '2', '3', '4', '5', '6', '7', '8')


fun positionFromFenUnsafe(fen: String): Position{
    val sqrs = IntArray(64)
    val parts = fen.split(" ")
    val rows = parts[0].split("/")
    for (h in 7 downTo 0) {
        val chars = rows[7 - h].toCharArray()
        var i = 0
        for (character in chars) {
            if (character in emptyPossibilities) {
                i += character.toString().toInt()
            } else {
                when (character) {
                    'P' -> sqrs[h * 8 + i] = WP.ordinal
                    'N' -> sqrs[h * 8 + i] = WN.ordinal
                    'B' -> sqrs[h * 8 + i] = WB.ordinal
                    'R' -> sqrs[h * 8 + i] = WR.ordinal
                    'Q' -> sqrs[h * 8 + i] = WQ.ordinal
                    'K' -> sqrs[h * 8 + i] = WK.ordinal
                    'p' -> sqrs[h * 8 + i] = BP.ordinal
                    'n' -> sqrs[h * 8 + i] = BN.ordinal
                    'b' -> sqrs[h * 8 + i] = BB.ordinal
                    'r' -> sqrs[h * 8 + i] = BR.ordinal
                    'q' -> sqrs[h * 8 + i] = BQ.ordinal
                    'k' -> sqrs[h * 8 + i] = BK.ordinal
                    else -> IllegalArgumentException("invalid character $character in fen string")
                }
                i++
            }
        }
    }

    // squares to bitboards
    val bitboards = LongArray(12)
    for (i in 0..63) {
        if (sqrs[i] > 0) {
            bitboards[sqrs[i] - 1] = bitboards[sqrs[i] - 1] or (1L shl i)
        }
    }


    // side to move
    val whiteMove = parts[1] == "w"

    // castle rights
    val castlePart = parts[2]
    var wk = false
    var bk = false
    var wq = false
    var bq = false
    if (castlePart != "-") {
        val chars = castlePart.toCharArray()
        for (character in chars) {
            when (character) {
                'K' -> wk = true
                'k' -> bk = true
                'Q' -> wq = true
                'q' -> bq = true
            }
        }
    }

    val enPassant: Int
    // en passant
    val enPassantPart = parts[3]
    if (enPassantPart == "-") {
        enPassant = -1
    } else {
        val chars = enPassantPart.toCharArray()
        val col = "" + chars[0]
        val x = getColIndex(col)
        val y = if (("" + chars[1]).toInt() == 6) 4 else if (("" + chars[1]).toInt() == 3) 3 else
            throw IllegalArgumentException("invalid en passant string")
        enPassant = getSquareIndex(x, y)
    }

    // half moves
    val halfMovesCounter = parts[4].toInt()
    require(halfMovesCounter in 0..50) { "Half moves has to be over -1 and bellow 51." }

    // moves counter
    val movesCounter = parts[5].toInt()
    if(whiteMove){
        return WhitePosition(
            bitboards = bitboards,
            enPassant = enPassant,
            whiteCastleKingside = wk,
            whiteCastleQueenside = wq,
            blackCastleKingside = bk,
            blackCastleQueenside = bq,
            mc = movesCounter,
            hm = halfMovesCounter,
        )
    } else {
        return BlackPosition(
            bitboards = bitboards,
            enPassant = enPassant,
            whiteCastleKingside = wk,
            whiteCastleQueenside = wq,
            blackCastleKingside = bk,
            blackCastleQueenside = bq,
            mc = movesCounter,
            hm = halfMovesCounter,
        )
    }
}