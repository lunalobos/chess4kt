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

/*
 I take most of this code from https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/LinkedList.java
 */
internal class LinkedList<T> : AbstractMutableList<T>() {

    override var size: Int = 0

    private var first: Node<T>? = null
    private var last: Node<T>? = null

    override fun get(index: Int): T {
        checkElementIndex(index)
        return node(index).element!!
    }

    override fun set(index: Int, element: T): T {
        checkElementIndex(index)
        val n = node(index)
        val oldElement = n.element
        n.element = element
        return oldElement!!
    }

    override fun add(index: Int, element: T) {
        checkPositionIndex(index)
        when (index) {
            size -> {
                linkLast(element)
            }
            0 -> {
                linkFirst(element)
            }
            else -> {
                linkBefore(element, node(index))
            }
        }
    }

    override fun add(element: T): Boolean {
        linkLast(element)
        return true
    }

    override fun removeAt(index: Int): T {
        checkElementIndex(index)
        return when (index) {
            0 -> {
                unlinkFirst(first!!)
            }
            size - 1 -> {
                unlinkLast(last!!)
            }
            else -> {
                unlink(node(index))
            }
        }
    }

    private fun linkFirst(element: T) {
        val f = first
        val newNode = Node(element = element, next = f)
        first = newNode
        if (f == null) {
            last = newNode
        } else {
            f.prev = newNode
        }
        size++
        modCount++
    }

    private fun linkLast(element: T) {
        val l = last
        val newNode = Node(prev = l, element = element)
        last = newNode
        if (l == null) {
            first = newNode
        } else {
            l.next = newNode
        }
        size++
        modCount++
    }

    private fun linkBefore(element: T, next: Node<T>) {
        val prev = next.prev
        val newNode = Node(prev, element, next)
        next.prev = newNode
        if (prev == null) {
            first = newNode
        } else {
            prev.next = newNode
        }
        size++
        modCount++
    }

    private fun unlinkFirst(f: Node<T>): T {
        val element = f.element!!
        val next = f.next
        f.next = null
        first = next
        if(next == null) {
            last = null
        } else {
            next.prev = null
        }
        f.element = null
        size--
        modCount++
        return element
    }

    private fun unlinkLast(l: Node<T>): T {
        val element = l.element!!
        val prev = l.prev
        l.prev = null
        last = prev
        if (prev == null) {
            first = null
        } else {
            prev.next = null
        }
        l.element = null
        size--
        modCount++
        return element
    }

    private fun unlink(node: Node<T>): T {
        val element = node.element!!
        val next = node.next
        val prev = node.prev
        if(prev == null) {
            first = next
        } else {
            prev.next = next
            node.prev = null
        }
        if(next == null) {
            last = prev
        } else {
            next.prev = prev
            node.next = null
        }
        node.element = null
        size--
        modCount++
        return element
    }

    private fun node(index: Int): Node<T> {
        if(index < (size ushr 1)){
            var curr = first
            for(i in 0..<index){
                curr = curr?.next
            }
            return curr ?: throw NullPointerException("index $index, size: $size")
        } else {
            var curr = last
            for(i in (size - 1) downTo (index + 1)){
                curr = curr?.prev
            }
            return curr ?: throw NullPointerException("index $index, size: $size")
        }
    }

    private fun isElementIndex(index: Int): Boolean {
        return index in indices
    }

    private fun checkElementIndex(index: Int){
        if (!isElementIndex(index)){
            throw IndexOutOfBoundsException(outOfBoundsMsg(index))
        }
    }

    private fun isPositionIndex(index: Int): Boolean {
        return index in 0..size
    }

    private fun checkPositionIndex(index: Int) {
        if(!isPositionIndex(index)){
            throw IndexOutOfBoundsException(outOfBoundsMsg(index))
        }
    }

    private fun outOfBoundsMsg(index: Int): String {
        return "Index: $index, Size: $size"
    }

    private class Node<T>(
        var prev: Node<T>? = null,
        var element: T?,
        var next: Node<T>? = null
    )
}