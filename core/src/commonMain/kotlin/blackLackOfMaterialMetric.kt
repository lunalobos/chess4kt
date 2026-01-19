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