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

internal fun yieldComputeVisible():
            (square: Int, directionsIndexes: IntArray, directions: Array<IntArray>, friends: Long, enemies: Long) -> Long {
    val opts = longArrayOf(0L, 1L, 3L, 7L, 15L, 31L, 63L, 127L)
    val visibleOptions = Array(64) { arrayOfNulls<LongArray>(8) }
    val trailingZeros = IntArray(256)
    trailingZeros[0] = 7
    for (i in 1..127) {
        trailingZeros[i] = i.countTrailingZeroBits()
    }
    for (square in 0..63) {
        for (directionIndex in 0..7) {
            val direction: IntArray = queenMegamatrix[square][directionIndex]
            val options = LongArray(8)
            for (optionIndex in 0..7) {
                val image = opts[optionIndex]
                var visible = 0L
                var counter = 0
                for (sq in direction) {
                    visible = visible or (((image and (1L shl counter)) ushr counter) shl sq)
                    counter++
                }
                options[optionIndex] = visible
            }
            visibleOptions[square][directionIndex] = options
        }
    }
    val getVisible = { square: Int, index: Int, direction: IntArray, friends: Long, enemies: Long ->
        // space transformation: board -> direction
        var fimage = 0
        var eimage = 0
        var counter = 0
        for (sq in direction) {
            fimage = fimage or (((friends and (1L shl sq)) ushr sq) shl counter).toInt()
            eimage = eimage or (((enemies and (1L shl sq)) ushr sq) shl (counter + 1)).toInt()
            counter++
        }
        // image for direction space with bit population always <= 7
        val image = (fimage or eimage) and 127
        // trailing zeros count and visible bitboard selection
        visibleOptions[square][index]!![trailingZeros[image]]
    }
    return { square: Int, directionsIndexes: IntArray, directions: Array<IntArray>, friends: Long, enemies: Long ->
        var moves = 0L
        for (index in directionsIndexes) {
            moves = (moves
                    or getVisible(square, index, directions[index], friends, enemies))
        }
        moves
    }
}

internal fun yieldVisibleSquaresWhitePawn(
    wpcm: LongArray
): (square: Int, friends: Long) -> Long {
    return { square: Int, friends: Long ->
        wpcm[square] and friends.inv()
    }
}


internal fun yieldVisibleSquaresBlackPawn(
    bpcm: LongArray
): (square: Int, friends: Long) -> Long {
    return { square: Int, friends: Long ->
        bpcm[square] and friends.inv()
    }
}

internal fun yieldVisibleSquaresKnight(nmm: LongArray): (square: Int, friends: Long) -> Long {
    return { square: Int, friends: Long ->
        nmm[square] and friends.inv()
    }
}

internal inline fun yieldVisibleSquaresBishopAlternative(
    crossinline computeVisible: (square: Int, directionsIndexes: IntArray, directions: Array<IntArray>, friends: Long, enemies: Long) -> Long
): (square: Int, friends: Long, enemies: Long) -> Long {
    return { square: Int, friends: Long, enemies: Long ->
        computeVisible(
            square,
            bishopDirections,
            queenMegamatrix[square],
            friends,
            enemies
        )
    }
}

internal inline fun yieldVisibleSquaresRookAlternative(
    crossinline computeVisible: (square: Int, directionsIndexes: IntArray, directions: Array<IntArray>, friends: Long, enemies: Long) -> Long
): (square: Int, friends: Long, enemies: Long) -> Long {
    return { square: Int, friends: Long, enemies: Long ->
        computeVisible(
            square,
            rookDirections,
            queenMegamatrix[square],
            friends,
            enemies
        )
    }
}

internal inline fun yieldVisibleSquaresQueen(
    crossinline visibleSquaresBishop: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresRook: (square: Int, friends: Long, enemies: Long) -> Long
): (square: Int, friends: Long, enemies: Long) -> Long {
    return { square: Int, friends: Long, enemies: Long ->
        val bishopVisible = visibleSquaresBishop(square, friends, enemies)
        val rookVisible = visibleSquaresRook(square, friends, enemies)
        bishopVisible or rookVisible
    }
}

internal fun yieldVisibleSquaresKing(kmm: LongArray): (square: Int, friends: Long) -> Long{
    return { square: Int, friends: Long ->
        kmm[square] and friends.inv()
    }
}

internal inline fun yieldCalculators(
    crossinline visibleSquaresWhitePawn: (square: Int, friends: Long) -> Long,
    crossinline visibleSquaresBlackPawn: (square: Int, friends: Long) -> Long,
    crossinline visibleSquaresKnight: (square: Int, friends: Long) -> Long,
    crossinline visibleSquaresBishop: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresRook: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresQueen: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresKing: (square: Int, friends: Long) -> Long
): Array<(sq: Int, f: Long, e: Long) -> Long> {
    return arrayOf(
        { _: Int, _: Long, _: Long -> throw IllegalArgumentException("piece must be between 1 and 12") },
        { sq: Int, f: Long, _: Long -> visibleSquaresWhitePawn(sq, f) },
        { sq: Int, f: Long, _: Long -> visibleSquaresKnight(sq, f) },
        { sq: Int, f: Long, e: Long -> visibleSquaresBishop(sq, f, e) },
        { sq: Int, f: Long, e: Long -> visibleSquaresRook(sq, f, e) },
        { sq: Int, f: Long, e: Long -> visibleSquaresQueen(sq, f, e) },
        { sq: Int, f: Long, _: Long -> visibleSquaresKing(sq, f) },
        { sq: Int, f: Long, _: Long -> visibleSquaresBlackPawn(sq, f) },
        { sq: Int, f: Long, _: Long -> visibleSquaresKnight(sq, f) },
        { sq: Int, f: Long, e: Long -> visibleSquaresBishop(sq, f, e) },
        { sq: Int, f: Long, e: Long -> visibleSquaresRook(sq, f, e) },
        { sq: Int, f: Long, e: Long -> visibleSquaresQueen(sq, f, e) },
        { sq: Int, f: Long, _: Long -> visibleSquaresKing(sq, f) }
    )
}

