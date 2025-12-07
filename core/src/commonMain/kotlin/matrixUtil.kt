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


internal val blackPawnMatrix1 = arrayOf(
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(0),
    intArrayOf(1),
    intArrayOf(2),
    intArrayOf(3),
    intArrayOf(4),
    intArrayOf(5),
    intArrayOf(6),
    intArrayOf(7),
    intArrayOf(8),
    intArrayOf(9),
    intArrayOf(10),
    intArrayOf(11),
    intArrayOf(12),
    intArrayOf(13),
    intArrayOf(14),
    intArrayOf(15),
    intArrayOf(16),
    intArrayOf(17),
    intArrayOf(18),
    intArrayOf(19),
    intArrayOf(20),
    intArrayOf(21),
    intArrayOf(22),
    intArrayOf(23),
    intArrayOf(24),
    intArrayOf(25),
    intArrayOf(26),
    intArrayOf(27),
    intArrayOf(28),
    intArrayOf(29),
    intArrayOf(30),
    intArrayOf(31),
    intArrayOf(32),
    intArrayOf(33),
    intArrayOf(34),
    intArrayOf(35),
    intArrayOf(36),
    intArrayOf(37),
    intArrayOf(38),
    intArrayOf(39),
    intArrayOf(40, 32),
    intArrayOf(41, 33),
    intArrayOf(42, 34),
    intArrayOf(43, 35),
    intArrayOf(44, 36),
    intArrayOf(45, 37),
    intArrayOf(46, 38),
    intArrayOf(47, 39),
    intArrayOf(48),
    intArrayOf(49),
    intArrayOf(50),
    intArrayOf(51),
    intArrayOf(52),
    intArrayOf(53),
    intArrayOf(54),
    intArrayOf(55)
)
internal val blackPawnMatrix2 = arrayOf(
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(1),
    intArrayOf(0, 2),
    intArrayOf(1, 3),
    intArrayOf(2, 4),
    intArrayOf(3, 5),
    intArrayOf(4, 6),
    intArrayOf(5, 7),
    intArrayOf(6),
    intArrayOf(9),
    intArrayOf(8, 10),
    intArrayOf(9, 11),
    intArrayOf(10, 12),
    intArrayOf(11, 13),
    intArrayOf(12, 14),
    intArrayOf(13, 15),
    intArrayOf(14),
    intArrayOf(17),
    intArrayOf(16, 18),
    intArrayOf(17, 19),
    intArrayOf(18, 20),
    intArrayOf(19, 21),
    intArrayOf(20, 22),
    intArrayOf(21, 23),
    intArrayOf(22),
    intArrayOf(25),
    intArrayOf(24, 26),
    intArrayOf(25, 27),
    intArrayOf(26, 28),
    intArrayOf(27, 29),
    intArrayOf(28, 30),
    intArrayOf(29, 31),
    intArrayOf(30),
    intArrayOf(33),
    intArrayOf(32, 34),
    intArrayOf(33, 35),
    intArrayOf(34, 36),
    intArrayOf(35, 37),
    intArrayOf(36, 38),
    intArrayOf(37, 39),
    intArrayOf(38),
    intArrayOf(41),
    intArrayOf(40, 42),
    intArrayOf(41, 43),
    intArrayOf(42, 44),
    intArrayOf(43, 45),
    intArrayOf(44, 46),
    intArrayOf(45, 47),
    intArrayOf(46),
    intArrayOf(49),
    intArrayOf(48, 50),
    intArrayOf(49, 51),
    intArrayOf(50, 52),
    intArrayOf(51, 53),
    intArrayOf(52, 54),
    intArrayOf(53, 55),
    intArrayOf(54)
)

internal fun yieldBlackPawnCaptureMoves(): LongArray {
    return blackPawnMatrix2
        .map { it.map { sq -> 1L shl sq }.reduceOrNull { acc, element -> acc or element } ?: 0L }.toLongArray()
}

