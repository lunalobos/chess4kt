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

    @Test
    fun fen(){
        val pgn = """
            [Event "chess.com SpeedChess 2022"]
            [Site "chess.com INT"]
            [Date "2022.12.18"]
            [Round "4.19"]
            [White "Nakamura,Hi"]
            [Black "Carlsen,M"]
            [Result "1/2-1/2"]
            [WhiteElo "2768"]
            [BlackElo "2859"]
            [ECO "B98"]
            [EventDate "2022.11.23"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [Opening "Sicilian"]
            [Variation "Najdorf, 7...Be7"]
            [WhiteFideId "2016192"]
            [BlackFideId "1503014"]

            1.e4 c5 2.Nf3 d6 3.d4 cxd4 4.Nxd4 Nf6 5.Nc3 a6 6.Bg5 e6 7.f4 Be7 8.Qf3 Bd7
            9.O-O-O Nc6 10.Bc4 h6 11.Bh4 Qb6 12.Bf2 Qc7 13.Bb3 Na5 14.Kb1 O-O-O 15.f5 
            Nxb3 16.cxb3 Kb8 17.Rc1 Qa5 18.b4 Qxb4 19.a3 Qc4 20.Nd5 exd5 21.Rxc4 dxc4 
            22.h3 Rhe8 23.Re1 Rc8 24.Ne2 Bc6 25.Nc3 Bd8 26.Bd4 Ba5 27.Bxf6 gxf6 28.Qf4
            Re5 29.Qxh6 Bxc3 30.bxc3 Bxe4+ 31.Ka1 Rcc5 32.Qxf6 Rcd5 33.Qxf7 Ka7 34.f6 
            Rd2 35.Qc7 Rb5 36.Rxe4 Rd1+ 37.Ka2 Rd2+ 38.Ka1 Rd1+ 39.Ka2 Rd2+ 40.Ka1 
            1/2-1/2
        """.trimIndent()

        val game = parseGames(pgn).first();

        val fens = listOf(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2",
            "rnbqkbnr/pp2pppp/3p4/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 3",
            "rnbqkbnr/pp2pppp/3p4/2p5/3PP3/5N2/PPP2PPP/RNBQKB1R b KQkq d3 0 3",
            "rnbqkbnr/pp2pppp/3p4/8/3pP3/5N2/PPP2PPP/RNBQKB1R w KQkq - 0 4",
            "rnbqkbnr/pp2pppp/3p4/8/3NP3/8/PPP2PPP/RNBQKB1R b KQkq - 0 4",
            "rnbqkb1r/pp2pppp/3p1n2/8/3NP3/8/PPP2PPP/RNBQKB1R w KQkq - 1 5",
            "rnbqkb1r/pp2pppp/3p1n2/8/3NP3/2N5/PPP2PPP/R1BQKB1R b KQkq - 2 5",
            "rnbqkb1r/1p2pppp/p2p1n2/8/3NP3/2N5/PPP2PPP/R1BQKB1R w KQkq - 0 6",
            "rnbqkb1r/1p2pppp/p2p1n2/6B1/3NP3/2N5/PPP2PPP/R2QKB1R b KQkq - 1 6",
            "rnbqkb1r/1p3ppp/p2ppn2/6B1/3NP3/2N5/PPP2PPP/R2QKB1R w KQkq - 0 7",
            "rnbqkb1r/1p3ppp/p2ppn2/6B1/3NPP2/2N5/PPP3PP/R2QKB1R b KQkq f3 0 7",
            "rnbqk2r/1p2bppp/p2ppn2/6B1/3NPP2/2N5/PPP3PP/R2QKB1R w KQkq - 1 8",
            "rnbqk2r/1p2bppp/p2ppn2/6B1/3NPP2/2N2Q2/PPP3PP/R3KB1R b KQkq - 2 8",
            "rn1qk2r/1p1bbppp/p2ppn2/6B1/3NPP2/2N2Q2/PPP3PP/R3KB1R w KQkq - 3 9",
            "rn1qk2r/1p1bbppp/p2ppn2/6B1/3NPP2/2N2Q2/PPP3PP/2KR1B1R b kq - 4 9",
            "r2qk2r/1p1bbppp/p1nppn2/6B1/3NPP2/2N2Q2/PPP3PP/2KR1B1R w kq - 5 10",
            "r2qk2r/1p1bbppp/p1nppn2/6B1/2BNPP2/2N2Q2/PPP3PP/2KR3R b kq - 6 10",
            "r2qk2r/1p1bbpp1/p1nppn1p/6B1/2BNPP2/2N2Q2/PPP3PP/2KR3R w kq - 0 11",
            "r2qk2r/1p1bbpp1/p1nppn1p/8/2BNPP1B/2N2Q2/PPP3PP/2KR3R b kq - 1 11",
            "r3k2r/1p1bbpp1/pqnppn1p/8/2BNPP1B/2N2Q2/PPP3PP/2KR3R w kq - 2 12",
            "r3k2r/1p1bbpp1/pqnppn1p/8/2BNPP2/2N2Q2/PPP2BPP/2KR3R b kq - 3 12",
            "r3k2r/1pqbbpp1/p1nppn1p/8/2BNPP2/2N2Q2/PPP2BPP/2KR3R w kq - 4 13",
            "r3k2r/1pqbbpp1/p1nppn1p/8/3NPP2/1BN2Q2/PPP2BPP/2KR3R b kq - 5 13",
            "r3k2r/1pqbbpp1/p2ppn1p/n7/3NPP2/1BN2Q2/PPP2BPP/2KR3R w kq - 6 14",
            "r3k2r/1pqbbpp1/p2ppn1p/n7/3NPP2/1BN2Q2/PPP2BPP/1K1R3R b kq - 7 14",
            "2kr3r/1pqbbpp1/p2ppn1p/n7/3NPP2/1BN2Q2/PPP2BPP/1K1R3R w - - 8 15",
            "2kr3r/1pqbbpp1/p2ppn1p/n4P2/3NP3/1BN2Q2/PPP2BPP/1K1R3R b - - 0 15",
            "2kr3r/1pqbbpp1/p2ppn1p/5P2/3NP3/1nN2Q2/PPP2BPP/1K1R3R w - - 0 16",
            "2kr3r/1pqbbpp1/p2ppn1p/5P2/3NP3/1PN2Q2/PP3BPP/1K1R3R b - - 0 16",
            "1k1r3r/1pqbbpp1/p2ppn1p/5P2/3NP3/1PN2Q2/PP3BPP/1K1R3R w - - 1 17",
            "1k1r3r/1pqbbpp1/p2ppn1p/5P2/3NP3/1PN2Q2/PP3BPP/1KR4R b - - 2 17",
            "1k1r3r/1p1bbpp1/p2ppn1p/q4P2/3NP3/1PN2Q2/PP3BPP/1KR4R w - - 3 18",
            "1k1r3r/1p1bbpp1/p2ppn1p/q4P2/1P1NP3/2N2Q2/PP3BPP/1KR4R b - - 0 18",
            "1k1r3r/1p1bbpp1/p2ppn1p/5P2/1q1NP3/2N2Q2/PP3BPP/1KR4R w - - 0 19",
            "1k1r3r/1p1bbpp1/p2ppn1p/5P2/1q1NP3/P1N2Q2/1P3BPP/1KR4R b - - 0 19",
            "1k1r3r/1p1bbpp1/p2ppn1p/5P2/2qNP3/P1N2Q2/1P3BPP/1KR4R w - - 1 20",
            "1k1r3r/1p1bbpp1/p2ppn1p/3N1P2/2qNP3/P4Q2/1P3BPP/1KR4R b - - 2 20",
            "1k1r3r/1p1bbpp1/p2p1n1p/3p1P2/2qNP3/P4Q2/1P3BPP/1KR4R w - - 0 21",
            "1k1r3r/1p1bbpp1/p2p1n1p/3p1P2/2RNP3/P4Q2/1P3BPP/1K5R b - - 0 21",
            "1k1r3r/1p1bbpp1/p2p1n1p/5P2/2pNP3/P4Q2/1P3BPP/1K5R w - - 0 22",
            "1k1r3r/1p1bbpp1/p2p1n1p/5P2/2pNP3/P4Q1P/1P3BP1/1K5R b - - 0 22",
            "1k1rr3/1p1bbpp1/p2p1n1p/5P2/2pNP3/P4Q1P/1P3BP1/1K5R w - - 1 23",
            "1k1rr3/1p1bbpp1/p2p1n1p/5P2/2pNP3/P4Q1P/1P3BP1/1K2R3 b - - 2 23",
            "1kr1r3/1p1bbpp1/p2p1n1p/5P2/2pNP3/P4Q1P/1P3BP1/1K2R3 w - - 3 24",
            "1kr1r3/1p1bbpp1/p2p1n1p/5P2/2p1P3/P4Q1P/1P2NBP1/1K2R3 b - - 4 24",
            "1kr1r3/1p2bpp1/p1bp1n1p/5P2/2p1P3/P4Q1P/1P2NBP1/1K2R3 w - - 5 25",
            "1kr1r3/1p2bpp1/p1bp1n1p/5P2/2p1P3/P1N2Q1P/1P3BP1/1K2R3 b - - 6 25",
            "1krbr3/1p3pp1/p1bp1n1p/5P2/2p1P3/P1N2Q1P/1P3BP1/1K2R3 w - - 7 26",
            "1krbr3/1p3pp1/p1bp1n1p/5P2/2pBP3/P1N2Q1P/1P4P1/1K2R3 b - - 8 26",
            "1kr1r3/1p3pp1/p1bp1n1p/b4P2/2pBP3/P1N2Q1P/1P4P1/1K2R3 w - - 9 27",
            "1kr1r3/1p3pp1/p1bp1B1p/b4P2/2p1P3/P1N2Q1P/1P4P1/1K2R3 b - - 0 27",
            "1kr1r3/1p3p2/p1bp1p1p/b4P2/2p1P3/P1N2Q1P/1P4P1/1K2R3 w - - 0 28",
            "1kr1r3/1p3p2/p1bp1p1p/b4P2/2p1PQ2/P1N4P/1P4P1/1K2R3 b - - 1 28",
            "1kr5/1p3p2/p1bp1p1p/b3rP2/2p1PQ2/P1N4P/1P4P1/1K2R3 w - - 2 29",
            "1kr5/1p3p2/p1bp1p1Q/b3rP2/2p1P3/P1N4P/1P4P1/1K2R3 b - - 0 29",
            "1kr5/1p3p2/p1bp1p1Q/4rP2/2p1P3/P1b4P/1P4P1/1K2R3 w - - 0 30",
            "1kr5/1p3p2/p1bp1p1Q/4rP2/2p1P3/P1P4P/6P1/1K2R3 b - - 0 30",
            "1kr5/1p3p2/p2p1p1Q/4rP2/2p1b3/P1P4P/6P1/1K2R3 w - - 0 31",
            "1kr5/1p3p2/p2p1p1Q/4rP2/2p1b3/P1P4P/6P1/K3R3 b - - 1 31",
            "1k6/1p3p2/p2p1p1Q/2r1rP2/2p1b3/P1P4P/6P1/K3R3 w - - 2 32",
            "1k6/1p3p2/p2p1Q2/2r1rP2/2p1b3/P1P4P/6P1/K3R3 b - - 0 32",
            "1k6/1p3p2/p2p1Q2/3rrP2/2p1b3/P1P4P/6P1/K3R3 w - - 1 33",
            "1k6/1p3Q2/p2p4/3rrP2/2p1b3/P1P4P/6P1/K3R3 b - - 0 33",
            "8/kp3Q2/p2p4/3rrP2/2p1b3/P1P4P/6P1/K3R3 w - - 1 34",
            "8/kp3Q2/p2p1P2/3rr3/2p1b3/P1P4P/6P1/K3R3 b - - 0 34",
            "8/kp3Q2/p2p1P2/4r3/2p1b3/P1P4P/3r2P1/K3R3 w - - 1 35",
            "8/kpQ5/p2p1P2/4r3/2p1b3/P1P4P/3r2P1/K3R3 b - - 2 35",
            "8/kpQ5/p2p1P2/1r6/2p1b3/P1P4P/3r2P1/K3R3 w - - 3 36",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/3r2P1/K7 b - - 0 36",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/6P1/K2r4 w - - 1 37",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/K5P1/3r4 b - - 2 37",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/K2r2P1/8 w - - 3 38",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/3r2P1/K7 b - - 4 38",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/6P1/K2r4 w - - 5 39",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/K5P1/3r4 b - - 6 39",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/K2r2P1/8 w - - 7 40",
            "8/kpQ5/p2p1P2/1r6/2p1R3/P1P4P/3r2P1/K7 b - - 8 40"
        )

        val gameIterator = game.iterator();
        val fenIterator = fens.iterator();
        while (gameIterator.hasNext() && fenIterator.hasNext()) {
            val currentNode = gameIterator.next()
            val currentFen = fenIterator.next()
            assertEquals(currentFen, currentNode.position.fen)
        }
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