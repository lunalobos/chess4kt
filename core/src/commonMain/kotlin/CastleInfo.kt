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

internal data class CastleInfo (
    var wk: Boolean,
    var wq: Boolean,
    var bk: Boolean,
    var bq: Boolean
) {
    fun applyCastleRules(bitboards: LongArray, wm: Boolean): CastleInfo {
        val scMask: LongArray
        val lcMask: LongArray
        val scSquares: IntArray
        val lcSquares: IntArray
        val rookBits: Long
        val kingBits: Long
        if(wm){
            scMask = castleMask[1][0]
            lcMask = castleMask[1][1]
            scSquares = castleSquares[1][0]
            lcSquares = castleSquares[1][1]
            rookBits = bitboards[Piece.WR.ordinal - 1]
            kingBits = bitboards[Piece.WK.ordinal - 1]
        } else {
            scMask = castleMask[0][0]
            lcMask = castleMask[0][1]
            scSquares = castleSquares[0][0]
            lcSquares = castleSquares[0][1]
            rookBits = bitboards[Piece.BR.ordinal - 1]
            kingBits = bitboards[Piece.BK.ordinal - 1]
        }
        val scBitsMasked = ((rookBits and scMask[1]) ushr scSquares[1]) and ((kingBits and scMask[0]) ushr scSquares[0])
        val lcBitsMasked = ((rookBits and lcMask[1]) ushr lcSquares[1]) and ((kingBits and lcMask[0]) ushr lcSquares[0])
        if(wm){
            wk = isPresent(scBitsMasked) && wk
            wq = isPresent(lcBitsMasked) && wq
        } else {
            bk = isPresent(scBitsMasked) && bk
            bq = isPresent(lcBitsMasked) && bq
        }
        return this
    }
}