internal val kingMatrix = arrayOf(
    intArrayOf(9, 8, 1),
    intArrayOf(10, 8, 9, 2, 0),
    intArrayOf(11, 9, 10, 3, 1),
    intArrayOf(12, 10, 11, 4, 2),
    intArrayOf(13, 11, 12, 5, 3),
    intArrayOf(14, 12, 13, 6, 4),
    intArrayOf(15, 13, 14, 7, 5),
    intArrayOf(14, 15, 6),
    intArrayOf(17, 1, 16, 0, 9),
    intArrayOf(18, 16, 0, 2, 17, 1, 10, 8),
    intArrayOf(19, 17, 1, 3, 18, 2, 11, 9),
    intArrayOf(20, 18, 2, 4, 19, 3, 12, 10),
    intArrayOf(21, 19, 3, 5, 20, 4, 13, 11),
    intArrayOf(22, 20, 4, 6, 21, 5, 14, 12),
    intArrayOf(23, 21, 5, 7, 22, 6, 15, 13),
    intArrayOf(22, 6, 23, 7, 14),
    intArrayOf(25, 9, 24, 8, 17),
    intArrayOf(26, 24, 8, 10, 25, 9, 18, 16),
    intArrayOf(27, 25, 9, 11, 26, 10, 19, 17),
    intArrayOf(28, 26, 10, 12, 27, 11, 20, 18),
    intArrayOf(29, 27, 11, 13, 28, 12, 21, 19),
    intArrayOf(30, 28, 12, 14, 29, 13, 22, 20),
    intArrayOf(31, 29, 13, 15, 30, 14, 23, 21),
    intArrayOf(30, 14, 31, 15, 22),
    intArrayOf(33, 17, 32, 16, 25),
    intArrayOf(34, 32, 16, 18, 33, 17, 26, 24),
    intArrayOf(35, 33, 17, 19, 34, 18, 27, 25),
    intArrayOf(36, 34, 18, 20, 35, 19, 28, 26),
    intArrayOf(37, 35, 19, 21, 36, 20, 29, 27),
    intArrayOf(38, 36, 20, 22, 37, 21, 30, 28),
    intArrayOf(39, 37, 21, 23, 38, 22, 31, 29),
    intArrayOf(38, 22, 39, 23, 30),
    intArrayOf(41, 25, 40, 24, 33),
    intArrayOf(42, 40, 24, 26, 41, 25, 34, 32),
    intArrayOf(43, 41, 25, 27, 42, 26, 35, 33),
    intArrayOf(44, 42, 26, 28, 43, 27, 36, 34),
    intArrayOf(45, 43, 27, 29, 44, 28, 37, 35),
    intArrayOf(46, 44, 28, 30, 45, 29, 38, 36),
    intArrayOf(47, 45, 29, 31, 46, 30, 39, 37),
    intArrayOf(46, 30, 47, 31, 38),
    intArrayOf(49, 33, 48, 32, 41),
    intArrayOf(50, 48, 32, 34, 49, 33, 42, 40),
    intArrayOf(51, 49, 33, 35, 50, 34, 43, 41),
    intArrayOf(52, 50, 34, 36, 51, 35, 44, 42),
    intArrayOf(53, 51, 35, 37, 52, 36, 45, 43),
    intArrayOf(54, 52, 36, 38, 53, 37, 46, 44),
    intArrayOf(55, 53, 37, 39, 54, 38, 47, 45),
    intArrayOf(54, 38, 55, 39, 46),
    intArrayOf(57, 41, 56, 40, 49),
    intArrayOf(58, 56, 40, 42, 57, 41, 50, 48),
    intArrayOf(59, 57, 41, 43, 58, 42, 51, 49),
    intArrayOf(60, 58, 42, 44, 59, 43, 52, 50),
    intArrayOf(61, 59, 43, 45, 60, 44, 53, 51),
    intArrayOf(62, 60, 44, 46, 61, 45, 54, 52),
    intArrayOf(63, 61, 45, 47, 62, 46, 55, 53),
    intArrayOf(62, 46, 63, 47, 54),
    intArrayOf(49, 48, 57),
    intArrayOf(48, 50, 49, 58, 56),
    intArrayOf(49, 51, 50, 59, 57),
    intArrayOf(50, 52, 51, 60, 58),
    intArrayOf(51, 53, 52, 61, 59),
    intArrayOf(52, 54, 53, 62, 60),
    intArrayOf(53, 55, 54, 63, 61),
    intArrayOf(54, 55, 62)
)

internal fun yieldKingMovesMatrix(): LongArray {
    return kingMatrix
        .map { it.map { sq -> 1L shl sq }.reduceOrNull { acc, element -> acc or element } ?: 0L }.toLongArray()
}

