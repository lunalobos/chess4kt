package io.github.lunalobos.chess4kt

internal class CheckMaskGenerator(private val visibleMetrics: VisibleMetrics) {

    fun checkMaskWhite(kingSquare: Int, enemies: Long, friends: Long, bitboards: LongArray): Long {
        val empty = (enemies or friends).inv()
        var checkMask = 0L
        for (j in rookDirections) {
            val visibleEmptyOrFriendsRD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                enemies,
                friends
            )
            val friendsRD = visibleEmptyOrFriendsRD and empty.inv()

            val visibleEmptyOrEnemyRD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                friends and friendsRD.inv(),
                enemies and friendsRD.inv(),
            )
            val enemiesThreadsRD =
                (visibleEmptyOrEnemyRD and (bitboards[Piece.BR.ordinal - 1] or bitboards[Piece.BQ.ordinal - 1]))
            checkMask = checkMask or (if (isPresent(enemiesThreadsRD)) (friendsRD or visibleEmptyOrEnemyRD) else 0L)
        }
        for (j in bishopDirections) {
            val visibleEmptyOrFriendsBD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                enemies,
                friends
            )
            val friendsBD = visibleEmptyOrFriendsBD and empty.inv()

            val visibleEmptyOrEnemyBD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                friends and friendsBD.inv(),
                enemies and friendsBD.inv(),
            )
            val enemiesThreadsBD =
                (visibleEmptyOrEnemyBD and (bitboards[Piece.BB.ordinal - 1] or bitboards[Piece.BQ.ordinal - 1]))
            checkMask = checkMask or (if (isPresent(enemiesThreadsBD)) (friendsBD or visibleEmptyOrEnemyBD) else 0L)
        }
        //logger.traceExit("checkMask",checkMask)
        return checkMask
    }

    fun checkMaskBlack(kingSquare: Int, enemies: Long, friends: Long, bitboards: LongArray): Long {
        val empty = (enemies or friends).inv()
        var checkMask = 0L
        for (j in rookDirections) {
            val visibleEmptyOrFriendsRD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                enemies,
                friends
            )
            val friendsRD = visibleEmptyOrFriendsRD and empty.inv()

            val visibleEmptyOrEnemyRD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                friends and friendsRD.inv(),
                enemies and friendsRD.inv(),
            )
            val enemiesThreadsRD =
                (visibleEmptyOrEnemyRD and (bitboards[Piece.WR.ordinal - 1] or bitboards[Piece.WQ.ordinal - 1]))
            checkMask = checkMask or (if (isPresent(enemiesThreadsRD)) (friendsRD or visibleEmptyOrEnemyRD) else 0L)
        }
        for (j in bishopDirections) {
            val visibleEmptyOrFriendsBD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                enemies,
                friends
            )
            val friendsBD = visibleEmptyOrFriendsBD and empty.inv()

            val visibleEmptyOrEnemyBD: Long = visibleMetrics.computeVisible(
                kingSquare,
                intArrayOf(j),
                queenMegamatrix[kingSquare],
                friends and friendsBD.inv(),
                enemies and friendsBD.inv(),
            )
            val enemiesThreadsBD =
                (visibleEmptyOrEnemyBD and (bitboards[Piece.WB.ordinal - 1] or bitboards[Piece.WQ.ordinal - 1]))
            checkMask = checkMask or (if (isPresent(enemiesThreadsBD)) (friendsBD or visibleEmptyOrEnemyBD) else 0L)
        }
        //logger.traceExit("checkMask",checkMask)
        return checkMask
    }

}
