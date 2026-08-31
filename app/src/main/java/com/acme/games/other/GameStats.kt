package com.acme.games.other

import java.util.*
import kotlin.collections.ArrayList

class GameStats {


    var games: ArrayList<Gtype> = ArrayList()
    //not need, can/will sort by type. GameResult.
    var gamesLost : Int = 0
    var gamesWon : Int = 0
    var time : String = ""

    fun logGame(r : GameResult, l: Int, t: GameTime, s: Int = 0){
//        val g = mutableListOf<Gtype>()
        var gg = Gtype(r, l, t.toString(), s)
//        for( a in games)
//            g.add(a)
        games.add(gg)
        if(r == GameResult.HIT) gamesWon++
        else if(r == GameResult.MISS) gamesLost++


        
//        games = g
        time = t.toString()

    }

    override fun toString():String {
        return time
    }

}


enum class GameResult { HIT, MISS, RESET, SKIP}
class Gtype {

    var result : GameResult = GameResult.SKIP
    var level: Int = 0
    var gameTime : String
    var score: Int = 0

    constructor(R: GameResult, L:Int, T: String, S: Int = 0){
        result = R
        level = L
        gameTime = T
        score = S

    }

    override fun toString() : String{
        var w = "Level:" + level
        if(result == GameResult.HIT) w += " won"
        else if(result == GameResult.MISS) w += " lost"
        else if(result == GameResult.SKIP) w += " skip"
        return w + " Score: " + score + " " + gameTime.toString()
    }
}

class GameTime{
    //keep track of play time.

    var stime : Date? = null
    var etime : Date? = null
    var fistclicktime : Date? = null

    override fun toString() : String{

        val e = etime?.time ?: 0
        val f = fistclicktime?.time ?: 0
        val s = stime?.time ?: 0
        val t1 : Long = f - s
        val t2 : Long = e - f
        var msg = " View: " + t1
        if(t2 > 0) msg += " solve: " + t2
        return msg

    }
}