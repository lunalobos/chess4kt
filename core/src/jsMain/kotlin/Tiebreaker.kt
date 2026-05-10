package io.github.lunalobos.chess4kt.js

@JsExport
/**
 * Class for defining tournament tie-breaking strategies.
 *
 * @author lunalobos
 * @since 1.0.0-beta.8
 */
class Tiebreaker internal constructor(internal val backedTiebreaker: io.github.lunalobos.chess4kt.Tiebreaker) {
    /**
     * Calculates the specific tie-breaker value for a given [player].
     * @param player The participant whose performance is being evaluated.
     * @return A [Score] representing the calculated tie-breaker points.
     */
    fun getValue(player: Player): Score {
        return Score(backedTiebreaker.getValue(player.backedPlayer))
    }
}