internal val kings: IntArray = intArrayOf(Piece.BK.ordinal, Piece.WK.ordinal)
internal val knightMatrix = arrayOf(
    intArrayOf(17, 10),
    intArrayOf(18, 16, 11),
    intArrayOf(19, 17, 12, 8),
    intArrayOf(20, 18, 13, 9),
    intArrayOf(21, 19, 14, 10),
    intArrayOf(22, 20, 15, 11),
    intArrayOf(23, 21, 12),
    intArrayOf(22, 13),
    intArrayOf(25, 18, 2),
    intArrayOf(26, 24, 19, 3),
    intArrayOf(27, 25, 20, 16, 4, 0),
    intArrayOf(28, 26, 21, 17, 5, 1),
    intArrayOf(29, 27, 22, 18, 6, 2),
    intArrayOf(30, 28, 23, 19, 7, 3),
    intArrayOf(31, 29, 20, 4),
    intArrayOf(30, 21, 5),
    intArrayOf(33, 26, 1, 10),
    intArrayOf(34, 32, 27, 2, 0, 11),
    intArrayOf(35, 33, 28, 24, 3, 1, 12, 8),
    intArrayOf(36, 34, 29, 25, 4, 2, 13, 9),
    intArrayOf(37, 35, 30, 26, 5, 3, 14, 10),
    intArrayOf(38, 36, 31, 27, 6, 4, 15, 11),
    intArrayOf(39, 37, 28, 7, 5, 12),
    intArrayOf(38, 29, 6, 13),
    intArrayOf(41, 34, 9, 18),
    intArrayOf(42, 40, 35, 10, 8, 19),
    intArrayOf(43, 41, 36, 32, 11, 9, 20, 16),
    intArrayOf(44, 42, 37, 33, 12, 10, 21, 17),
    intArrayOf(45, 43, 38, 34, 13, 11, 22, 18),
    intArrayOf(46, 44, 39, 35, 14, 12, 23, 19),
    intArrayOf(47, 45, 36, 15, 13, 20),
    intArrayOf(46, 37, 14, 21),
    intArrayOf(49, 42, 17, 26),
    intArrayOf(50, 48, 43, 18, 16, 27),
    intArrayOf(51, 49, 44, 40, 19, 17, 28, 24),
    intArrayOf(52, 50, 45, 41, 20, 18, 29, 25),
    intArrayOf(53, 51, 46, 42, 21, 19, 30, 26),
    intArrayOf(54, 52, 47, 43, 22, 20, 31, 27),
    intArrayOf(55, 53, 44, 23, 21, 28),
    intArrayOf(54, 45, 22, 29),
    intArrayOf(57, 50, 25, 34),
    intArrayOf(58, 56, 51, 26, 24, 35),
    intArrayOf(59, 57, 52, 48, 27, 25, 36, 32),
    intArrayOf(60, 58, 53, 49, 28, 26, 37, 33),
    intArrayOf(61, 59, 54, 50, 29, 27, 38, 34),
    intArrayOf(62, 60, 55, 51, 30, 28, 39, 35),
    intArrayOf(63, 61, 52, 31, 29, 36),
    intArrayOf(62, 53, 30, 37),
    intArrayOf(58, 33, 42),
    intArrayOf(59, 34, 32, 43),
    intArrayOf(60, 56, 35, 33, 44, 40),
    intArrayOf(61, 57, 36, 34, 45, 41),
    intArrayOf(62, 58, 37, 35, 46, 42),
    intArrayOf(63, 59, 38, 36, 47, 43),
    intArrayOf(60, 39, 37, 44),
    intArrayOf(61, 38, 45),
    intArrayOf(41, 50),
    intArrayOf(42, 40, 51),
    intArrayOf(43, 41, 52, 48),
    intArrayOf(44, 42, 53, 49),
    intArrayOf(45, 43, 54, 50),
    intArrayOf(46, 44, 55, 51),
    intArrayOf(47, 45, 52),
    intArrayOf(46, 53)
)

internal fun yieldKnightMovesMatrix(): LongArray {
    return knightMatrix
        .map { it.map { sq -> 1L shl sq }.reduceOrNull { acc, element -> acc or element } ?: 0L }.toLongArray()
}

internal val knights: IntArray = intArrayOf(Piece.BN.ordinal, Piece.WN.ordinal)
internal val whitePawnMatrix1 = arrayOf(
    intArrayOf(8),
    intArrayOf(9),
    intArrayOf(10),
    intArrayOf(11),
    intArrayOf(12),
    intArrayOf(13),
    intArrayOf(14),
    intArrayOf(15),
    intArrayOf(16, 24),
    intArrayOf(17, 25),
    intArrayOf(18, 26),
    intArrayOf(19, 27),
    intArrayOf(20, 28),
    intArrayOf(21, 29),
    intArrayOf(22, 30),
    intArrayOf(23, 31),
    intArrayOf(24),
    intArrayOf(25),
    intArrayOf(26),
    intArrayOf(27),
    intArrayOf(28),
    intArrayOf(29),
    intArrayOf(30),
    intArrayOf(31),
    intArrayOf(32),
    intArrayOf(33),
    intArrayOf(34),
    intArrayOf(35),
    intArrayOf(36),
    intArrayOf(37),
    intArrayOf(38),
    intArrayOf(39),
    intArrayOf(40),
    intArrayOf(41),
    intArrayOf(42),
    intArrayOf(43),
    intArrayOf(44),
    intArrayOf(45),
    intArrayOf(46),
    intArrayOf(47),
    intArrayOf(48),
    intArrayOf(49),
    intArrayOf(50),
    intArrayOf(51),
    intArrayOf(52),
    intArrayOf(53),
    intArrayOf(54),
    intArrayOf(55),
    intArrayOf(56),
    intArrayOf(57),
    intArrayOf(58),
    intArrayOf(59),
    intArrayOf(60),
    intArrayOf(61),
    intArrayOf(62),
    intArrayOf(63),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf()
)

