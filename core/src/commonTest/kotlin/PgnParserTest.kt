package io.github.lunalobos.chess4kt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

class PgnParserTest {

    private companion object {
        val logger = getLogger("io.github.lunalobos.chess4kt.PgnParserTest")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun engines() {
        val pgnInput = """
            [Event "TCEC 20 Premier 2020"]
            [Site "tcec-chess.com INT"]
            [Date "2020.04.06"]
            [Round "33.1"]
            [White "LCZero v0.24-sv-t60-3010"]
            [Black "KomodoMCTS 2503.05"]
            [Result "1-0"]
            [WhiteElo "3958"]
            [BlackElo "3809"]
            [ECO "B42"]
            [EventDate "2020.03.17"]
            [Opening "Sicilian"]
            [Variation "Kan, 5.Bd3"]
            
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.O-O Qc7 7.Qe2 d6 8.f4 
            Nbd7 9.c4 e5 10.Nf3 Be7 11.Nc3 O-O 12.Kh1 b5 13.cxb5 Nc5 14.Bc2 axb5 15.b4
            Ne6 16.Nxb5 Qb6 17.fxe5 Ba6 18.Bd3 dxe5 19.Be3 Qb8 20.Ng5 Bxb4 21.Nxe6 
            fxe6 22.Rab1 Bxb5 23.Bxb5 Qd6 24.a4 h6 25.h3 Bc5 26.Bxc5 Qxc5 27.Kh2 Nh7 
            28.Qg4 Rxf1 29.Rxf1 Qe7 30.Qg3 Qd6 31.Rf3 Qd4 32.Qg6 Ng5 33.Rf1 Rb8 34.a5 
            Qxe4 35.Qxe4 Nxe4 36.Bc6 Nc5 37.Rc1 Na6 38.Bf3 e4 39.Bxe4 Rb5 40.Bg6 Kf8 
            41.Rf1+ Kg8 42.Ra1 Rd5 43.Ra3 Kf8 44.Bd3 Rd6 45.Kg3 e5 46.Bc4 Ke7 47.Re3 
            Rg6+ 48.Kh2 Rc6 49.Rxe5+ Kf6 50.Re4 Nc7 51.Bg8 Ra6 52.Ra4 Ra7 53.Bc4 Na6 
            54.Bxa6 Rxa6 55.Kg3 Ke7 56.Kf4 Kd7 57.h4 Kd6 58.g3 Ke7 59.Ke5 Kd7 60.Ra3 
            h5 61.Kf5 Ke8 62.Kg5 Kf8 63.Kxh5 Kf7 64.Kg4 Ke7 65.Kg5 Kf7 66.Kf4 Ra8 67.
            a6 Ke6 68.a7 Kd6 69.h5 Kd5 70.h6 Rf8+ 71.Kg5 Rh8 72.a8=Q+ Rxa8 73.Rxa8 1-0
            
            [Event "TCEC 20 Premier 2020"]
            [Site "tcec-chess.com INT"]
            [Date "2020.03.29"]
            [Round "20.3"]
            [White "Houdini 6.03"]
            [Black "Stoofvlees II a14"]
            [Result "1/2-1/2"]
            [WhiteElo "3854"]
            [BlackElo "3846"]
            [ECO "B42"]
            [EventDate "2020.03.17"]
            [Opening "Sicilian"]
            [Variation "Kan, 5.Bd3"]
            
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.O-O d6 7.c4 b6 8.Nc3 Bb7 
            9.f4 Be7 10.Be3 O-O 11.g4 Re8 12.g5 Nfd7 13.Qd2 Nc6 14.Nxc6 Bxc6 15.b4 g6 
            16.Rad1 Rc8 17.h4 h5 18.Qf2 Bf8 19.Bd4 Qc7 20.f5 Ne5 21.Bxe5 dxe5 22.c5 
            bxc5 23.Bxa6 exf5 24.b5 Bb7 25.exf5 c4 26.b6 Qc6 27.Bxb7 Qxb7 28.fxg6 fxg6
            29.Nd5 Bg7 30.Qg2 Rc6 31.Qe4 Kh8 32.Rb1 Rb8 33.Ne7 Rxb6 34.Nxg6+ Kg8 35.
            Qxc4+ Kh7 36.Nxe5 Rc8 37.Qd3+ Kg8 38.Ng6 Rc3 39.Qd5+ Qxd5 40.Ne7+ Kh7 41.
            Nxd5 Rxb1 42.Rxb1 Rc5 43.Ne7 Bf8 44.Rb7 Rc4 45.Kg2 Bxe7 46.Rxe7+ Kg8 47.
            Kh3 Ra4 48.a3 Rxa3+ 49.Kg2 Rb3 50.g6 Rb6 51.g7 Rb4 52.Re8+ Kxg7 1/2-1/2
            
            [Event "TCEC 20 Premier 2020"]
            [Site "tcec-chess.com INT"]
            [Date "2020.03.25"]
            [Round "14.3"]
            [White "Stoofvlees II a14"]
            [Black "Houdini 6.03"]
            [Result "1/2-1/2"]
            [WhiteElo "3846"]
            [BlackElo "3854"]
            [ECO "B42"]
            [EventDate "2020.03.17"]
            [Opening "Sicilian"]
            [Variation "Kan, 5.Bd3"]
            
            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Nf6 6.O-O d6 7.c4 b6 8.Nc3 Bb7 
            9.f4 Be7 10.Be3 Nbd7 11.f5 e5 12.Nb3 O-O 13.Qf3 b5 14.cxb5 d5 15.bxa6 dxe4
            16.Bxe4 Bxe4 17.Nxe4 Rxa6 18.Nbd2 Qb8 19.b3 Rd8 20.h3 h6 21.Rfd1 Nxe4 22.
            Nxe4 Nf6 23.Rxd8+ Bxd8 24.Qe2 Rc6 25.Bf2 Bb6 26.Bxb6 Rxb6 27.Rc1 Nxe4 28.
            Qxe4 Ra6 29.Rc2 Kh7 30.Kh2 f6 31.Qg4 Ra7 32.Re2 Rc7 33.Re4 Rc1 34.Qg6+ Kh8
            35.Qg3 Rf1 36.Rc4 Rxf5 37.Qe1 Rf4 38.Rxf4 exf4 39.Kh1 Qd6 40.a4 Qd3 41.
            Qe8+ Kh7 42.Qb5 Qe3 43.Qf1 Qxb3 44.a5 g5 45.a6 Kg7 46.Qe2 Qa3 47.h4 Qa1+ 
            48.Kh2 Qa3 49.h5 Qd6 50.Kh1 Qa3 51.Qb5 Qc1+ 52.Kh2 Qa3 53.Kg1 Qe3+ 54.Kf1 
            Qc1+ 55.Ke2 Qc2+ 56.Kf3 Qd1+ 57.Ke4 Qc2+ 58.Kd4 Qf2+ 59.Kc4 Qe2+ 60.Kc5 
            Qe5+ 61.Kb6 Qb8+ 62.Ka5 Qd8+ 63.Kb4 Qd4+ 64.Qc4 1/2-1/2
        """.trimIndent()
        val t1 = now()
        val games = parseGames(pgnInput)
        assertEquals(games[0].result, Game.Result.WHITE_WIN)
        assertEquals(games[1].result, Game.Result.DRAW)
        assertEquals(games[2].result, Game.Result.DRAW)
        val t2 = now()
        logger.debug("engines time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @Test
    @OptIn(ExperimentalTime::class)
    fun masters(){
        val pgnInput = """
            [Event "Zurich CC Blitz 2014"]
            [Site "Zurich SUI"]
            [Date "2014.01.29"]
            [Round "1.1"]
            [White "Carlsen,M"]
            [Black "Caruana,F"]
            [Result "0-1"]
            [WhiteElo "2872"]
            [BlackElo "2782"]
            [ECO "B42"]
            [EventDate "2014.01.29"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [Opening "Sicilian"]
            [Variation "Kan, Polugaievsky variation"]
            [WhiteFideId "1503014"]
            [BlackFideId "2020009"]

            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Bc5 6.c3 d6 7.Nd2 Nf6 8.O-O O-O
            9.a4 e5 10.N4b3 Ba7 11.Nc4 Be6 12.Qe2 Nc6 13.Bg5 h6 14.Bh4 g5 15.Bg3 Ne7 
            16.Nbd2 Ng6 17.Rfe1 h5 18.h3 h4 19.Bh2 g4 20.Kh1 gxh3 21.gxh3 Bxh3 22.Rg1 
            Kg7 23.Ne3 Bxe3 24.Qxe3 Ng4 25.Qf3 Qf6 26.Rxg4 Qxf3+ 27.Nxf3 Bxg4 28.Nxh4 
            Nxh4 29.Rg1 Nf3 30.Rxg4+ Kf6 31.Rg3 Nxh2 32.Kxh2 Rh8+ 33.Kg2 Rag8 34.Bc4 
            Rxg3+ 35.fxg3 a5 0-1
            
            [Event "49th Biel Masters Rapid"]
            [Site "Biel SUI"]
            [Date "2016.07.24"]
            [Round "2"]
            [White "Vachier Lagrave,M"]
            [Black "Svidler,P"]
            [Result "0-1"]
            [WhiteElo "2798"]
            [BlackElo "2759"]
            [ECO "B42"]
            [EventDate "2016.07.24"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [Opening "Sicilian"]
            [Variation "Kan, Polugaievsky variation"]
            [WhiteFideId "623539"]
            [BlackFideId "4102142"]

            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Bc5 6.Nb3 Be7 7.Be3 Nf6 8.N1d2 
            d6 9.g4 h5 10.gxh5 Nxh5 11.Qe2 Bg5 12.Nc4 Nf4 13.Qg4 Rh4 14.Qg3 Rh3 15.
            Bxf4 Rxg3 16.hxg3 Bh6 17.Bxh6 gxh6 18.Rxh6 Ke7 19.O-O-O b5 20.Nca5 Qb6 21.
            f4 Nd7 22.Kb1 Bb7 23.f5 e5 24.f6+ Nxf6 25.Nxb7 Qxb7 26.Rf1 Nd7 27.g4 Rg8 
            28.Nc1 Rxg4 29.Ne2 Rg6 30.Rh3 b4 31.Ng3 Nc5 32.Bc4 Rf6 33.Rxf6 Kxf6 34.
            Rh6+ Kg5 35.Rh7 Nxe4 36.Rxf7 Qc6 37.Rg7+ Kf4 38.Ne2+ Ke3 39.Bd3 Nc5 40.Nc1
            e4 41.Bf1 d5 42.Rg2 Qf6 43.Re2+ Kd4 44.Bg2 a5 45.Rd2+ Ke5 46.c3 bxc3 47.
            bxc3 Na4 48.Kc2 Qc6 49.Ne2 Qc4 0-1
            
            [Event "Crocus City Stars Rapid"]
            [Site "Krasnogorsk RUS"]
            [Date "2022.10.01"]
            [Round "6.5"]
            [White "Karjakin,Sergey"]
            [Black "Vallejo Pons,F"]
            [Result "0-1"]
            [WhiteElo "2747"]
            [BlackElo "2711"]
            [ECO "B42"]
            [EventDate "2022.09.30"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [Opening "Sicilian"]
            [Variation "Kan, Polugaievsky variation"]
            [WhiteFideId "14109603"]
            [BlackFideId "2205530"]

            1.e4 c5 2.Nf3 e6 3.d4 cxd4 4.Nxd4 a6 5.Bd3 Bc5 6.Nb3 Ba7 7.Bf4 Ne7 8.Nc3 
            Nbc6 9.Qd2 O-O 10.Bd6 Bb8 11.Bxb8 Rxb8 12.f4 d5 13.e5 b5 14.O-O-O Bd7 15.
            Qe3 b4 16.Ne2 a5 17.Kb1 Qc7 18.Qh3 Ng6 19.Nbd4 a4 20.Nxc6 Qxc6 21.Nd4 Qb6 
            22.Bxg6 fxg6 23.Qe3 Rbc8 24.g3 Rc4 25.h4 a3 26.b3 Rc3 27.Rd3 Rfc8 28.Rhd1 
            Bb5 29.Rxc3 Rxc3 30.Qf2 h5 31.Rd2 Ba6 32.Rd1 Kf7 33.Qg1 Bb5 34.Qf2 Ba6 35.
            Rd2 Rc8 36.Kc1 Qc7 37.Qf3 Qc3 38.Qxc3 Rxc3 39.Rg2 Bd3 40.Kd2 Be4 41.Rg1 
            Bf5 42.Rg2 Ke8 43.Ke1 Kd7 44.Kd2 Ke7 45.Kd1 Kd7 46.Kd2 Kc8 47.Kd1 Kb7 48.
            Nb5 Rf3 49.Ke2 Bg4 50.Ke1 Kb6 51.Nd6 Kc5 52.Kd2 Rf1 53.c3 bxc3+ 54.Kxc3 
            d4+ 55.Kd2 Ra1 56.Kd3 Kd5 57.Rc2 Rd1+ 58.Rd2 Rg1 59.Rc2 Bf5+ 60.Nxf5 gxf5 
            61.b4 Rxg3+ 62.Kd2 Rg2+ 63.Kd3 Rg3+ 64.Kd2 Rg1 65.Kd3 Rd1+ 66.Rd2 Rc1 67.
            Rc2 Rd1+ 68.Rd2 Rb1 69.Rc2 Rxb4 70.Rc7 Rb2 71.Rd7+ Kc6 72.Rd6+ Kc5 73.Rxe6
            Rxa2 74.Ra6 Rf2 75.e6 Rxf4 76.e7 Rf3+ 77.Kd2 Re3 78.Rxa3 Rxe7 79.Ra5+ Kc4 
            80.Rxf5 g6 81.Rf6 Re4 82.Rc6+ Kd5 83.Rxg6 Rxh4 84.Rg8 Ke4 85.Re8+ Kf3 86.
            Re5 Kf4 87.Re1 Rh3 88.Rf1+ Rf3 89.Rh1 Kg4 90.Rg1+ Rg3 91.Re1 d3 92.Re4+ 
            Kh3 93.Rf4 h4 94.Ke1 Rg2 95.Rf3+ Rg3 96.Rf4 Rg1+ 97.Kd2 Kg3 98.Rf8 h3 99.
            Rg8+ Kh2 100.Rf8 Rg3 101.Ke1 Kh1 102.Kf1 d2 103.Ke2 Rg2+ 104.Kd1 Kg1 0-1
        """.trimIndent()

        val t1 = now()
        val games = parseGames(pgnInput)
        assertEquals(games[0].result, Game.Result.BLACK_WIN)
        assertEquals(games[1].result, Game.Result.BLACK_WIN)
        assertEquals(games[2].result, Game.Result.BLACK_WIN)
        val t2 = now()
        logger.debug("masters time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun petrovDefense(){
        val pgnInput = """
            [Event "Jerusalem Masters 2025"]
            [Site "Jerusalem, Occupied Country"]
            [Date "2025.12.01"]
            [Round "5.5"]
            [White "Nepomniachtchi,I"]
            [Black "Gorshtein,Ido"]
            [Result "1-0"]
            [WhiteElo "2732"]
            [BlackElo "2554"]
            [ECO "C42"]
            [EventDate "2025.11.30"]
            [WhiteTitle "GM"]
            [BlackTitle "GM"]
            [Opening "Petrov"]
            [Variation "classical attack"]
            [WhiteFideId "4168119"]
            [BlackFideId "2815532"]

            1.e4 e5 2.Nf3 Nf6 3.Nxe5 d6 4.Nf3 Nxe4 5.d4 d5 6.Bd3 Be7 7.O-O Nc6 8.Nbd2 
            Nxd2 9.Qxd2 $5 
                ( 9.Bxd2 Bg4 10.c3 O-O 11.h3 Bh5 )
                ( 9.Bxd2 Bg4 10.c3 O-O 11.h3 Bh5 12.g3 Qd7 13.Kg2 Bg6 14.Bf4 Be4 15.
                Re1 f5 16.Kh2 Bd6 17.Ne5 Qe7 18.Qd2 Bxe5 19.Bxe5 Nxe5 20.dxe5 Rad8 {
                1/2-1/2(26) Petrov,N (2576) - Gorshtein,Ido (2537) / 25th European 
                Teams (3.12), Batumi GEO 2025} )
            9...O-O 10.c3 Bg4 11.Ne1 Qd7 12.Qc2 h6 13.h3 Be6 14.f4 f5 15.Nf3 Bd6 16.
            Bd2 a6 17.Rae1 Rae8 18.Nh4 Ne7 19.Re2 g6 20.g4 Kh8 $6 {Blacks blunder a 
            pawn.} 
                ( 20...Qc8 21.Rfe1 Bd7 22.gxf5 Nxf5 23.Nxg6 $16 
                    ( 23.Nxf5 Rxe2 24.Rxe2 Bxf5 25.Bxf5 Rxf5 26.Qd3 $17 )
                23...Rxe2 24.Rxe2 Rf6 25.Rg2 Ng7 26.f5 )
                ( 20...Rf6 $1 21.g5 hxg5 22.fxg5 Rff8 23.Bf4 c5 $10 )
            21.Rfe1 Bf7 22.gxf5 Nxf5 23.Nxf5 Rxe2 24.Rxe2 gxf5 25.Bxf5 Rg8+ 26.Rg2 
            Rxg2+ 27.Kxg2 Be6 28.Bxe6 Qxe6 29.Qd3 Kg7 30.f5 Qe4+ 31.Qxe4 dxe4 32.Kf2 
            Kf6 33.Bxh6 Kxf5 34.h4 Be7 35.h5 Kg4 36.Ke3 Kxh5 37.Bf4 c6 38.Kxe4 Kg6 39.
            Ke5 Kf7 40.Kf5 b5 41.Ke5 Ke8 42.Ke6 a5 43.Bd6 Bd8 44.Bb8 Be7 45.Ba7 Bg5 
            46.Kd6 Bc1 47.b3 Bb2 48.c4 1-0
        """.trimIndent()

        val t1 = now()
        val games = parseGames(pgnInput)
        assertEquals(games[0].result, Game.Result.WHITE_WIN)
        val t2 = now()
        logger.debug("petrov time[ms]=${t2.toEpochMilliseconds() - t1.toEpochMilliseconds()}")
    }
}