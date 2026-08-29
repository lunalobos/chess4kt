package io.github.lunalobos.chess4kt

internal class CheckInfoGenerator(private val visibleMetrics: VisibleMetrics) {

    fun checkInfoWhite(friends: Long, enemies: Long, bitboards: LongArray, kingSquare: Int): CheckInfo {
        val pawnsDirections = whitePawnMatrix2[kingSquare]
        var inCheckMask = 0L
        var checkCount = 0
        var isInCheck = false

        for (pawnDirection in pawnsDirections) {
            val enemyPawnDangerLocation = 1L shl pawnDirection
            val pawnScopeAsInt = isPresentAsInt(bitboards[Piece.BP.ordinal - 1] and enemyPawnDangerLocation)
            val pawnScope = pawnScopeAsInt == 1
            isInCheck = isInCheck || pawnScope
            inCheckMask = inCheckMask or if (pawnScope) enemyPawnDangerLocation else 0L
            checkCount += pawnScopeAsInt
        }

        var kingDirectionsBits = 0L
        for (square in kingMatrix[kingSquare]) {
            kingDirectionsBits = kingDirectionsBits or (1L shl square)
        }
        val kingScopeAsInt = isPresentAsInt(kingDirectionsBits and bitboards[Piece.BK.ordinal - 1])
        val kingScope = kingScopeAsInt == 1
        isInCheck = isInCheck || kingScope
        checkCount += kingScopeAsInt

        var knightDirectionsBits: Long
        for (square in knightMatrix[kingSquare]) {
            knightDirectionsBits = 1L shl square
            val knightScopeAsInt = isPresentAsInt(knightDirectionsBits and bitboards[Piece.BN.ordinal - 1])
            val knightScope = knightScopeAsInt == 1
            isInCheck = isInCheck || knightScope
            inCheckMask = inCheckMask or if (knightScope) knightDirectionsBits else 0L
            checkCount += knightScopeAsInt
        }

        val enemyBishopsAndQueens = bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BQ.ordinal - 1]
        for (i in bishopDirections) {
            val visible = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(i),
                queenMegamatrix[kingSquare],
                friends,
                enemies
            )
            val bishopScopeAsInt = isPresentAsInt(enemyBishopsAndQueens and visible)
            val bishopScope = bishopScopeAsInt == 1
            isInCheck = isInCheck || bishopScope
            inCheckMask = inCheckMask or if (bishopScope) visible else 0L
            checkCount += bishopScopeAsInt
        }

        val enemyRooksAndQueens = bitboards[Piece.BR.ordinal - 1] or bitboards[Piece.BQ.ordinal - 1]
        for (i in rookDirections) {
            val visible: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(i),
                queenMegamatrix[kingSquare],
                friends,
                enemies
            )
            val rookScopeAsInt = isPresentAsInt(enemyRooksAndQueens and visible)
            val rookScope = rookScopeAsInt == 1
            isInCheck = isInCheck or rookScope
            inCheckMask = inCheckMask or if (rookScope) visible else 0
            checkCount += rookScopeAsInt
        }

        inCheckMask = when (checkCount) {
            0 -> -1L
            1 -> inCheckMask
            else -> 0L
        }

        return CheckInfo(isInCheck, inCheckMask)
    }

    fun checkInfoBlack(friends: Long, enemies: Long, bitboards: LongArray, kingSquare: Int): CheckInfo {
        val pawnsDirections = blackPawnMatrix2[kingSquare]
        var inCheckMask = 0L
        var checkCount = 0
        var isInCheck = false

        for (pawnDirection in pawnsDirections) {
            val enemyPawnDangerLocation = 1L shl pawnDirection
            val pawnScopeAsInt = isPresentAsInt(bitboards[Piece.WP.ordinal - 1] and enemyPawnDangerLocation)
            val pawnScope = pawnScopeAsInt == 1
            isInCheck = isInCheck || pawnScope
            inCheckMask = inCheckMask or if (pawnScope) enemyPawnDangerLocation else 0L
            checkCount += pawnScopeAsInt
        }

        var kingDirectionsBits = 0L
        for (square in kingMatrix[kingSquare]) {
            kingDirectionsBits = kingDirectionsBits or (1L shl square)
        }
        val kingScopeAsInt = isPresentAsInt(kingDirectionsBits and bitboards[Piece.WK.ordinal - 1])
        val kingScope = kingScopeAsInt == 1
        isInCheck = isInCheck || kingScope
        checkCount += kingScopeAsInt

        var knightDirectionsBits: Long
        for (square in knightMatrix[kingSquare]) {
            knightDirectionsBits = 1L shl square
            val knightScopeAsInt = isPresentAsInt(knightDirectionsBits and bitboards[Piece.WN.ordinal - 1])
            val knightScope = knightScopeAsInt == 1
            isInCheck = isInCheck || knightScope
            inCheckMask = inCheckMask or if (knightScope) knightDirectionsBits else 0L
            checkCount += knightScopeAsInt
        }

        val enemyBishopsAndQueens = bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WQ.ordinal - 1]
        for (i in bishopDirections) {
            val visible = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(i),
                queenMegamatrix[kingSquare],
                friends,
                enemies
            )
            val bishopScopeAsInt = isPresentAsInt(enemyBishopsAndQueens and visible)
            val bishopScope = bishopScopeAsInt == 1
            isInCheck = isInCheck || bishopScope
            inCheckMask = inCheckMask or if (bishopScope) visible else 0L
            checkCount += bishopScopeAsInt
        }

        val enemyRooksAndQueens = bitboards[Piece.WR.ordinal - 1] or bitboards[Piece.WQ.ordinal - 1]
        for (i in rookDirections) {
            val visible: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(i),
                queenMegamatrix[kingSquare],
                friends,
                enemies
            )
            val rookScopeAsInt = isPresentAsInt(enemyRooksAndQueens and visible)
            val rookScope = rookScopeAsInt == 1
            isInCheck = isInCheck or rookScope
            inCheckMask = inCheckMask or if (rookScope) visible else 0
            checkCount += rookScopeAsInt
        }

        inCheckMask = when (checkCount) {
            0 -> -1L
            1 -> inCheckMask
            else -> 0L
        }

        return CheckInfo(isInCheck, inCheckMask)
    }
}
