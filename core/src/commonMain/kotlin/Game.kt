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

/**
 * Class for manipulating games. It allows starting a game from a configurable initial position and with various
 * configuration options, such as the game type (match or analysis), and how to apply the three-fold repetition
 * and 50-move rules.
 *
 * This class implements [Iterable], allowing easy traversal over the nodes of the main line.
 * The toString function returns the game in PGN format.
 *
 * @since 1.0.0-beta.1
 * @author lunalobos
 */
class Game : Iterable<Game.Node> {

    internal companion object {
        val logger = getLogger("io.github.lunalobos.chess4kt.Game")
    }

    /**
     * The root node of the game tree. The setter is protected if the game is immutable.
     */
    var root: Node = RootNode(positionOf())
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * Standard PGN tags (e.g., Event, Site, Date, Round, White, Black, Result).
     */
    val tags = mutableMapOf<String, String>()

    /**
     * Indicates whether a position has been repeated three times (non-terminal).
     */
    var threeRepetitions: Boolean = false
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * Indicates whether a position has been repeated five times, leading to an automatic draw according to FIDE rules
     * (terminal).
     */
    var fiveRepetitions: Boolean = false
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * Indicates that a three-fold repetition draw can be claimed, as the repetition is impending (e.g., the current
     * move will complete the third repetition).
     */
    var threeRepetitionsWarning: Boolean = false
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * Indicates whether the 50-move half-move limit has been reached (non-terminal).
     */
    var fiftyMoves: Boolean = false
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * Indicates whether the 75-move half-move limit has been reached, leading to an automatic draw (terminal).
     */
    var seventyFiveMoves: Boolean = false
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }
    private val gameMode: GameMode

    /**
     * Stores the current ECO (Encyclopedia of Chess Openings) information for the main line.
     * Setting this property will be ignored if the game is immutable (i.e., [GameMode.MATCH] and [result] is set).
     */
    var ecoInfo: EcoInfo? = null
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * The final result of the game.
     * Setting this property will update the "Result" tag in [tags] and set the game as immutable if [GameMode.MATCH] is used.
     */
    var result: Result? = null
        set(value) {
            if (immutable()) {
                return
            }
            tags["result"] = value?.str ?: "unknown"
            field = value
        }

    /**
     * Determines how the three-fold repetition rule is enforced for this game instance.
     * Setting this property will be ignored if the game is immutable.
     */
    val threeRepetitionsMode: ThreeRepetitionsMode

    /**
     * Determines how the fifty-move rule is enforced for this game instance.
     * Setting this property will be ignored if the game is immutable.
     */
    val fiftyMovesRuleMode: FiftyMovesRuleMode

    internal val repetitions = Repetitions()

    /**
     * An optional comment placed immediately after the game's result tag in PGN.
     * This comment is enclosed in braces {}.
     * Setting this property will be ignored if the game is immutable.
     */
    var finalComment: String? = null
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * An optional end-of-line comment placed immediately after the game's result tag in PGN.
     * This comment follows a semicolon ; and extends until the end of the line.
     * Setting this property will be ignored if the game is immutable.
     */
    var finalEndLineComment: String? = null
        set(value) {
            if (immutable()) {
                return
            }
            field = value
        }

    /**
     * A developer-provided unique identifier for serialization or tracking purposes (e.g., UUID or String).
     */
    val id: Any?

    internal constructor(
        tags: Map<String, String>,
        gameMode: GameMode,
        threeRepetitionsMode: ThreeRepetitionsMode,
        fiftyMovesRuleMode: FiftyMovesRuleMode,
        root: Node? = null,
        idSupplier: () -> Any? = { null }
    ) {
        // root
        if (root != null) {
            this.root = root
        } else {
            this.root = RootNode(tags["fen"]?.let { positionOf(it) } ?: positionOf())
        }

        this.gameMode = gameMode
        this.threeRepetitionsMode = threeRepetitionsMode
        this.fiftyMovesRuleMode = fiftyMovesRuleMode
        this.tags.putAll(tags)
        this.id = idSupplier() ?: tags["id"]
        this.tags["id"] = id.toString()
    }

    internal constructor(
        tags: Map<String, String>,
        gameMode: GameMode,
        threeRepetitionsMode: ThreeRepetitionsMode,
        fiftyMovesRuleMode: FiftyMovesRuleMode,
        idSupplier: () -> Any? = { null }
    ) {
        // root
        this.root = RootNode(tags["fen"]?.let { positionOf(it) } ?: positionOf())
        this.gameMode = gameMode
        this.threeRepetitionsMode = threeRepetitionsMode
        this.fiftyMovesRuleMode = fiftyMovesRuleMode
        this.tags.putAll(tags)
        this.id = idSupplier() ?: tags["id"]
        this.tags["id"] = id.toString()
    }

    private fun immutable(): Boolean {
        return result != null && gameMode.value
    }

    internal fun checkRepetitions(node: Node) {
        val count = repetitions.push(node)
        if (gameMode == GameMode.MATCH && count == 5) {
            fiveRepetitions = true
            finalEndLineComment = "five repetitions"
            result = Result.DRAW
        }
        if (count == 3) {
            when (threeRepetitionsMode) {
                ThreeRepetitionsMode.AWARE -> {
                    threeRepetitions = true
                }

                ThreeRepetitionsMode.STRICT -> {
                    threeRepetitions = true
                    finalEndLineComment = "three repetitions"
                    result = Result.DRAW
                }

                ThreeRepetitionsMode.IGNORE -> {}
            }
        } else {
            threeRepetitions = false
        }
        if (repetitions.warning(node)) {
            when (threeRepetitionsMode) {
                ThreeRepetitionsMode.AWARE -> threeRepetitionsWarning = true
                ThreeRepetitionsMode.STRICT -> threeRepetitionsWarning = true
                ThreeRepetitionsMode.IGNORE -> {}
            }
        }
    }

    internal fun checkFiftyMoves(node: Node) {
        if (node.position.fiftyMoves) {
            when (fiftyMovesRuleMode) {
                FiftyMovesRuleMode.IGNORE -> {}
                FiftyMovesRuleMode.AWARE -> fiftyMoves = true
                FiftyMovesRuleMode.STRICT -> {
                    fiftyMoves = true
                    result = Result.DRAW
                }
            }
        } else {
            fiftyMoves = false
        }
    }

    internal fun checkSeventyFiveMoves(node: Node) {
        if (node.position.halfMovesCounter == 75) {
            seventyFiveMoves = true
            finalEndLineComment = "75 moves rule"
            result = Result.DRAW
        }
    }

    /**
     * Updates the instance's ECO ranking based on the last move of the main line. In match mode this is automatic,
     * but in analysis this function must be called, otherwise the game will not be ranked.
     *
     * @since 1.0.0-beta.1
     */
    fun updateEco() {
        val iterator = iterator()
        iterator.next() // discard root node
        while (iterator.hasNext()) {
            val node = iterator.next()
            ecoInfo = ecoInfo(node.position) ?: ecoInfo
        }
        tags["ECO"] = ecoInfo?.eco ?: "unknown"
        tags["opening"] = ecoInfo?.name ?: "unknown"
    }

    internal fun checkGameOver(node: Node) {
        if (node.position.gameOver) {
            result = if (node.position.draw) {
                Result.DRAW
            } else if (node.position.sideToMove == Side.WHITE) {
                Result.BLACK_WIN
            } else {
                Result.WHITE_WIN
            }
        }
    }

    private fun isMainLine(nodes: List<Node>): Boolean {
        for (i in 0..<nodes.size - 1) {
            val parent = nodes[i]
            val child = nodes[i + 1]
            val mainChild = parent.children[0]
            if (child !== mainChild) {
                return false
            }
        }
        return true
    }

    /**
     * Returns the PGN representation of the game, including tags, moves, and result.
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(tagsToString()).append("\n")
        iterator().asSequence()
            .filter { it is MoveNode }
            .forEach { sb.append(it.toString()) }
        sb.append(result?.str ?: "")
            .append(finalComment?.let { "{$it}" } ?: "")
            .append(finalEndLineComment?.let { " ; $it\n" } ?: "")

        return sb.toString()
    }

    private fun tagsToString(): String {
        val mainTags = arrayOf("event", "site", "date", "round", "white", "black", "result", "ECO")
        val sb = mainTags.asSequence()
            .map { tupleOf(it, tags[it] ?: "unknown") }
            .fold(StringBuilder()) { acc, (name, value) ->
                acc.append("[${name.capitalize()} \"$value\"]\n")
            }
        tags.entries.asSequence()
            .filter { it.key !in mainTags }
            .forEach { (name, value) ->
                sb.append("[${name.capitalize()} \"$value\"]\n")
            }
        return sb.toString()
    }

    /**
     * Returns an iterator that traverses the main line nodes of the game.
     */
    override fun iterator(): Iterator<Node> {
        return NodeIterator(root)
    }

    /**
     * Creates a deep copy of this game, converting its mode to [GameMode.ANALYSIS] and setting both repetition rules
     * to [ThreeRepetitionsMode.AWARE] and [FiftyMovesRuleMode.AWARE]. This makes the new instance fully mutable for
     * analysis.
     *
     * The idea behind idSupplier is to give the developer the flexibility to provide their own ID generator (idSupplier()).
     * This allows the object's ID to be custom-defined, which aids the developer in various operations such as serializing
     * the object, indexing it in an in-memory or on-disk database, or leveraging the ID in any way that facilitates or
     * enables operations.
     *
     * @param idSupplier a lambda that provides different IDs each time it is called
     *
     * @return the analysis deep copy of this [Game]
     * @since 1.0.0-beta.3
     */
    fun toAnalysis(idSupplier: () -> Any? = { null }): Game {
        return Game(
            mutableMapOf<String, String>().apply {
                putAll(tags)
            },
            GameMode.ANALYSIS,
            ThreeRepetitionsMode.AWARE,
            FiftyMovesRuleMode.AWARE,
            this@Game.root.copy(null),
            idSupplier
        ).apply {
            finalComment = this@Game.finalComment
            finalEndLineComment = this@Game.finalEndLineComment
            ecoInfo = this@Game.ecoInfo
            result = this@Game.result
            threeRepetitions = this@Game.threeRepetitions
            fiftyMoves = this@Game.fiftyMoves
        }
    }

    /**
     * Each game can be interpreted as a series of interconnected nodes in a tree. Each node is move in the game and
     * holds information about the board state after that move. Additionally, each node can have regular or end-of-line
     * comments, suffix annotations, as well as more than one child (from the second child onwards, they are
     * variations), and always only one parent node.
     */
    interface Node {
        /**
         * The position of the node, which is the result after executing the move.
         */
        val position: Position

        /**
         * The move of the node. It is null when it is the root node, which only has the starting position of the
         * game.
         */
        val move: Move?

        /**
         * The children of the node. The first child corresponds to the main line. The rest are variations (RAVs).
         */
        val children: MutableList<Node>

        /**
         * The comment that precedes the move number in PGN (e.g., "{Comment} 1. e4").
         */
        var initialComment: String?

        /**
         * The regular comment that follows the move and any suffix annotations (e.g., 1. e4 {Comment}).
         */
        var comment: String?

        /**
         * The end-of-line comment for the node, which follows a semicolon ; and goes until the end of the line.
         */
        var endLineComment: String?

        /**
         * The list of suffix annotations (NAGs) for the node.
         */
        var suffixAnnotations: List<Int>?

        /**
         * The parent node. It can only be null when it is the root node, which evidently cannot have a parent.
         */
        var parent: Node?

        /**
         * Appends a move and returns the added node. If the node already has a child, the added move
         * will be a variation (RAV). Returns the new node if the move is legal, or the current node if the move is illegal.
         */
        fun appendMove(
            move: String,
            initialComment: String? = null,
            comment: String? = null,
            endLineComment: String? = null,
            suffixAnnotations: List<Int>? = null,
            notation: Notation = Notation.SAN
        ): Node

        /**
         * Promotes the child at the given index to the primary variation (children[0]).
         */
        fun promoteChild(index: Int): Boolean {
            if (index < 0 || index >= children.size) {
                return false
            }
            val ravToPromote = children.removeAt(index)
            val deque = ArrayDeque(children)
            deque.addFirst(ravToPromote)
            children.clear()
            children.addAll(deque)
            return true
        }

        /**
         * Promotes this node to the main line.
         */
        fun promoteNode(): Boolean {
            var index = -1
            var currentIndex = 0
            for (child in parent?.children ?: listOf()) {
                if (child === this) {
                    index = currentIndex
                    break
                } else {
                    currentIndex++
                }
            }
            if (index == -1) {
                return false
            }
            return this.parent?.promoteChild(index) ?: false
        }

        /**
         * Removes the specified child node (variation) from the current node's list of children.
         */
        fun removeChild(node: Node): Boolean {
            var index = -1
            var currentIndex = 0
            for (child in children) {
                if (node === child) {
                    index = currentIndex
                    break
                } else {
                    currentIndex++
                }
            }
            if (index == -1) {
                return false
            }
            children.removeAt(index)
            return true
        }

        /**
         * Checks if the node has children.
         */
        fun hasChildren(): Boolean {
            return children.isNotEmpty()
        }

        /**
         * Indicates whether this node belongs to the main line (i.e., it is the first child of all its ancestors).
         */
        fun belongsToMainLine(): Boolean

        /**
         * Creates a deep copy of this node and its entire subtree, assigning the specified parent to the new copy.
         * This process is recursive; copying the root node copies the entire game tree.
         */
        fun copy(parent: Node?): Node

        /**
         * Converts a move to Standard Algebraic Notation (SAN).
         *
         * This function acts as a convenience wrapper to generate notation adapted to different
         * languages. If the [move] is null, it returns an empty string. If the specified
         * language is not predefined, a custom array of piece initials must be provided.
         *
         * Supported internal languages: "english", "spanish", "dutch", "french", "german", and "italian".
         *
         * @param language The desired language for piece initials (defaults to "english").
         * @param pieces An optional array of 12 strings for custom initials
         * (e.g., `["", "N", "B", "R", "Q", "K", "", "N", "B", "R", "Q", "K"]`).
         * @return A string representation of the move in SAN format (e.g., "Nf3", "exd5", "O-O").
         * @throws IllegalArgumentException If the language is unknown and a valid 12-element
         * pieces array is not provided.
         *
         * @since 1.0.0-beta.6
         */
        fun toSan(language: String = "english", pieces: Array<String>? = null): String{
            if(move == null){
                return ""
            }
            return when(language){
                "english" -> toSan(position, move!!)
                "spanish" -> toSan(position, move!!, piecesSpanish)
                "dutch" -> toSan(position, move!!, piecesDutch)
                "french" -> toSan(position, move!!, piecesFrench)
                "german" -> toSan(position, move!!, piecesGerman)
                "italian" -> toSan(position, move!!, piecesItalian)
                else -> {
                    if(pieces == null){
                        throw IllegalArgumentException("unknown language: $language and pieces array is null")
                    } else if (pieces.size != 12){
                        throw IllegalArgumentException("unknown language: $language and invalid pieces array")
                    } else {
                        toSan(position, move!!, pieces)
                    }
                }
            }
        }
    }

    internal inner class RootNode(override val position: Position) : Node {
        override var comment: String? = null
        override val children: MutableList<Node> = mutableListOf()
        override var endLineComment: String? = null
        override var suffixAnnotations: List<Int>? = null
            set(value) {
                throw UnsupportedOperationException("RootNode does not accept suffix annotations.")
            }
        override var parent: Node? = null
            set(value) {
                throw UnsupportedOperationException("RootNode does not accept a parent")
            }
        override val move: Move? = null

        override var initialComment: String? = null
            set(value) {
                logger.warn("initialComment on root nodes it will be always null and can't be set")
                field = null
            }

        override fun hashCode(): Int {
            return genericHashCode(arrayOf(position, comment))
        }

        override fun equals(other: Any?): Boolean {
            return if (other == null) {
                false
            } else if (other === this) {
                true
            } else if (other !is RootNode) {
                false
            } else {
                position == other.position && comment == other.comment && endLineComment == other.endLineComment
            }
        }

        override fun toString(): String {
            return "${comment?.let { "{${it}}" }}${endLineComment?.let { ";${it}" }}"
        }

        override fun appendMove(
            move: String,
            initialComment: String?,
            comment: String?,
            endLineComment: String?,
            suffixAnnotations: List<Int>?,
            notation: Notation
        ): Node {
            if (immutable()) {
                logger.warn("trying to make move $move on immutable game id=$id")
                return this
            }
            if (children.size == 1 && gameMode == GameMode.MATCH) {
                throw GameModeException("Match mode cannot accept variations (RAVs); only the main line is allowed.")
            }
            try {
                val m = when (notation) {
                    Notation.SAN -> sanToMove(position, move)
                    Notation.UCI -> moveOf(move)
                }
                val child = MoveNode(
                    position.move(m),
                    m,
                    initialComment,
                    comment,
                    endLineComment,
                    suffixAnnotations,
                    this
                )
                children.add(child)
                if (children.size == 1) {
                    if (gameMode == GameMode.MATCH) {
                        updateEco()
                    }
                    checkRepetitions(child)
                    checkGameOver(child)
                    checkFiftyMoves(child)
                    checkSeventyFiveMoves(child)
                }
                return child
            } catch (e: MoveException) {
                logger.error(e)
                return this
            }

        }

        override fun belongsToMainLine(): Boolean {
            return true
        }

        override fun copy(parent: Node?): Node {
            return RootNode(position).apply {
                comment = this@RootNode.comment
                endLineComment = this@RootNode.endLineComment
                suffixAnnotations = this@RootNode.suffixAnnotations
                children.addAll(this@RootNode.children.map { it.copy(this) })
            }
        }


    }

    internal inner class MoveNode(
        override val position: Position,
        override val move: Move,
        override var initialComment: String?,
        override var comment: String?,
        override var endLineComment: String?,
        override var suffixAnnotations: List<Int>?,
        override var parent: Node?,

        ) : Node {
        override val children = mutableListOf<Node>()


        override fun hashCode(): Int {
            return genericHashCode(arrayOf(move))
        }

        override fun equals(other: Any?): Boolean {
            return if (other == null) {
                false
            } else if (other === this) {
                true
            } else if (other !is MoveNode) {
                false
            } else {
                move == other.move && position == other.position && comment == other.comment &&
                        suffixAnnotations == other.suffixAnnotations && endLineComment == other.endLineComment
            }
        }

        override fun toString(): String {
            return toString(1)
        }

        private fun toString(tabulation: Int): String {
            val sb = StringBuilder()
            initialComment?.let {
                sb.append("{").append(
                    it
                        .replace("\n", "")
                        .replace(Regex("\\s+"), " ")
                ).append("} ")
            }
            val p = parent ?: throw UnexpectedGameInternalError("node parent can't be null")
            if (p.position.whiteMove) {
                sb.append(p.position.movesCounter).append(".")
            } else if (p.comment != null || p.endLineComment != null || ((p.parent?.children?.size
                    ?: 0) > 1 && p.parent?.children[0] === p) || !mainChild()
            ) {
                sb.append(p.position.movesCounter).append("...")
            }
            sb.append(toSan(p.position, move)).append(" ")
            suffixAnnotations?.let { it.forEach { suffix -> sb.append("$").append(suffix).append(" ") } }
            comment?.let {
                sb.append("{").append(
                    it
                        .replace("\n", "")
                        .replace(Regex("\\s+"), " ")
                ).append("} ")
            }
            endLineComment?.let {
                sb.append(" ; ").append(
                    it
                        .replace("\n", "")
                        .replace(Regex("\\s+"), " ")
                ).append("\n")
            }
            if (p.children.size > 1 && mainChild()) {
                for (i in 0..<p.children.size) {
                    if (p.children[i] !== this) {
                        if (i < 2) {
                            sb.append("\n")
                        }
                        sb.append("\t".repeat(tabulation))
                        sb.append("( ")
                        NodeIterator(p.children[i]).asSequence()
                            .filter { it is MoveNode }
                            .map { it as MoveNode }
                            .forEach { sb.append(it.toString(tabulation + 1)) }
                        sb.append(")\n")
                    }
                }
            }
            return sb.toString()
        }

        override fun appendMove(
            move: String,
            initialComment: String?,
            comment: String?,
            endLineComment: String?,
            suffixAnnotations: List<Int>?,
            notation: Notation
        ): Node {
            if (immutable()) {
                logger.warn("trying to make move $move on immutable game id=$id")
                return this
            }
            if (children.isNotEmpty() && gameMode == GameMode.MATCH) {
                throw GameModeException("Match mode cannot accept variations. Only the main line is allowed.")
            }
            try {
                val m = when (notation) {
                    Notation.SAN -> sanToMove(position, move)
                    Notation.UCI -> moveOf(move)
                }
                val child = MoveNode(
                    position.move(m),
                    m,
                    initialComment,
                    comment,
                    endLineComment,
                    suffixAnnotations,
                    this
                )
                children.add(child)
                if (children.size > 1) {
                    return child
                }
                if (iterator().asSequence().last() === child) {
                    if (gameMode == GameMode.MATCH) {
                        updateEco()
                    }
                    checkRepetitions(child)
                    checkGameOver(child)
                    checkFiftyMoves(child)
                    checkSeventyFiveMoves(child)
                }
                return child
            } catch (e: MoveException) {
                logger.error(e)
                return this
            }
        }

        override fun belongsToMainLine(): Boolean {
            return iterator().asSequence().any { it === this }
        }

        override fun copy(parent: Node?): Node {
            return parent?.let {
                MoveNode(
                    position,
                    move,
                    initialComment,
                    comment,
                    endLineComment,
                    suffixAnnotations,
                    it
                )
            }?.apply {
                children.addAll(this@MoveNode.children.map { it.copy(this) })
            } ?: throw logger.error(UnexpectedGameInternalError("node parent can't be null"))
        }

        private fun mainChild(): Boolean {
            return parent?.children[0] === this
        }

    }

    /**
     * Enum to choose the modality of the instance. In a [MATCH], the idea is that there is no going back or possibility
     * of modification once the game is over. In an [ANALYSIS] game, immutability is not necessary.
     */
    enum class GameMode(internal val value: Boolean) {
        MATCH(true),
        ANALYSIS(false)
    }

    /**
     * Represents the mode in which the three-fold repetition rule is handled.
     */
    enum class ThreeRepetitionsMode {
        /**
         * Ignore repetitions.
         */
        IGNORE,

        /**
         * Consider repetitions as game over condition and automatically set the result to DRAW.
         */
        STRICT,

        /**
         * Consider repetitions but do not automatically set the result tag.
         */
        AWARE
    }

    /**
     * Represents the mode in which the 50-move rule is handled.
     */
    enum class FiftyMovesRuleMode {
        /**
         * Ignore fifty moves rule.
         */
        IGNORE,

        /**
         * Consider fifty moves rule as game over condition and automatically set the result to DRAW.
         */
        STRICT,

        /**
         * Consider fifty moves rule but do not automatically set the result tag.
         */
        AWARE
    }

    /**
     * Enum that represents the result of a game.
     */
    enum class Result(val str: String) {
        WHITE_WIN("1-0"),
        BLACK_WIN("0-1"),
        DRAW("1/2-1/2");
    }

    private class NodeIterator(var curr: Node?) : Iterator<Node> {

        override fun next(): Node {
            val aux = curr
            if (curr?.hasChildren() ?: false) {
                curr = curr?.children[0]
            } else {
                curr = null
            }
            return aux ?: throw NoSuchElementException("next has been called when current node is null")
        }

        override fun hasNext(): Boolean {
            return curr != null
        }
    }

    internal class Repetitions {
        private val positions = mutableMapOf<Position, Int>()

        fun push(node: Node): Int {
            positions[node.position] = positions[node.position]?.let { it + 1 } ?: 1
            return positions[node.position] ?: 1
        }

        fun warning(node: Node): Boolean {
            return positions.entries.asSequence()
                .filter { (_, count) -> count == 2 }
                .any { (position, _) -> position in node.position.children.map { it.v1 } }
        }

        fun remove(node: Node) {
            positions[node.position] = positions[node.position]?.let {
                if (it > 0) {
                    it - 1
                } else {
                    it
                }
            } ?: 0
        }
    }

    /**
     * Thrown when an internal value of a Game instance is inconsistent (indicates a library bug).
     * If it is thrown, please create an ISSUE with the context where the exception occurs.
     */
    class UnexpectedGameInternalError internal constructor(msg: String) : RuntimeException(msg)

    /**
     * This type of exception is thrown if an instance is misused according to the established [GameMode].
     */
    class GameModeException internal constructor(msg: String) : RuntimeException(msg)
}