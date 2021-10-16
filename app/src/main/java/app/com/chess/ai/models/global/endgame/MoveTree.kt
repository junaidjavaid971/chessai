package app.com.chess.ai.models.global.endgame

import com.github.bhlangonijr.chesslib.move.Move
import java.util.*

class MoveTree<T>(value: T) {
    var value: T = value
    var parent: MoveTree<T>? = null
    var children: MutableList<MoveTree<T>> = mutableListOf()

    fun addChild(move: MoveTree<T>) {
        children.add(move)
    }

    fun updateChild(move: MoveTree<T>, previousMove: MoveTree<T>) {
        children.set(children.indexOf(previousMove), move)
    }

    fun getNumberOfChilds(move: MoveTree<T>): Int {
        return move.children.size
    }

    fun getNode(visit: VisitorNode<T>, move: Move) {
        visit(this)
        val queue: Queue<MoveTree<T>> = ArrayDeque()
        children.forEach { queue.add(it) }

        var node = queue.poll()
        while (node != null) {
            visit(node)
            node.children.forEach { queue.add(it) }
            node = queue.poll()
        }
    }

    fun getParentNode(move: MoveTree<T>, value: T): MoveTree<T>? {
        var nodePosition = 0

        children.forEach() {
            if (it.value == value) {
                nodePosition = children.indexOf(it)
            }
        }

        return children[nodePosition - 1]
    }

    fun getNodePosition(value: T): Int {
        var nodePosition: Int = 0

        children.forEach() {
            if (it.value == value) {
                nodePosition = children.indexOf(it)
            }
        }

        return nodePosition
    }

    fun forEachLevelOrder(visit: VisitorNode<T>) {
        visit(this)
        val queue: Queue<MoveTree<T>> = ArrayDeque()
        children.forEach { queue.add(it) }

        var node = queue.poll()
        while (node != null) {
            visit(node)
            node.children.forEach { queue.add(it) }
            node = queue.poll()
        }
    }


    fun search(moveTree: MoveTree<T>, value: T): MoveTree<T>? {
        var result: MoveTree<T>? = null

        moveTree.children.forEach() {
            if (it.value == value) {
                result = it
            }
        }

        return result
    }

    override fun toString(): String {
        var s = "${value}"
        if (!children.isEmpty()) {
            s += " {" + children.map { it.toString() } + " }"
        }
        return s
    }
}

typealias VisitorNode<T> = (MoveTree<T>) -> Unit