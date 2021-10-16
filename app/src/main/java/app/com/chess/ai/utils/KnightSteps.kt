package app.com.chess.ai.utils

class KnightSteps {
    // Driver Code
    fun getNumberOfSteps(x: Int, y: Int, tx: Int, ty: Int): Int {
        val n = 0
        val ans: Int
        if (x == 1 && y == 1 && tx == 2 && ty == 2
            || x == 2 && y == 2 && tx == 1 && ty == 1
        ) {
            ans = 4
        } else if (x == 1 && y == n && tx == 2 && ty == n - 1
            || x == 2 && y == n - 1 && tx == 1 && ty == n
        ) {
            ans = 4
        } else if (x == n && y == 1 && tx == n - 1 && ty == 2
            || x == n - 1 && y == 2 && tx == n && ty == 1
        ) {
            ans = 4
        } else if (x == n && y == n && tx == n - 1 && ty == n - 1
            || x == n - 1 && y == n - 1 && tx == n && ty == n
        ) {
            ans = 4
        } else {
            dp[1][0] = 3
            dp[0][1] = 3
            dp[1][1] = 2
            dp[2][0] = 2
            dp[0][2] = 2
            dp[2][1] = 1
            dp[1][2] = 1
            ans = getsteps(x, y, tx, ty)
        }
        return ans
    }

    companion object {
        var dp = Array(8) { IntArray(8) }
        fun getsteps(
            x: Int, y: Int,
            tx: Int, ty: Int
        ): Int {
            return if (x == tx && y == ty) {
                dp[0][0]
            } else if (dp[Math.abs(x - tx)][Math.abs(y - ty)] != 0) {
                dp[Math.abs(x - tx)][Math.abs(y - ty)]
            } else {
                val x1: Int
                val y1: Int
                val x2: Int
                val y2: Int
                if (x <= tx) {
                    if (y <= ty) {
                        x1 = x + 2
                        y1 = y + 1
                        x2 = x + 1
                        y2 = y + 2
                    } else {
                        x1 = x + 2
                        y1 = y - 1
                        x2 = x + 1
                        y2 = y - 2
                    }
                } else if (y <= ty) {
                    x1 = x - 2
                    y1 = y + 1
                    x2 = x - 1
                    y2 = y + 2
                } else {
                    x1 = x - 2
                    y1 = y - 1
                    x2 = x - 1
                    y2 = y - 2
                }
                dp[Math.abs(x - tx)][Math.abs(y - ty)] =
                    Math.min(
                        getsteps(x1, y1, tx, ty),
                        getsteps(x2, y2, tx, ty)
                    ) + 1
                dp[Math.abs(y - ty)][Math.abs(x - tx)] =
                    dp[Math.abs(x - tx)][Math.abs(y - ty)]
                dp[Math.abs(x - tx)][Math.abs(y - ty)]
            }
        }
    }
}