internal val whitePawnMatrix2 = arrayOf(
    intArrayOf(9),
    intArrayOf(8, 10),
    intArrayOf(9, 11),
    intArrayOf(10, 12),
    intArrayOf(11, 13),
    intArrayOf(12, 14),
    intArrayOf(13, 15),
    intArrayOf(14),
    intArrayOf(17),
    intArrayOf(16, 18),
    intArrayOf(17, 19),
    intArrayOf(18, 20),
    intArrayOf(19, 21),
    intArrayOf(20, 22),
    intArrayOf(21, 23),
    intArrayOf(22),
    intArrayOf(25),
    intArrayOf(24, 26),
    intArrayOf(25, 27),
    intArrayOf(26, 28),
    intArrayOf(27, 29),
    intArrayOf(28, 30),
    intArrayOf(29, 31),
    intArrayOf(30),
    intArrayOf(33),
    intArrayOf(32, 34),
    intArrayOf(33, 35),
    intArrayOf(34, 36),
    intArrayOf(35, 37),
    intArrayOf(36, 38),
    intArrayOf(37, 39),
    intArrayOf(38),
    intArrayOf(41),
    intArrayOf(40, 42),
    intArrayOf(41, 43),
    intArrayOf(42, 44),
    intArrayOf(43, 45),
    intArrayOf(44, 46),
    intArrayOf(45, 47),
    intArrayOf(46),
    intArrayOf(49),
    intArrayOf(48, 50),
    intArrayOf(49, 51),
    intArrayOf(50, 52),
    intArrayOf(51, 53),
    intArrayOf(52, 54),
    intArrayOf(53, 55),
    intArrayOf(54),
    intArrayOf(57),
    intArrayOf(56, 58),
    intArrayOf(57, 59),
    intArrayOf(58, 60),
    intArrayOf(59, 61),
    intArrayOf(60, 62),
    intArrayOf(61, 63),
    intArrayOf(62),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf(),
    intArrayOf()
)

internal fun yieldWhitePawnCaptureMoves(): LongArray{
    return whitePawnMatrix2
        .map { it.map { sq -> 1L shl sq }.reduceOrNull { acc, element -> acc or element } ?: 0L }.toLongArray()
}

