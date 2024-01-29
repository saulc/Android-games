package com.acme.games.other


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.acme.games.BlankFragment

import java.util.*

class GameControl{
    private var img : ImageView? = null
    private var tex : TextView? = null
    private var space : Float = 99f
    private var width: Int = 0
    private var height:Int = 0
    private var bit :Bitmap? = null
    private var dat : gdata
    private var listener : BlankFragment
    private var mstat : GameStats? = null


    fun log(msg :String ){ Log.i("Game Control: " , msg)}

    inner class gdata{
        //contain info for current game. soft reset ready.
        private var tick : Int = 0
        var gameOrder : List<Int>? = null
        var vis = mutableListOf<Boolean>()
        private var pause : Boolean = false
        private var started : Boolean = false
        var firstClick: Boolean = false
        private var current : Int = 0
        var count = 3
        var mode : Int = 2
        var grid : Int = 8
        val maxgrid  = 25 - 1
        var gridrows = 3 //
        var timer = GameTime()




        fun start(order: List<Int>){
            gameOrder = order
            started = true
            current = 0
            firstClick = false

            if(vis.size == 0) for(i in 0.. maxgrid ) vis.add(true)
            else show()

            timer.stime = getTime()
            tick = 0
        }

        /*
        Mode layout: ? ? ?
        0   3x3 = 9
        1   3x4 = 12
        2   4 x 4 = 16
        3   4 x 5 = 20

        4   5 x 7 = 35?
         */
        fun setmode(m: Int, g: Int){
            mode = m
            count = m+1

                gridrows = g
            when(g){
                3 -> grid = 9-1
                4 -> grid = 20-1
                5 -> grid = maxgrid
            }
            if( (mode > 7) and (g == 3)) grid = 11
            space = (width / gridrows ).toFloat()

        }
        fun hide(){

            for(i in 0.. grid ) vis.set(i, false)
        }

        fun show(){

            for(i in 0.. grid ) vis.set(i, true)
        }

        fun toc(): Int{
            if(! pause) tick++
            return tick
        }

        fun vtime(): Int{
            val timed : Boolean =  (mode % 2 == 1)
//            log("Game timer: " + (mode %2 ) )
            if(!timed) return 10000
            var t = 70 - (mode * 10)
            if( t < 6 ) t = 7
            return t
        }

        fun click(zone: Int){
            if(zone > grid ) return       //ignore unused zones this game

            if(! firstClick){

                timer.fistclicktime = getTime()
                firstClick = true
                hide()

                listener.stopUpdate()
            }
                if(current == gameOrder?.get(zone)){
                    vis[zone] = true
                    if(current++ == count) winner()
                    //game completed

                }else {
                    //game over
                    show()
                    gameOver()
                }

        }
        fun getTime() :Date {

//            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val d = Date()
//            val currentDate = sdf.format(d)
            return d

        }

    }       //end of inner class


    fun winner(){

        dat.timer.etime = dat.getTime()
        dat.show()
        mstat?.logGame(GameResult.HIT, dat.mode, dat.timer)
        listener.win(mstat.toString())
    }

    fun gameOver(){

        mstat?.logGame(GameResult.HIT, dat.mode, dat.timer)
        listener.gameOver(mstat.toString())


    }

    fun update( ){
        //not needed for this game?
        val t = dat.toc()
        tex?.text = "T: " + t

        log("T: " + t  + " vt " + dat.vtime())
        if(dat.vtime() == 10000)  listener.stopUpdate() //no need to listen on these levels....
        else if( (t >= dat.vtime()) and (!dat.firstClick ) ) {
            log("View time over. hiding grid.")
            dat.hide()
            drawGrid()
            listener.stopUpdate()
        }

    }


    constructor(Callback: BlankFragment, iv: ImageView?, tv: TextView?,  w: Int, h: Int){
        listener = Callback
        img = iv
        tex = tv
        width = w
        height = h
        dat = gdata()
        mstat = GameStats()


    }


    //initialize a game in mode
    fun startGame(mode: Int, grid: Int){

        space = width/3f

        dat.setmode(mode, grid)
        val rn = getNumbers(dat.count)
        dat.start(rn)


        val b = drawGrid()
        bit = b


    }

    //randomize start values
    fun getNumbers(n: Int): List<Int>{
        //  import kotlin.random.Random
        val order = mutableListOf<Int>()
        for(k in 0 .. dat.grid){
            if(k <= n) order.add(k)
            else order.add(-1)
        }

        order.shuffle()

        return order

    }



    fun checkZones(x: Float, y: Float): Int{
        var z : Int = 0
        val row  : Int = y.toInt() / space.toInt()
        z += x.toInt() / space.toInt()

        if(row > 0) z += row.toInt() * dat.gridrows

        tex?.text = "Zone: " + z + " r: " + row
        return z
    }

    fun touch(x: Float, y: Float){
         if(bit != null) {

             val z = checkZones(x, y)
             dat.click(z)

             val rc = drawGrid()
             val cc = Canvas(rc)
             val p = getPaint()

             if(z%dat.gridrows == 2) p.color = Color.MAGENTA
             else if(z%dat.gridrows == 1) p.color = Color.YELLOW
             else if(z%dat.gridrows == 0) p.color = Color.RED


             cc.drawARGB(0, 0, 0, 0)
             cc.drawCircle(x, y, 22f, p)

             img?.setImageBitmap(rc)
//                bit = rc  //save touches/ display only...



         }
    }

    fun getPaint(): Paint{
        var p = Paint()
        //  p.setColor(Color.TRANSPARENT);
        p.strokeWidth = 3.0f
        p.color = Color.GREEN
        p.textSize = 44f
        return p
    }

    //scaling problems... switching to floating buttons.
    fun drawGrid(): Bitmap{ 

        val rc = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val cc = Canvas(rc)
        val p = getPaint()

        //black background
        cc.drawARGB(0, 0, 0, 0)

        val pos : List<Int> = dat.gameOrder!!.toList()
        val vi : List<Boolean>  = dat.vis
        for(k in 0 .. dat.grid){
            if(vi[k]) {
                val x = k % dat.gridrows
                val y = k / dat.gridrows

                p.color = Color.CYAN
                cc.drawCircle(space * x + space / 2, space * y + space / 2, space / 2, p)

                var msg = ""
                if (pos[k] >= 0) {
                    p.color = Color.GREEN
                    cc.drawCircle(space * x + space / 2, space * y + space / 2, space / 2, p)

                    msg += pos[k]
                    p.color = Color.BLACK
                    var singlespace : Float = space*.1f
                    if( pos[k] > 9 ) singlespace = 0f
                    cc.drawText(msg, space * x + space / 3 + singlespace, space * y + space / 2, p)
                } //(x+y*4)

            }
        }

        img?.setImageBitmap(rc)
        return rc
    }
}