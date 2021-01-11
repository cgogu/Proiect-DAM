package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class GameActivity : AppCompatActivity() {
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    var mainBoard = Board()
    lateinit var name: String

    inner class CellClickListener(private val i: Int, private val j: Int) : View.OnClickListener {
        @SuppressLint("SetTextI18n")
        override fun onClick(v: View?) {
            if (!mainBoard.isGameOver) {
                val cell = Cell(i, j)
                mainBoard.placeMove(cell, Board.HUMAN)
                mainBoard.miniMax(0, Board.COMPUTER)
                mainBoard.computersMove?.let {
                    mainBoard.placeMove(it, Board.COMPUTER)
                }
                mapBoardToUI()
            }
            when {
                mainBoard.hasWon(Board.HUMAN) -> text_view_result.text = "Congrats, $name\nYou Won"
                mainBoard.hasWon(Board.COMPUTER) -> text_view_result.text = "Too bad, $name\nComputer Won"
                mainBoard.isGameOver -> text_view_result.text = "50-50, $name\nGame Tied"
            }
        }
    }

    private fun mapBoardToUI() {
        for (i in mainBoard.board.indices) {
            for (j in mainBoard.board.indices) {
                when (mainBoard.board[i][j]) {
                    Board.HUMAN -> {
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    private fun loadBoard() {
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 250
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                layout_board.addView(boardCells[i][j])
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        name = intent.getStringExtra("Name").toString()
        loadBoard()
        button_restart.setOnClickListener {
            mainBoard = Board()
            text_view_result.text = ""
            mapBoardToUI()
        }
        button_back.setOnClickListener {
            val i = Intent(this@GameActivity, IntroActivity::class.java)
            startActivity(i)
        }
    }
}