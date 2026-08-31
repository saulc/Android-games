package com.acme.games.other

import java.util.ArrayList

interface GameListener {
    fun win(msg: String)
    fun gameOver(msg: String)
    fun updateboard(games: ArrayList<Gtype>)
}
