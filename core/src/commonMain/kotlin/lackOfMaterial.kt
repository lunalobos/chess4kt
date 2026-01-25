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

internal val pieceMask: Array<Int> = arrayOf<Int>(
    1 shl Piece.EMPTY.ordinal, 1 shl Piece.WP.ordinal,
    1 shl Piece.WN.ordinal, 1 shl Piece.WB.ordinal, 1 shl Piece.WR.ordinal, 1 shl Piece.WQ.ordinal,
    1 shl Piece.WK.ordinal, 1 shl Piece.BP.ordinal, 1 shl Piece.BN.ordinal, 1 shl Piece.BB.ordinal,
    1 shl Piece.BR.ordinal, 1 shl Piece.BQ.ordinal, 1 shl Piece.BK.ordinal,
)

private val material: Array<Int> = arrayOf<Int>(
    pieceMask[Piece.WK.ordinal] or pieceMask[Piece.BK.ordinal],  // K k
    (pieceMask[Piece.WN.ordinal] or pieceMask[Piece.BN.ordinal] or pieceMask[Piece.WK.ordinal]
            or pieceMask[Piece.BK.ordinal]),  // KN kn
    pieceMask[Piece.WN.ordinal] or pieceMask[Piece.WK.ordinal] or pieceMask[Piece.BK.ordinal],  // KN k
    pieceMask[Piece.BN.ordinal] or pieceMask[Piece.WK.ordinal] or pieceMask[Piece.BK.ordinal],  // K kn
    (pieceMask[Piece.WN.ordinal] or pieceMask[Piece.BB.ordinal] or pieceMask[Piece.WK.ordinal]
            or pieceMask[Piece.BK.ordinal]),  // KN kb
    (pieceMask[Piece.WB.ordinal] or pieceMask[Piece.BN.ordinal] or pieceMask[Piece.WK.ordinal]
            or pieceMask[Piece.BK.ordinal]),  // KB kn
    (pieceMask[Piece.WB.ordinal] or pieceMask[Piece.BB.ordinal] or pieceMask[Piece.WK.ordinal]
            or pieceMask[Piece.BK.ordinal]),  // KB kb
    pieceMask[Piece.WB.ordinal] or pieceMask[Piece.WK.ordinal] or pieceMask[Piece.BK.ordinal],  // KB k
    pieceMask[Piece.BB.ordinal] or pieceMask[Piece.WK.ordinal] or pieceMask[Piece.BK.ordinal] // K kb
)
internal val bitCountFunctions: Array<(Int) -> Int> = arrayOf(
    { i -> 0 },
    { i -> i },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size },
    { i -> 1 shl Piece.entries.size })
private val inverse: Array<Int> = arrayOf(1, 0)

internal fun isLackOfMaterial(bitboards: LongArray): Boolean {
    val m = (1 ..< Piece.entries.size).toList()
        .map { i -> bitCountFunctions[(bitboards[i - 1]).countOneBits()](pieceMask[i]) }
        .fold(0) {a, b -> a or b }
    return material.map { i-> i }
        .map { i -> inverse[(i xor m).sign] }
        .fold(0) { a, b -> a or b } == 1
}