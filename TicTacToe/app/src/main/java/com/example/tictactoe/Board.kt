package com.example.tictactoe

class Board {
    companion object {
        const val HUMAN = "O"
        const val COMPUTER = "X"
    }

    val board = Array(3) { arrayOfNulls<String>(3) }

    val availableCells: List<Cell>
        get() {
            val cells = mutableListOf<Cell>()
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j].isNullOrEmpty()) {
                        cells.add(Cell(i, j))
                    }
                }
            }
            return cells
        }

    val isGameOver: Boolean
        get() = hasWon(HUMAN) || hasWon(COMPUTER) || availableCells.isEmpty()

    var computersMove: Cell? = null

    fun placeMove(cell: Cell, player: String) {
        board[cell.i][cell.j] = player
    }

    fun hasWon(player: String): Boolean {
        // 00 01 02
        // 10 11 12
        // 20 21 22

        // diagonals
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == player ||
            board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == player
        ) {
            return true
        }
        // rows and columns
        for (i in board.indices) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == player ||
                board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == player
            ) {
                return true
            }
        }
        return false
    }

    fun miniMax(depth: Int, player: String): Int {
        if (hasWon(COMPUTER)) {
            return +1
        }
        if (hasWon(HUMAN)) {
            return -1
        }
        if (availableCells.isEmpty()) {
            return 0
        }

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (i in availableCells.indices) {
            val cell = availableCells[i]

            if (player == COMPUTER) {
                placeMove(cell, COMPUTER)
                val currentScore = miniMax(depth + 1, HUMAN)
                max = currentScore.coerceAtLeast(max)

                if (currentScore >= 0) {
                    if (depth == 0) {
                        computersMove = cell
                    }
                }

                if (currentScore == 1) {
                    board[cell.i][cell.j] = ""
                    break
                }

                if (i == availableCells.size - 1 && max < 0) {
                    if (depth == 0) {
                        computersMove = cell
                    }
                }
            } else if (player == HUMAN) {
                placeMove(cell, HUMAN)
                val currentScore = miniMax(depth + 1, COMPUTER)
                min = currentScore.coerceAtMost(min)

                if (min == -1) {
                    board[cell.i][cell.j] = ""
                    break
                }
            }
            board[cell.i][cell.j] = ""
        }
        return if (player == COMPUTER) max else min
    }
}