internal val pawnMatrix1 = arrayOf(blackPawnMatrix1, whitePawnMatrix1)
internal val pawnMatrix2 = arrayOf(blackPawnMatrix2, whitePawnMatrix2)
internal val pawns: IntArray = intArrayOf(Piece.BP.ordinal, Piece.WP.ordinal)
internal val queenDirections: IntArray = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
internal var queenMegamatrix = arrayOf(
    arrayOf(
        intArrayOf(9, 18, 27, 36, 45, 54, 63),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(8, 16, 24, 32, 40, 48, 56),
        intArrayOf(),
        intArrayOf(1, 2, 3, 4, 5, 6, 7),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(10, 19, 28, 37, 46, 55),
        intArrayOf(8),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(9, 17, 25, 33, 41, 49, 57),
        intArrayOf(),
        intArrayOf(2, 3, 4, 5, 6, 7),
        intArrayOf(0)
    ),
    arrayOf(
        intArrayOf(11, 20, 29, 38, 47),
        intArrayOf(9, 16),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(10, 18, 26, 34, 42, 50, 58),
        intArrayOf(),
        intArrayOf(3, 4, 5, 6, 7),
        intArrayOf(1, 0)
    ),
    arrayOf(
        intArrayOf(12, 21, 30, 39),
        intArrayOf(10, 17, 24),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(11, 19, 27, 35, 43, 51, 59),
        intArrayOf(),
        intArrayOf(4, 5, 6, 7),
        intArrayOf(2, 1, 0)
    ),
    arrayOf(
        intArrayOf(13, 22, 31),
        intArrayOf(11, 18, 25, 32),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(12, 20, 28, 36, 44, 52, 60),
        intArrayOf(),
        intArrayOf(5, 6, 7),
        intArrayOf(3, 2, 1, 0)
    ),
    arrayOf(
        intArrayOf(14, 23),
        intArrayOf(12, 19, 26, 33, 40),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(13, 21, 29, 37, 45, 53, 61),
        intArrayOf(),
        intArrayOf(6, 7),
        intArrayOf(4, 3, 2, 1, 0)
    ),
    arrayOf(
        intArrayOf(15),
        intArrayOf(13, 20, 27, 34, 41, 48),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(14, 22, 30, 38, 46, 54, 62),
        intArrayOf(),
        intArrayOf(7),
        intArrayOf(5, 4, 3, 2, 1, 0)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(14, 21, 28, 35, 42, 49, 56),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(15, 23, 31, 39, 47, 55, 63),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(6, 5, 4, 3, 2, 1, 0)
    ),
    arrayOf(
        intArrayOf(17, 26, 35, 44, 53, 62),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(1),
        intArrayOf(16, 24, 32, 40, 48, 56),
        intArrayOf(0),
        intArrayOf(9, 10, 11, 12, 13, 14, 15),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(18, 27, 36, 45, 54, 63),
        intArrayOf(16),
        intArrayOf(0),
        intArrayOf(2),
        intArrayOf(17, 25, 33, 41, 49, 57),
        intArrayOf(1),
        intArrayOf(10, 11, 12, 13, 14, 15),
        intArrayOf(8)
    ),
    arrayOf(
        intArrayOf(19, 28, 37, 46, 55),
        intArrayOf(17, 24),
        intArrayOf(1),
        intArrayOf(3),
        intArrayOf(18, 26, 34, 42, 50, 58),
        intArrayOf(2),
        intArrayOf(11, 12, 13, 14, 15),
        intArrayOf(9, 8)
    ),
    arrayOf(
        intArrayOf(20, 29, 38, 47),
        intArrayOf(18, 25, 32),
        intArrayOf(2),
        intArrayOf(4),
        intArrayOf(19, 27, 35, 43, 51, 59),
        intArrayOf(3),
        intArrayOf(12, 13, 14, 15),
        intArrayOf(10, 9, 8)
    ),
    arrayOf(
        intArrayOf(21, 30, 39),
        intArrayOf(19, 26, 33, 40),
        intArrayOf(3),
        intArrayOf(5),
        intArrayOf(20, 28, 36, 44, 52, 60),
        intArrayOf(4),
        intArrayOf(13, 14, 15),
        intArrayOf(11, 10, 9, 8)
    ),
    arrayOf(
        intArrayOf(22, 31),
        intArrayOf(20, 27, 34, 41, 48),
        intArrayOf(4),
        intArrayOf(6),
        intArrayOf(21, 29, 37, 45, 53, 61),
        intArrayOf(5),
        intArrayOf(14, 15),
        intArrayOf(12, 11, 10, 9, 8)
    ),
    arrayOf(
        intArrayOf(23),
        intArrayOf(21, 28, 35, 42, 49, 56),
        intArrayOf(5),
        intArrayOf(7),
        intArrayOf(22, 30, 38, 46, 54, 62),
        intArrayOf(6),
        intArrayOf(15),
        intArrayOf(13, 12, 11, 10, 9, 8)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(22, 29, 36, 43, 50, 57),
        intArrayOf(6),
        intArrayOf(),
        intArrayOf(23, 31, 39, 47, 55, 63),
        intArrayOf(7),
        intArrayOf(),
        intArrayOf(14, 13, 12, 11, 10, 9, 8)
    ),
    arrayOf(
        intArrayOf(25, 34, 43, 52, 61),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(9, 2),
        intArrayOf(24, 32, 40, 48, 56),
        intArrayOf(8, 0),
        intArrayOf(17, 18, 19, 20, 21, 22, 23),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(26, 35, 44, 53, 62),
        intArrayOf(24),
        intArrayOf(8),
        intArrayOf(10, 3),
        intArrayOf(25, 33, 41, 49, 57),
        intArrayOf(9, 1),
        intArrayOf(18, 19, 20, 21, 22, 23),
        intArrayOf(16)
    ),
    arrayOf(
        intArrayOf(27, 36, 45, 54, 63),
        intArrayOf(25, 32),
        intArrayOf(9, 0),
        intArrayOf(11, 4),
        intArrayOf(26, 34, 42, 50, 58),
        intArrayOf(10, 2),
        intArrayOf(19, 20, 21, 22, 23),
        intArrayOf(17, 16)
    ),
    arrayOf(
        intArrayOf(28, 37, 46, 55),
        intArrayOf(26, 33, 40),
        intArrayOf(10, 1),
        intArrayOf(12, 5),
        intArrayOf(27, 35, 43, 51, 59),
        intArrayOf(11, 3),
        intArrayOf(20, 21, 22, 23),
        intArrayOf(18, 17, 16)
    ),
    arrayOf(
        intArrayOf(29, 38, 47),
        intArrayOf(27, 34, 41, 48),
        intArrayOf(11, 2),
        intArrayOf(13, 6),
        intArrayOf(28, 36, 44, 52, 60),
        intArrayOf(12, 4),
        intArrayOf(21, 22, 23),
        intArrayOf(19, 18, 17, 16)
    ),
    arrayOf(
        intArrayOf(30, 39),
        intArrayOf(28, 35, 42, 49, 56),
        intArrayOf(12, 3),
        intArrayOf(14, 7),
        intArrayOf(29, 37, 45, 53, 61),
        intArrayOf(13, 5),
        intArrayOf(22, 23),
        intArrayOf(20, 19, 18, 17, 16)
    ),
    arrayOf(
        intArrayOf(31),
        intArrayOf(29, 36, 43, 50, 57),
        intArrayOf(13, 4),
        intArrayOf(15),
        intArrayOf(30, 38, 46, 54, 62),
        intArrayOf(14, 6),
        intArrayOf(23),
        intArrayOf(21, 20, 19, 18, 17, 16)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(30, 37, 44, 51, 58),
        intArrayOf(14, 5),
        intArrayOf(),
        intArrayOf(31, 39, 47, 55, 63),
        intArrayOf(15, 7),
        intArrayOf(),
        intArrayOf(22, 21, 20, 19, 18, 17, 16)
    ),
    arrayOf(
        intArrayOf(33, 42, 51, 60),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(17, 10, 3),
        intArrayOf(32, 40, 48, 56),
        intArrayOf(16, 8, 0),
        intArrayOf(25, 26, 27, 28, 29, 30, 31),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(34, 43, 52, 61),
        intArrayOf(32),
        intArrayOf(16),
        intArrayOf(18, 11, 4),
        intArrayOf(33, 41, 49, 57),
        intArrayOf(17, 9, 1),
        intArrayOf(26, 27, 28, 29, 30, 31),
        intArrayOf(24)
    ),
    arrayOf(
        intArrayOf(35, 44, 53, 62),
        intArrayOf(33, 40),
        intArrayOf(17, 8),
        intArrayOf(19, 12, 5),
        intArrayOf(34, 42, 50, 58),
        intArrayOf(18, 10, 2),
        intArrayOf(27, 28, 29, 30, 31),
        intArrayOf(25, 24)
    ),
    arrayOf(
        intArrayOf(36, 45, 54, 63),
        intArrayOf(34, 41, 48),
        intArrayOf(18, 9, 0),
        intArrayOf(20, 13, 6),
        intArrayOf(35, 43, 51, 59),
        intArrayOf(19, 11, 3),
        intArrayOf(28, 29, 30, 31),
        intArrayOf(26, 25, 24)
    ),
    arrayOf(
        intArrayOf(37, 46, 55),
        intArrayOf(35, 42, 49, 56),
        intArrayOf(19, 10, 1),
        intArrayOf(21, 14, 7),
        intArrayOf(36, 44, 52, 60),
        intArrayOf(20, 12, 4),
        intArrayOf(29, 30, 31),
        intArrayOf(27, 26, 25, 24)
    ),
    arrayOf(
        intArrayOf(38, 47),
        intArrayOf(36, 43, 50, 57),
        intArrayOf(20, 11, 2),
        intArrayOf(22, 15),
        intArrayOf(37, 45, 53, 61),
        intArrayOf(21, 13, 5),
        intArrayOf(30, 31),
        intArrayOf(28, 27, 26, 25, 24)
    ),
    arrayOf(
        intArrayOf(39),
        intArrayOf(37, 44, 51, 58),
        intArrayOf(21, 12, 3),
        intArrayOf(23),
        intArrayOf(38, 46, 54, 62),
        intArrayOf(22, 14, 6),
        intArrayOf(31),
        intArrayOf(29, 28, 27, 26, 25, 24)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(38, 45, 52, 59),
        intArrayOf(22, 13, 4),
        intArrayOf(),
        intArrayOf(39, 47, 55, 63),
        intArrayOf(23, 15, 7),
        intArrayOf(),
        intArrayOf(30, 29, 28, 27, 26, 25, 24)
    ),
    arrayOf(
        intArrayOf(41, 50, 59),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(25, 18, 11, 4),
        intArrayOf(40, 48, 56),
        intArrayOf(24, 16, 8, 0),
        intArrayOf(33, 34, 35, 36, 37, 38, 39),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(42, 51, 60),
        intArrayOf(40),
        intArrayOf(24),
        intArrayOf(26, 19, 12, 5),
        intArrayOf(41, 49, 57),
        intArrayOf(25, 17, 9, 1),
        intArrayOf(34, 35, 36, 37, 38, 39),
        intArrayOf(32)
    ),
    arrayOf(
        intArrayOf(43, 52, 61),
        intArrayOf(41, 48),
        intArrayOf(25, 16),
        intArrayOf(27, 20, 13, 6),
        intArrayOf(42, 50, 58),
        intArrayOf(26, 18, 10, 2),
        intArrayOf(35, 36, 37, 38, 39),
        intArrayOf(33, 32)
    ),
    arrayOf(
        intArrayOf(44, 53, 62),
        intArrayOf(42, 49, 56),
        intArrayOf(26, 17, 8),
        intArrayOf(28, 21, 14, 7),
        intArrayOf(43, 51, 59),
        intArrayOf(27, 19, 11, 3),
        intArrayOf(36, 37, 38, 39),
        intArrayOf(34, 33, 32)
    ),
    arrayOf(
        intArrayOf(45, 54, 63),
        intArrayOf(43, 50, 57),
        intArrayOf(27, 18, 9, 0),
        intArrayOf(29, 22, 15),
        intArrayOf(44, 52, 60),
        intArrayOf(28, 20, 12, 4),
        intArrayOf(37, 38, 39),
        intArrayOf(35, 34, 33, 32)
    ),
    arrayOf(
        intArrayOf(46, 55),
        intArrayOf(44, 51, 58),
        intArrayOf(28, 19, 10, 1),
        intArrayOf(30, 23),
        intArrayOf(45, 53, 61),
        intArrayOf(29, 21, 13, 5),
        intArrayOf(38, 39),
        intArrayOf(36, 35, 34, 33, 32)
    ),
    arrayOf(
        intArrayOf(47),
        intArrayOf(45, 52, 59),
        intArrayOf(29, 20, 11, 2),
        intArrayOf(31),
        intArrayOf(46, 54, 62),
        intArrayOf(30, 22, 14, 6),
        intArrayOf(39),
        intArrayOf(37, 36, 35, 34, 33, 32)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(46, 53, 60),
        intArrayOf(30, 21, 12, 3),
        intArrayOf(),
        intArrayOf(47, 55, 63),
        intArrayOf(31, 23, 15, 7),
        intArrayOf(),
        intArrayOf(38, 37, 36, 35, 34, 33, 32)
    ),
    arrayOf(
        intArrayOf(49, 58),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(33, 26, 19, 12, 5),
        intArrayOf(48, 56),
        intArrayOf(32, 24, 16, 8, 0),
        intArrayOf(41, 42, 43, 44, 45, 46, 47),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(50, 59),
        intArrayOf(48),
        intArrayOf(32),
        intArrayOf(34, 27, 20, 13, 6),
        intArrayOf(49, 57),
        intArrayOf(33, 25, 17, 9, 1),
        intArrayOf(42, 43, 44, 45, 46, 47),
        intArrayOf(40)
    ),
    arrayOf(
        intArrayOf(51, 60),
        intArrayOf(49, 56),
        intArrayOf(33, 24),
        intArrayOf(35, 28, 21, 14, 7),
        intArrayOf(50, 58),
        intArrayOf(34, 26, 18, 10, 2),
        intArrayOf(43, 44, 45, 46, 47),
        intArrayOf(41, 40)
    ),
    arrayOf(
        intArrayOf(52, 61),
        intArrayOf(50, 57),
        intArrayOf(34, 25, 16),
        intArrayOf(36, 29, 22, 15),
        intArrayOf(51, 59),
        intArrayOf(35, 27, 19, 11, 3),
        intArrayOf(44, 45, 46, 47),
        intArrayOf(42, 41, 40)
    ),
    arrayOf(
        intArrayOf(53, 62),
        intArrayOf(51, 58),
        intArrayOf(35, 26, 17, 8),
        intArrayOf(37, 30, 23),
        intArrayOf(52, 60),
        intArrayOf(36, 28, 20, 12, 4),
        intArrayOf(45, 46, 47),
        intArrayOf(43, 42, 41, 40)
    ),
    arrayOf(
        intArrayOf(54, 63),
        intArrayOf(52, 59),
        intArrayOf(36, 27, 18, 9, 0),
        intArrayOf(38, 31),
        intArrayOf(53, 61),
        intArrayOf(37, 29, 21, 13, 5),
        intArrayOf(46, 47),
        intArrayOf(44, 43, 42, 41, 40)
    ),
    arrayOf(
        intArrayOf(55),
        intArrayOf(53, 60),
        intArrayOf(37, 28, 19, 10, 1),
        intArrayOf(39),
        intArrayOf(54, 62),
        intArrayOf(38, 30, 22, 14, 6),
        intArrayOf(47),
        intArrayOf(45, 44, 43, 42, 41, 40)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(54, 61),
        intArrayOf(38, 29, 20, 11, 2),
        intArrayOf(),
        intArrayOf(55, 63),
        intArrayOf(39, 31, 23, 15, 7),
        intArrayOf(),
        intArrayOf(46, 45, 44, 43, 42, 41, 40)
    ),
    arrayOf(
        intArrayOf(57),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(41, 34, 27, 20, 13, 6),
        intArrayOf(56),
        intArrayOf(40, 32, 24, 16, 8, 0),
        intArrayOf(49, 50, 51, 52, 53, 54, 55),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(58),
        intArrayOf(56),
        intArrayOf(40),
        intArrayOf(42, 35, 28, 21, 14, 7),
        intArrayOf(57),
        intArrayOf(41, 33, 25, 17, 9, 1),
        intArrayOf(50, 51, 52, 53, 54, 55),
        intArrayOf(48)
    ),
    arrayOf(
        intArrayOf(59),
        intArrayOf(57),
        intArrayOf(41, 32),
        intArrayOf(43, 36, 29, 22, 15),
        intArrayOf(58),
        intArrayOf(42, 34, 26, 18, 10, 2),
        intArrayOf(51, 52, 53, 54, 55),
        intArrayOf(49, 48)
    ),
    arrayOf(
        intArrayOf(60),
        intArrayOf(58),
        intArrayOf(42, 33, 24),
        intArrayOf(44, 37, 30, 23),
        intArrayOf(59),
        intArrayOf(43, 35, 27, 19, 11, 3),
        intArrayOf(52, 53, 54, 55),
        intArrayOf(50, 49, 48)
    ),
    arrayOf(
        intArrayOf(61),
        intArrayOf(59),
        intArrayOf(43, 34, 25, 16),
        intArrayOf(45, 38, 31),
        intArrayOf(60),
        intArrayOf(44, 36, 28, 20, 12, 4),
        intArrayOf(53, 54, 55),
        intArrayOf(51, 50, 49, 48)
    ),
    arrayOf(
        intArrayOf(62),
        intArrayOf(60),
        intArrayOf(44, 35, 26, 17, 8),
        intArrayOf(46, 39),
        intArrayOf(61),
        intArrayOf(45, 37, 29, 21, 13, 5),
        intArrayOf(54, 55),
        intArrayOf(52, 51, 50, 49, 48)
    ),
    arrayOf(
        intArrayOf(63),
        intArrayOf(61),
        intArrayOf(45, 36, 27, 18, 9, 0),
        intArrayOf(47),
        intArrayOf(62),
        intArrayOf(46, 38, 30, 22, 14, 6),
        intArrayOf(55),
        intArrayOf(53, 52, 51, 50, 49, 48)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(62),
        intArrayOf(46, 37, 28, 19, 10, 1),
        intArrayOf(),
        intArrayOf(63),
        intArrayOf(47, 39, 31, 23, 15, 7),
        intArrayOf(),
        intArrayOf(54, 53, 52, 51, 50, 49, 48)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(49, 42, 35, 28, 21, 14, 7),
        intArrayOf(),
        intArrayOf(48, 40, 32, 24, 16, 8, 0),
        intArrayOf(57, 58, 59, 60, 61, 62, 63),
        intArrayOf()
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(48),
        intArrayOf(50, 43, 36, 29, 22, 15),
        intArrayOf(),
        intArrayOf(49, 41, 33, 25, 17, 9, 1),
        intArrayOf(58, 59, 60, 61, 62, 63),
        intArrayOf(56)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(49, 40),
        intArrayOf(51, 44, 37, 30, 23),
        intArrayOf(),
        intArrayOf(50, 42, 34, 26, 18, 10, 2),
        intArrayOf(59, 60, 61, 62, 63),
        intArrayOf(57, 56)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(50, 41, 32),
        intArrayOf(52, 45, 38, 31),
        intArrayOf(),
        intArrayOf(51, 43, 35, 27, 19, 11, 3),
        intArrayOf(60, 61, 62, 63),
        intArrayOf(58, 57, 56)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(51, 42, 33, 24),
        intArrayOf(53, 46, 39),
        intArrayOf(),
        intArrayOf(52, 44, 36, 28, 20, 12, 4),
        intArrayOf(61, 62, 63),
        intArrayOf(59, 58, 57, 56)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(52, 43, 34, 25, 16),
        intArrayOf(54, 47),
        intArrayOf(),
        intArrayOf(53, 45, 37, 29, 21, 13, 5),
        intArrayOf(62, 63),
        intArrayOf(60, 59, 58, 57, 56)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(53, 44, 35, 26, 17, 8),
        intArrayOf(55),
        intArrayOf(),
        intArrayOf(54, 46, 38, 30, 22, 14, 6),
        intArrayOf(63),
        intArrayOf(61, 60, 59, 58, 57, 56)
    ),
    arrayOf(
        intArrayOf(),
        intArrayOf(),
        intArrayOf(54, 45, 36, 27, 18, 9, 0),
        intArrayOf(),
        intArrayOf(),
        intArrayOf(55, 47, 39, 31, 23, 15, 7),
        intArrayOf(),
        intArrayOf(62, 61, 60, 59, 58, 57, 56)
    )
)
internal val queens: IntArray = intArrayOf(Piece.BQ.ordinal, Piece.WQ.ordinal)
internal val rookDirections: IntArray = intArrayOf(4, 5, 6, 7)
internal val rooks: IntArray = intArrayOf(Piece.BR.ordinal, Piece.WR.ordinal)
internal val bishopDirections: IntArray = intArrayOf(0, 1, 2, 3)
internal val bishops: IntArray = intArrayOf(Piece.BB.ordinal, Piece.WB.ordinal)

