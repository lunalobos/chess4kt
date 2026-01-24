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
package io.github.lunalobos.chess4kt.js

import io.github.lunalobos.chess4kt.genericHashCode

/**
 * Auxiliary generic class to represent a pair of related, heterogeneous elements. It is immutable.
 *
 * This class provides properties [v1] and [v2], along with overloaded operators
 * [component1] and [component2] to enable destructuring declarations (e.g., `val (position, move) = tuple`).
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Tuple<T1, T2> {

    companion object {
        /**
         * Factory function to easily create a new [Tuple] instance.
         *
         */
        fun <T1, T2> of(v1: T1, v2: T2): Tuple<T1,T2> {
            return Tuple(v1, v2)
        }
    }

    /**
     * The first element of the tuple.
     */
    val v1: T1

    /**
     * The second element of the tuple.
     */
    val v2: T2

    /**
     * Internal constructor for creating a [Tuple] instance.
     *
     */
    internal constructor(v1: T1, v2: T2) {
        this.v1 = v1
        this.v2 = v2
    }

    operator fun component1(): T1 {
        return v1
    }

    operator fun component2(): T2 {
        return v2
    }

    override fun hashCode(): Int {
        return genericHashCode(arrayOf(v1, v2))
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null) {
            false
        } else if (other === this) {
            true
        } else if (other is Tuple<T1, T2>) {
            other.v1 == v1 && other.v2 == v2
        } else {
            false
        }
    }
}