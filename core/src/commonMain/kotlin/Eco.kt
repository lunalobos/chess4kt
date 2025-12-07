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

internal class Eco {
    val movesMap: Map<String, EcoInfo>
    val positionMap: Map<Position, EcoInfo>

    constructor(){
        movesMap = loadMoves()
        positionMap = loadPositions()
    }

    private fun loadPositions(): Map<Position, EcoInfo>{
        val positions = mutableMapOf<Position, EcoInfo>()
        movesMap.entries.asSequence()
            .forEach { entry ->
                val moves = entry.key.split(Regex("\\s+")).fold(ArrayDeque<String>()){ deque, curr ->
                    deque.add(curr)
                    deque
                }
                var position = positionOf()
                while(moves.isNotEmpty()){
                    position = position.move(moves.pop(), Notation.SAN)
                    positions[position] = entry.value
                }
            }
        return positions
    }

    private fun loadMoves(): Map<String, EcoInfo>{
        return openings.split("\n").asSequence()
            .map { parseLine(it)}
            .map { EcoRow(it[0].trim(), it[1].trim(), it[2].trim())}
            .fold(mutableMapOf<String, EcoInfo>()) { map, curr ->
                map[curr.moves] = curr.asDescriptor()
                map
            }
    }

    private fun parseLine(line: String): List<String> {
        val stack = line.toCharArray().asSequence().fold(ArrayDeque<Char>()){ deque, curr ->
            deque.add(curr)
            deque
        }

        val strings = mutableListOf<String>()
        var sb = StringBuilder()
        var previousString = false
        while (!stack.isEmpty()) {
            val c: Char = stack.pop()
            val stringSeparator = '"'
            val separator = ','
            if (c == separator && !previousString) {
                strings.add(sb.toString())

                sb = StringBuilder()
            } else if (c == stringSeparator && sb.length == 0) {
                val bucket = Bucket(stringSeparator)
                previousString = true
                while (bucket.hasSpace && !stack.isEmpty()) {
                    val ch: Char = stack.pop()
                    bucket.add(ch)
                }
                strings.add(bucket.content())
            } else if (c == separator) {
                continue
            } else {
                sb.append(c)
            }
        }
        if (sb.isNotEmpty()) {
            strings.add(sb.toString())
        }
        return strings
    }

    class Bucket{
        val end: Char
        val sb: StringBuilder
        var hasSpace: Boolean

        constructor(end: Char){
            this.end = end
            sb = StringBuilder()
            hasSpace = true
        }

        fun add(c: Char){
            if(c == end){
                hasSpace = false
                return
            }
            sb.append(c)
        }

        fun content(): String{
            return sb.toString()
        }

    }

    data class EcoRow (val eco: String, val name: String, val moves: String){
        fun asDescriptor(): EcoInfo{
            return EcoInfo(eco, name)
        }
    }
}

private fun <T> ArrayDeque<T>.pop(): T {
    return this.removeFirst()
}