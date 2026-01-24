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

import kotlin.math.sign

private val material: Array<Int> = arrayOf(
    pieceMask[Piece.WK.ordinal] ,  // K
    pieceMask[Piece.WN.ordinal]  or pieceMask[Piece.WK.ordinal],  // KN
    pieceMask[Piece.WB.ordinal]  or pieceMask[Piece.WK.ordinal]  // KB
)

private val inverse: Array<Int> = arrayOf(1, 0)

internal fun isWhiteLackOfMaterial(bitboards: LongArray): Boolean {
    val m = (1 .. Piece.WK.ordinal).toList()
        .map { i -> bitCountFunctions[(bitboards[i - 1]).countOneBits()](pieceMask[i]) }
        .fold(0) {a, b -> a or b }
    return material.map { i -> inverse[(i xor m).sign] }
        .fold(0) { a, b -> a or b } == 1
}