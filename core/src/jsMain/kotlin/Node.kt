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

import io.github.lunalobos.chess4kt.Game

/**
 * Each game can be interpreted as a series of interconnected nodes in a tree. Each node is move in the game and
 * holds information about the board state after that move. Additionally, each node can have regular or end-of-line
 * comments, suffix annotations, as well as more than one child (from the second child onwards, they are
 * variations), and always only one parent node.
 *
 * This is a facade created to enable exporting the code to JS, though it can also be used directly within the JS modules of any KMP project.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class Node internal constructor(internal val backedNode: io.github.lunalobos.chess4kt.Game.Node) {
    /**
     * The position of the node, which is the result after executing the move.
     */
    val position get() = Position(backedNode.position)

    /**
     * The move of the node. It is null when it is the root node, which only has the starting position of the
     * game.
     */
    val move get() = backedNode.move?.let { Move(it) }

    /**
     * The children of the node. The first child corresponds to the main line. The rest are variations (RAVs).
     */
    val children get() = backedNode.children.map { Node(it) }

    /**
     * The comment that precedes the move number in PGN (e.g., "{Comment} 1. e4").
     */
    val initialComment get() = backedNode.initialComment

    /**
     * The regular comment that follows the move and any suffix annotations (e.g., 1. e4 {Comment}).
     */
    val comment get() = backedNode.comment

    /**
     * The end-of-line comment for the node, which follows a semicolon ; and goes until the end of the line.
     */
    val endLineComment get() = backedNode.endLineComment

    /**
     * The list of suffix annotations (NAGs) for the node.
     */
    val suffixAnnotations get() = backedNode.suffixAnnotations

    /**
     * The parent node. It can only be null when it is the root node, which evidently cannot have a parent.
     */
    val parent get() = backedNode.parent?.let { Node(it) }

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
    ) = Node(
        backedNode.appendMove(
            move,
            initialComment,
            comment,
            endLineComment,
            suffixAnnotations,
            io.github.lunalobos.chess4kt.Notation.valueOf(notation.name)
        )
    )

    /**
     * Promotes the child at the given index to the primary variation (children[0]).
     */
    fun promoteChild(index: Int) = backedNode.promoteChild(index)

    /**
     * Promotes this node to the main line.
     */
    fun promoteNode() = backedNode.promoteNode()

    /**
     * Removes the specified child node (variation) from the current node's list of children.
     */
    fun removeChild(node: Node) = backedNode.removeChild(node.backedNode)

    /**
     * Checks if the node has children.
     */
    fun hasChildren() = backedNode.hasChildren()

    /**
     * Indicates whether this node belongs to the main line (i.e., it is the first child of all its ancestors).
     */
    fun belongsToMainLine() = backedNode.belongsToMainLine()

    /**
     * Creates a deep copy of this node and its entire subtree, assigning the specified parent to the new copy.
     * This process is recursive; copying the root node copies the entire game tree.
     */
    fun copy(parent: Node?) = Node(backedNode.copy(parent?.backedNode))

    override fun hashCode() = backedNode.hashCode()

    override fun equals(other: Any?) = if (other is Node) {
        backedNode == other.backedNode
    } else {
        false
    }

    override fun toString() = backedNode.toString()
}