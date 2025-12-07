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


import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class Logger(val name: String, val filterLevel: Level) {

    fun trace(message: String){
        log(msg = message, level = Level.TRACE)
    }

    fun traceEntry(functionName: String, vararg arguments: Any){
        val args = arguments.toList().map {
            when(it){
                is Array<*> -> it.toList()
                is LongArray -> it.toList().map{e -> Bitboard(e).toSquares() }
                is IntArray -> it.toList()
                is Long -> Bitboard(it).toSquares()
                else -> it
            }
        }
        trace("entry to $functionName with arguments ${args}")
    }

    fun <T> traceExit(functionName: String, output: T): T{
        trace("Exit from $functionName with $output")
        return output
    }

    fun debug(message: String){
        log(msg = message, level = Level.DEBUG)
    }

    fun info(message: String){
        log(msg = message, level = Level.INFO)
    }

    fun warn(message: String){
        log(msg = message, level = Level.WARN)
    }

    fun error(message: String){
        log(msg = message, level = Level.ERROR)
    }

    fun error(err: Throwable): Throwable {
        error(err.message ?: "none")
        return err
    }

    fun fatal(message: String){
        log(msg = message, level = Level.FATAL)
    }

    fun fatal(err: Throwable): Throwable {
        fatal(err.message ?: "none")
        return err
    }

    fun instantiation() {
        debug("$name created")
    }

    @OptIn(ExperimentalTime::class)
    fun instantiation(t1: Instant, t2: Instant) {
        val ms: Long = t2.toEpochMilliseconds() - t1.toEpochMilliseconds()
        debug("$name created in $ms ms")
    }

    @OptIn(ExperimentalTime::class)
    private fun log(level: Level, msg: String){
        if (level.ordinal >= filterLevel.ordinal) {
            println("[${level.name}] - ${now()} - $name - $msg".trim { it <= ' ' })
        }
    }

}