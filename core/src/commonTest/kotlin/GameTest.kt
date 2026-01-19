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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GameTest {

    internal companion object {
        val logger = getLogger("io.github.lunalobos.chess4kt.GameTest")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun checkmate() {
        val t1 = now()
        val game = strictMatch()
        game.root
            .appendMove("e4")
            .appendMove("e5")
            .appendMove("Bc4")
            .appendMove("d6")
            .appendMove("Qh5")
            .appendMove("Nf6")
            .appendMove("Qxf7#")
        assertEquals(Game.Result.WHITE_WIN, game.result)
        val t2 = now()
        logger.debug("checkmate time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun threeRepetitions() {
        val t1 = now()
        val game = strictMatch()
        game.root
            .appendMove("e4")
            .appendMove("c5")
            .appendMove("Nf3")
            .appendMove("e6")
            .appendMove("d4")
            .appendMove("cxd4")
            .appendMove("Nxd4")
            .appendMove("Qb6")
            .appendMove("Nc3")
            .appendMove("Bc5")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("Nc3")
            .appendMove("Qb6")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("Nc3")
            .appendMove("Qb6")
        assertTrue(game.threeRepetitions)
        assertEquals(Game.Result.DRAW, game.result)
        val t2 = now()
        logger.debug("threeRepetitions time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun fiveRepetitions() {
        val t1 = now()
        val game = awareMatch()
        game.root
            .appendMove("e4")
            .appendMove("c5")
            .appendMove("Nf3")
            .appendMove("e6")
            .appendMove("d4")
            .appendMove("cxd4")
            .appendMove("Nxd4")
            .appendMove("Qb6")
            .appendMove("Nc3")
            .appendMove("Bc5")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("Nc3")
            .appendMove("Qb6")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("Nc3")
            .appendMove("Qb6")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("Nc3")
            .appendMove("Qb6")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("Nc3")
            .appendMove("Qb6")
        assertTrue(game.fiveRepetitions)
        assertEquals(Game.Result.DRAW, game.result)
        val t2 = now()
        logger.debug("fiveRepetitions time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    @Test
    fun pgn1() {
        val t1 = now()
        val id = Uuid.random()
        val game = analysisGame{ id }
        with(game) {
            tags["event"] = "test"
            tags["site"] = "test"
            tags["date"] = "2025.11.30"
            tags["round"] = "test"
            tags["white"] = "nn"
            tags["black"] = "nn"
            tags["eventDate"] = "2025.11.30"
            result = Game.Result.DRAW
        }
        val node: Game.Node

        game.root.appendMove("d4")
            .appendMove("Nf6")
            .appendMove("c4")
            .appendMove("e6")
            .appendMove("Nc3")
            .appendMove("Bb4")
            .appendMove("e3")
            .appendMove("O-O")
            .appendMove("Bd3")
            .appendMove("d5")
            .appendMove("Nf3")
            .appendMove("c5")
            .appendMove("O-O")
            .appendMove("cxd4")
            .appendMove("exd4")
            .appendMove("dxc4")
            .appendMove("Bxc4")
            .also { node = it }
            .appendMove("b6")
            .appendMove("Bg5")
            .appendMove("Bb7")

        node.appendMove(
            "Nc6",
            null,
            "Test comment",
            null,
            listOf(6)
        )
            .appendMove("Bg5")
            .appendMove("Be7")

        game.updateEco()

        val pgn = """
            [Event "test"]
            [Site "test"]
            [Date "2025.11.30"]
            [Round "test"]
            [White "nn"]
            [Black "nn"]
            [Result "1/2-1/2"]
            [ECO "E57"]
            [Id "$id"]
            [EventDate "2025.11.30"]
            [Opening "E57"]
            
            1.d4 Nf6 2.c4 e6 3.Nc3 Bb4 4.e3 O-O 5.Bd3 d5 6.Nf3 c5 7.O-O cxd4 8.exd4 dxc4 9.Bxc4 b6 
            	( 9...Nc6 $6 {Test comment} 10.Bg5 Be7 )
            10.Bg5 Bb7 1/2-1/2
        """.trimIndent()

        assertEquals(pgn, game.toString())
        val t2 = now()
        logger.debug("pgn1 time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    @Test
    fun pgn2(){
        val t1 = now()
        val id = Uuid.random()
        val game = analysisGame { id }
        with(game) {
            tags["event"] = "test"
            tags["site"] = "test"
            tags["date"] = "2025.11.30"
            tags["round"] = "test"
            tags["white"] = "nn"
            tags["black"] = "nn"
            tags["eventDate"] = "2025.11.30"
            result = Game.Result.DRAW
        }

        val node: Game.Node

        game.root.appendMove("e4")
            .appendMove("c5")
            .appendMove("Nf3")
            .appendMove("e6")
            .appendMove("d4")
            .appendMove("cxd4")
            .appendMove("Nxd4")
            .appendMove("Qb6")
            .also { node = it }
            .appendMove("Nb3")
            .appendMove("Qc7")
            .appendMove("Nc3")
            .appendMove("a6")
            .appendMove("Bd3")
            .appendMove("Nf6")

        node.appendMove("Nc3")
            .appendMove("Bc5")
            .appendMove("Na4")
            .appendMove("Qa5+")
            .appendMove("c3")
            .appendMove("Bxd4")
            .appendMove("Qxd4")
            .appendMove("Nf6")

        game.updateEco()

        val pgn = """
            [Event "test"]
            [Site "test"]
            [Date "2025.11.30"]
            [Round "test"]
            [White "nn"]
            [Black "nn"]
            [Result "1/2-1/2"]
            [ECO "B40"]
            [Id "$id"]
            [EventDate "2025.11.30"]
            [Opening "Kveinis Variation, Sicilian"]

            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 Qb6 5.Nb3 
            	( 5.Nc3 Bc5 6.Na4 Qa5+ 7.c3 Bxd4 8.Qxd4 Nf6 )
            5...Qc7 6.Nc3 a6 7.Bd3 Nf6 1/2-1/2
        """.trimIndent()

        assertEquals(pgn, game.toString())
        val t2 = now()
        logger.debug("pgn2 time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    @Test
    fun magnus(){
        val pgnMoves = """
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.Qe2 e5 7.Nf5 Nc6 8.O-O g6 9.Ne3 Bg7 10.Nc3 O-O 11.Ned5 Nxd5 
            12.Nxd5 d6 13.Be3 Be6 14.Bc4 b5 15.Bb3 Nd4 16.Bxd4 exd4 17.a4 Bd7 18.axb5 axb5 19.Rxa8 Qxa8 20.Nb6 Qa7 21.Nxd7 
            Qxd7 22.Bd5 h5 23.Ra1 Rb8 24.g3 Bf6 25.Ra6 b4 26.Qc4 Kg7 27.Rc6 Bd8 28.b3 Qa7 29.Rxd6 Bb6 30.Kg2 Rd8
        """.trimIndent()
        val moves = extractMoves(pgnMoves)
        val t1 = now()
        val id = Uuid.random()
        val game = analysisGame { id }
        with(game){
            tags["event"] = "2nd Norway Blitz 2014"
            tags["site"] = "Flor & Fjaere NOR"
            tags["date"] = "2014.06.02"
            tags["round"] = "9.4"
            tags["white"] = "Carlsen,M"
            tags["black"] = "Agdestein,S"
            result = Game.Result.WHITE_WIN
            tags["whiteElo"] = "2881"
            tags["blackElo"] = "2628"
            tags["eventDate"] = "2014.06.02"
            tags["whiteTitle"] = "GM"
            tags["blackTitle"] = "GM"
            tags["whiteFideId"] = "1503014"
            tags["blackFideId"] = "1500015"
        }
        moves.fold(game.root){ n, m -> n.appendMove(m) }
        val t2 = now()
        val time = t2.toEpochMilliseconds() - t1.toEpochMilliseconds()
        logger.debug("magnus time[ms]=$time")
        assertTrue(time <= 300)
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    @Test
    fun deleteBefore(){
        val pgnMoves = """
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.Qe2 e5 7.Nf5 Nc6 8.O-O g6 9.Ne3 Bg7 10.Nc3 O-O 11.Ned5 Nxd5 
            12.Nxd5 d6 13.Be3 Be6 14.Bc4 b5 15.Bb3 Nd4 16.Bxd4 exd4 17.a4 Bd7 18.axb5 axb5 19.Rxa8 Qxa8 20.Nb6 Qa7 21.Nxd7 
            Qxd7 22.Bd5 h5 23.Ra1 Rb8 24.g3 Bf6 25.Ra6 b4 26.Qc4 Kg7 27.Rc6 Bd8 28.b3 Qa7 29.Rxd6 Bb6 30.Kg2 Rd8
        """.trimIndent()
        val moves = extractMoves(pgnMoves)
        val t1 = now()
        val id = Uuid.random()
        val game = analysisGame { id }
        with(game){
            tags["event"] = "2nd Norway Blitz 2014"
            tags["site"] = "Flor & Fjaere NOR"
            tags["date"] = "2014.06.02"
            tags["round"] = "9.4"
            tags["white"] = "Carlsen,M"
            tags["black"] = "Agdestein,S"
            result = Game.Result.WHITE_WIN
            tags["whiteElo"] = "2881"
            tags["blackElo"] = "2628"
            tags["eventDate"] = "2014.06.02"
            tags["whiteTitle"] = "GM"
            tags["blackTitle"] = "GM"
            tags["whiteFideId"] = "1503014"
            tags["blackFideId"] = "1500015"
        }
        moves.fold(game.root){ n, m -> n.appendMove(m) }

        val node = game.iterator().asSequence()
            .filter { it.position.movesCounter == 17 && !it.position.whiteMove }.last()

        game.deleteBefore(node)
        game.updateEco()

        val pgn = """
            [Event "2nd Norway Blitz 2014"]
            [Site "Flor & Fjaere NOR"]
            [Date "2014.06.02"]
            [Round "9.4"]
            [White "Carlsen,M"]
            [Black "Agdestein,S"]
            [Result "1-0"]
            [ECO "unknown"]
            [Id "$id"]
            [WhiteElo "2881"]
            [BlackElo "2628"]
            [EventDate "2014.06.02"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [WhiteFideId "1503014"]
            [BlackFideId "1500015"]
            [Fen "r2q1rk1/5pbp/p2pb1p1/1p1N4/3pP3/1B6/PPP1QPPP/R4RK1 w - - 0 17"]
            [Opening "unknown"]
            
            17.a4 Bd7 18.axb5 axb5 19.Rxa8 Qxa8 20.Nb6 Qa7 21.Nxd7 Qxd7 22.Bd5 h5 23.Ra1 Rb8 24.g3 Bf6 25.Ra6 b4 26.Qc4 Kg7 27.Rc6 Bd8 28.b3 Qa7 29.Rxd6 Bb6 30.Kg2 Rd8 1-0
        """.trimIndent()

        assertEquals(pgn, game.toString())
        val t2 = now()
        val time = t2.toEpochMilliseconds() - t1.toEpochMilliseconds()
        logger.debug("deleteBefore time[ms]=$time")
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    @Test
    fun deleteFromExclusive(){
        val pgnMoves = """
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.Qe2 e5 7.Nf5 Nc6 8.O-O g6 9.Ne3 Bg7 10.Nc3 O-O 11.Ned5 Nxd5 
            12.Nxd5 d6 13.Be3 Be6 14.Bc4 b5 15.Bb3 Nd4 16.Bxd4 exd4 17.a4 Bd7 18.axb5 axb5 19.Rxa8 Qxa8 20.Nb6 Qa7 21.Nxd7 
            Qxd7 22.Bd5 h5 23.Ra1 Rb8 24.g3 Bf6 25.Ra6 b4 26.Qc4 Kg7 27.Rc6 Bd8 28.b3 Qa7 29.Rxd6 Bb6 30.Kg2 Rd8
        """.trimIndent()
        val moves = extractMoves(pgnMoves)
        val t1 = now()
        val id = Uuid.random()
        val game = analysisGame { id }
        with(game){
            tags["event"] = "2nd Norway Blitz 2014"
            tags["site"] = "Flor & Fjaere NOR"
            tags["date"] = "2014.06.02"
            tags["round"] = "9.4"
            tags["white"] = "Carlsen,M"
            tags["black"] = "Agdestein,S"
            result = Game.Result.WHITE_WIN
            tags["whiteElo"] = "2881"
            tags["blackElo"] = "2628"
            tags["eventDate"] = "2014.06.02"
            tags["whiteTitle"] = "GM"
            tags["blackTitle"] = "GM"
            tags["whiteFideId"] = "1503014"
            tags["blackFideId"] = "1500015"
        }
        moves.fold(game.root){ n, m -> n.appendMove(m) }

        val node = game.iterator().asSequence()
            .filter { it.position.movesCounter == 17 && !it.position.whiteMove }.last()

        game.deleteFromExclusive(node)
        game.updateEco()

        val pgn = """
            [Event "2nd Norway Blitz 2014"]
            [Site "Flor & Fjaere NOR"]
            [Date "2014.06.02"]
            [Round "9.4"]
            [White "Carlsen,M"]
            [Black "Agdestein,S"]
            [Result "1-0"]
            [ECO "B42"]
            [Id "$id"]
            [WhiteElo "2881"]
            [BlackElo "2628"]
            [EventDate "2014.06.02"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [WhiteFideId "1503014"]
            [BlackFideId "1500015"]
            [Opening "Swiss Cheese Variation, Sicilian"]
            
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.Qe2 e5 7.Nf5 Nc6 8.O-O g6 9.Ne3 Bg7 10.Nc3 O-O 11.Ned5 Nxd5 12.Nxd5 d6 13.Be3 Be6 14.Bc4 b5 15.Bb3 Nd4 16.Bxd4 exd4 17.a4 1-0
        """.trimIndent()

        assertEquals(pgn, game.toString())
        val t2 = now()
        val time = t2.toEpochMilliseconds() - t1.toEpochMilliseconds()
        logger.debug("deleteFromExclusive time[ms]=$time")
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    @Test
    fun deleteFromInclusive(){
        val pgnMoves = """
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.Qe2 e5 7.Nf5 Nc6 8.O-O g6 9.Ne3 Bg7 10.Nc3 O-O 11.Ned5 Nxd5 
            12.Nxd5 d6 13.Be3 Be6 14.Bc4 b5 15.Bb3 Nd4 16.Bxd4 exd4 17.a4 Bd7 18.axb5 axb5 19.Rxa8 Qxa8 20.Nb6 Qa7 21.Nxd7 
            Qxd7 22.Bd5 h5 23.Ra1 Rb8 24.g3 Bf6 25.Ra6 b4 26.Qc4 Kg7 27.Rc6 Bd8 28.b3 Qa7 29.Rxd6 Bb6 30.Kg2 Rd8
        """.trimIndent()
        val moves = extractMoves(pgnMoves)
        val t1 = now()
        val id = Uuid.random()
        val game = analysisGame { id }
        with(game){
            tags["event"] = "2nd Norway Blitz 2014"
            tags["site"] = "Flor & Fjaere NOR"
            tags["date"] = "2014.06.02"
            tags["round"] = "9.4"
            tags["white"] = "Carlsen,M"
            tags["black"] = "Agdestein,S"
            result = Game.Result.WHITE_WIN
            tags["whiteElo"] = "2881"
            tags["blackElo"] = "2628"
            tags["eventDate"] = "2014.06.02"
            tags["whiteTitle"] = "GM"
            tags["blackTitle"] = "GM"
            tags["whiteFideId"] = "1503014"
            tags["blackFideId"] = "1500015"
        }
        moves.fold(game.root){ n, m -> n.appendMove(m) }

        val node = game.iterator().asSequence()
            .filter { it.position.movesCounter == 17 && !it.position.whiteMove }.last()

        game.deleteFromInclusive(node)
        game.updateEco()

        val pgn = """
            [Event "2nd Norway Blitz 2014"]
            [Site "Flor & Fjaere NOR"]
            [Date "2014.06.02"]
            [Round "9.4"]
            [White "Carlsen,M"]
            [Black "Agdestein,S"]
            [Result "1-0"]
            [ECO "B42"]
            [Id "$id"]
            [WhiteElo "2881"]
            [BlackElo "2628"]
            [EventDate "2014.06.02"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [WhiteFideId "1503014"]
            [BlackFideId "1500015"]
            [Opening "Swiss Cheese Variation, Sicilian"]
            
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.Qe2 e5 7.Nf5 Nc6 8.O-O g6 9.Ne3 Bg7 10.Nc3 O-O 11.Ned5 Nxd5 12.Nxd5 d6 13.Be3 Be6 14.Bc4 b5 15.Bb3 Nd4 16.Bxd4 exd4 1-0
        """.trimIndent()

        assertEquals(pgn, game.toString())
        val t2 = now()
        val time = t2.toEpochMilliseconds() - t1.toEpochMilliseconds()
        logger.debug("deleteFromInclusive time[ms]=$time")
    }

    fun extractMoves(pgnString: String): List<String> {
        val cleanString = pgnString
            .replace("\n", " ")
            .trim()
        val regex = Regex("\\s*(?:\\d+\\.)?\\s*([\\w+#=xO-]+)")
        val moves = regex.findAll(cleanString)
            .map { it.groupValues[1] }
            .filter { it.isNotBlank() && it.first().isLetter() }
            .toList()
        return moves
    }
}