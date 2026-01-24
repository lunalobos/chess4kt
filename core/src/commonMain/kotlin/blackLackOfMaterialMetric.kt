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
    pieceMask[Piece.BK.ordinal] ,  // k
    pieceMask[Piece.BK.ordinal]  or pieceMask[Piece.BN.ordinal],  // kn
    pieceMask[Piece.BK.ordinal]  or pieceMask[Piece.BB.ordinal]  // kb
)

private val inverse: Array<Int> = arrayOf(1, 0)

internal fun isBlackLackOfMaterial(bitboards: LongArray): Boolean {
    val m = (Piece.BP.ordinal .. Piece.BK.ordinal).toList()
        .map { i -> bitCountFunctions[(bitboards[i - 1]).countOneBits()](pieceMask[i]) }
        .fold(0) {a, b -> a or b }
    return material.map { i -> inverse[(i xor m).sign] }
        .fold(0) { a, b -> a or b } == 1
}