// short castle black bitboard masks, element 0 for king mask, element 1 for
// rook mask
internal val scbMask: LongArray = longArrayOf(1L shl 60, 1L shl 63)

// short castle black squares, element 0 for king square, element 1 for rook
// square
internal val scbSquares: IntArray = intArrayOf(60, 63)

// short castle white bitboard masks, element 0 for king mask, element 1 for
// rook mask
internal val scwMask: LongArray = longArrayOf(1L shl 4, (1 shl 7).toLong())

// short castle white squares, element 0 for king square, element 1 for rook
// square
internal val scwSquares: IntArray = intArrayOf(4, 7)

// long castle black bitboard masks, element 0 for king mask, element 1 for rook
// mask
internal val lcbMask: LongArray = longArrayOf(1L shl 60, 1L shl 56)

// long castle black squares, element 0 for king square, element 1 for rook
// square
internal val lcbSquares: IntArray = intArrayOf(60, 56)

// long castle white bitboard masks, element 0 for king mask, element 1 for rook
// mask
internal val lcwMask: LongArray = longArrayOf(1L shl 4, (1 shl 0).toLong())

// long castle white squares, element 0 for king square, element 1 for rook
// square
internal val lcwSquares: IntArray = intArrayOf(4, 0)

internal val castleMask = arrayOf(arrayOf(scbMask, lcbMask), arrayOf(scwMask, lcwMask))

internal val castleSquares = arrayOf(
    arrayOf(scbSquares, lcbSquares),
    arrayOf(scwSquares, lcwSquares)
)

internal val kingPieces: IntArray = intArrayOf(Piece.BK.ordinal, Piece.WK.ordinal)
internal val rookPieces: IntArray = intArrayOf(Piece.BR.ordinal, Piece.WR.ordinal)

internal val indexes: IntArray = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)