internal fun yieldImmediateThreats(
    calculators: Array<(sq: Int, f: Long, e: Long) -> Long>
): (bitboards: LongArray, friends: Long, enemies: Long) -> Long {
    return { bitboards: LongArray, friends: Long, enemies: Long ->
        var enemiesCopy = enemies
        var enemiesVisible = 0L
        while (enemiesCopy != 0L) {
            val bitboard: Long = enemiesCopy.takeLowestOneBit()
            enemiesCopy = enemiesCopy and bitboard.inv()
            val square: Int = bitboard.countTrailingZeroBits()
            var piece = 0
            for (j in 1..12) {
                piece += j * (((1L shl square) and bitboards[j - 1]) ushr square).toInt()
            }
            enemiesVisible = enemiesVisible or calculators[piece](square, enemies, friends)
        }
        enemiesVisible
    }
}

internal inline fun yieldThreats(
    crossinline visibleSquaresKnight: (square: Int, friends: Long) -> Long,
    crossinline visibleSquaresBishop: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresRook: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresQueen: (square: Int, friends: Long, enemies: Long) -> Long,
    crossinline visibleSquaresKing: (square: Int, friends: Long) -> Long,
    calculators: Array<(sq: Int, f: Long, e: Long) -> Long>
): (bitboards: LongArray, friends: Long, enemies: Long, kingSquare: Int, wm: Boolean) -> Long {
    return { bitboards: LongArray, friends: Long, enemies: Long, kingSquare: Int, wm: Boolean ->
        var enemiesVisible = 0L
        val visibleKing = visibleSquaresKing(kingSquare, friends)
        val effectiveFriends = friends or (visibleKing and enemies)
        val effectiveEnemies = enemies and (visibleKing and enemies).inv()
        val enemyPawn = 6 * (if (wm) 1 else 0)
        val pawnFunction = calculators[enemyPawn + 1]
        var pawnBitboard = bitboards[enemyPawn]
        while (pawnBitboard != 0L) {
            val bitboard = pawnBitboard and -pawnBitboard
            pawnBitboard = pawnBitboard and bitboard.inv()
            val square = bitboard.countTrailingZeroBits()
            enemiesVisible = enemiesVisible or pawnFunction(square, effectiveEnemies, effectiveFriends)
        }
        var knightBitboard = bitboards[enemyPawn + 1]
        while (knightBitboard != 0L) {
            val bitboard = knightBitboard and -knightBitboard
            knightBitboard = knightBitboard and bitboard.inv()
            val square = bitboard.countTrailingZeroBits()
            enemiesVisible = enemiesVisible or visibleSquaresKnight(square, effectiveEnemies)
        }
        var bishopBitboard = bitboards[enemyPawn + 2]
        while (bishopBitboard != 0L) {
            val bitboard = bishopBitboard and -bishopBitboard
            bishopBitboard = bishopBitboard and bitboard.inv()
            val square = bitboard.countTrailingZeroBits()
            enemiesVisible = enemiesVisible or visibleSquaresBishop(square, effectiveEnemies, effectiveFriends)
        }
        var rookBitboard = bitboards[enemyPawn + 3]
        while (rookBitboard != 0L) {
            val bitboard = rookBitboard and -rookBitboard
            rookBitboard = rookBitboard and bitboard.inv()
            val square = bitboard.countTrailingZeroBits()
            enemiesVisible = enemiesVisible or visibleSquaresRook(square, effectiveEnemies, effectiveFriends)
        }
        var queenBitboard = bitboards[enemyPawn + 4]
        while (queenBitboard != 0L) {
            val bitboard = queenBitboard and -queenBitboard
            queenBitboard = queenBitboard and bitboard.inv()
            val square = bitboard.countTrailingZeroBits()
            enemiesVisible = enemiesVisible or visibleSquaresQueen(square, effectiveEnemies, effectiveFriends)
        }
        var kingBitboard = bitboards[enemyPawn + 5]
        while (kingBitboard != 0L) {
            val bitboard = kingBitboard and -kingBitboard
            kingBitboard = kingBitboard and bitboard.inv()
            val square = bitboard.countTrailingZeroBits()
            enemiesVisible = enemiesVisible or visibleSquaresKing(square, effectiveEnemies)
        }
        enemiesVisible
    }
}

internal fun yieldVisibleSquares(
    computeVisible: (square: Int, directionsIndexes: IntArray, directions: Array<IntArray>, friends: Long, enemies: Long) -> Long
): (bits: LongArray, directionsIndexes: IntArray, square: Int, wm: Boolean) -> Long
{

    return { bits: LongArray, directionsIndexes: IntArray, square: Int, wm: Boolean ->
        val black = bits[6] or bits[7] or bits[8] or bits[9] or bits[10] or bits[11]
        val white = bits[0] or bits[1] or bits[2] or bits[3] or bits[4] or bits[5]
        val friends: Long
        val enemies: Long
        if (wm) {
            friends = white
            enemies = black
        } else {
            friends = black
            enemies = white
        }
        computeVisible(
            square,
            directionsIndexes,
            queenMegamatrix[square],
            friends,
            enemies
        )
    }
}