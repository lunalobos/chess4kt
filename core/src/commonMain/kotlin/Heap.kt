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

internal class Heap<T>(
    initialSize: Int = 10,
    var comparator: Comparator<T>
) {
    private var heapSize = 0

    val size: Int
        get() = heapSize

    private var array = arrayOfNulls<Any>(
        if (initialSize < 10) {
            11
        } else {
            initialSize + 1
        }
    )

    companion object {
        private const val FRONT = 1
        private const val EMPTY_HEAP_ERROR = "Heap is empty"

        private fun parent(i: Int) = i shr 1
        private fun left(i: Int) = i shl 1
        private fun right(i: Int) = (i shl 1) + 1
    }

    fun isNotEmpty(): Boolean = heapSize > 0

    fun clear() {
        for (i in 1..heapSize) {
            array[i] = null
        }
        heapSize = 0
    }

    @Suppress("UNCHECKED_CAST")
    private fun elementAt(i: Int): T = array[i] as T

    private fun isHigherPriority(a: T, b: T): Boolean = comparator.compare(a, b) < 0

    private fun grow() {
        val newSize = (array.size - 1) * 2
        array = array.copyOf(newSize + 1)
    }

    fun pop(): T? {
        if (heapSize == 0) return null
        val popped = elementAt(FRONT)
        array[FRONT] = array[heapSize]
        array[heapSize--] = null
        if (heapSize > 0) heapify(FRONT)
        return popped
    }

    fun peek(): T = if (heapSize > 0) elementAt(FRONT) else error(EMPTY_HEAP_ERROR)

    operator fun plusAssign(value: T) {
        if (heapSize >= array.size - 1) {
            grow()
        }
        array[++heapSize] = value
        var current = heapSize
        var p = parent(current)
        while (p != 0 && isHigherPriority(elementAt(current), elementAt(p))) {
            swap(p, current)
            current = p
            p = parent(current)
        }
    }

    fun toList(): List<T> {
        val copy = Heap<T>(heapSize, comparator)
        copy.heapSize = heapSize
        copy.array = array.copyOf()
        val list = mutableListOf<T>()
        while(copy.isNotEmpty()){
            list.add(copy.pop()!!)
        }
        return list
    }

    private fun swap(i: Int, j: Int) {
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }

    private tailrec fun heapify(i: Int) {
        val l = left(i)
        val r = right(i)
        var priorityIdx = i
        if (l <= heapSize && isHigherPriority(elementAt(l), elementAt(priorityIdx))) priorityIdx = l
        if (r <= heapSize && isHigherPriority(elementAt(r), elementAt(priorityIdx))) priorityIdx = r
        if (priorityIdx != i) {
            swap(i, priorityIdx)
            heapify(priorityIdx)
        }
    }

    override fun toString() = toList().toString()
}