package app.com.chess.ai.utils

import android.util.Log
import java.util.*

object KnightShort {
    // Utility method returns true if (x, y) lies
    // inside Board
    fun isInside(x: Int, y: Int, N: Int): Boolean {
        return if (x >= 1 && x <= N && y >= 1 && y <= N) true else false
    }

    // Method returns minimum step
    // to reach target position
    fun minStepToReachTarget(
        knightPos: IntArray, targetPos: IntArray,
        N: Int
    ): Int {
        // x and y direction, where a knight can move
        val dx = intArrayOf(-2, -1, 1, 2, -2, -1, 1, 2)
        val dy = intArrayOf(-1, -2, -2, -1, 1, 2, 2, 1)

        // queue for storing states of knight in board
        val q = Vector<cell>()


        // push starting position of knight with 0 distance
        q.add(cell(knightPos[0], knightPos[1], 0))
        var t: cell
        var x: Int
        var y: Int
        val visit =
            Array(N + 1) { BooleanArray(N + 1) }

        // make all cell unvisited
        for (i in 1..N) for (j in 1..N) visit[i][j] = false

        // visit starting state
                visit[knightPos[0]][knightPos[1]] = true

        // loop untill we have one element in queue
        while (!q.isEmpty()) {
            t = q.firstElement()
            q.removeAt(0)
            // if current cell is equal to target cell,
            // return its distance
            if (t.x == targetPos[0] && t.y == targetPos[1])
            {
                return t.dis
            }

            // loop for all reachable states
            for (i in 0..7) {
                x = t.x + dx[i]
                y = t.y + dy[i]

                // If reachable state is not yet visited and
                // inside board, push that state into queue
                if (isInside(x, y, N) && !visit[x][y]) {
                    visit[x][y] = true
                    q.add(cell(x, y, t.dis + 1))
                }
            }
        }
        return Int.MAX_VALUE
    }

    // Class for storing a cell's data
    internal class cell(var x: Int, var y: Int, var dis: Int)
}