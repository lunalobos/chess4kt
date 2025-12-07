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

internal fun parseMoves(strArray: String ): List<Move> {
    val str = strArray.substring(1, strArray.length - 1)
    if(str.length < 4){
        return listOf()
    }
    return str.split(",").asSequence()
        .map { it.trim() }
        .map { moveOf(it) }
        .toList()
}

internal fun posToBasicPos(pos: Pos): BasicPosition {
    val bitboards: LongArray
    val moves: List<Move>
    val basicPos:  BasicPosition
    with(pos) {
        bitboards = arrayOf(wp, wn, wb, wr, wq, wk, bp, bn, bb, br, bq, bk)
            .map { it.toLong(16) }
            .toLongArray()
        moves = parseMoves(pos.children)
        basicPos = BasicPosition(
            bitboards,
            wm,
            ep,
            wsc,
            wlc,
            bsc,
            blc,
            moves
        )
    }
    return basicPos
}