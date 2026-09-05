package com.acme.games.other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acme.games.BlankFragment
import com.acme.games.R
import com.acme.games.other.GameControl.gdata
import com.acme.games.other.GameListener
import com.acme.games.other.placeholder.PlaceholderContent
import java.util.Date

/**
 * A fragment representing a list of Items.
 */
class memFragment : Fragment() {

    private lateinit var dat : gdata
    private var mstat : GameStats? = null
    private var columnCount = 3
    private var listener : GameListener? = null
    private var memAdapter: MymemRecyclerViewAdapter? = null

    public fun restart(){
        Log.i("Mem frag","restart called")
        val rn = getNumbers(dat.count)
        dat.start(rn)
        memAdapter?.updateValues(rn)
    }
    fun winner(){

        dat.timer.etime = dat.getTime()
        dat.show()
        memAdapter?.notifyDataSetChanged()
        mstat?.logGame(GameResult.HIT, dat.mode, dat.timer, dat.current)
        listener?.win(mstat.toString())
        listener?.updateboard(mstat!!.games)
    }

    fun gameOver(){

        mstat?.logGame(GameResult.MISS, dat.mode, dat.timer, dat.current)
        dat.show()
        memAdapter?.notifyDataSetChanged()
        listener?.gameOver(mstat.toString())

        listener?.updateboard(mstat!!.games)

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
    //initialize a game in mode
    fun startGame(mode: Int, grid: Int){
        columnCount = grid
        dat.setmode(mode, grid)
        val recyclerView = view as? RecyclerView
        (recyclerView?.layoutManager as? GridLayoutManager)?.spanCount = columnCount
    }


    fun hide(){
        dat.hide()
        memAdapter?.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        dat = gdata()
        mstat = GameStats()

    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is GameListener) {
            listener = context
        } else if (parentFragment is GameListener) {
            listener = parentFragment as GameListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val rn = getNumbers(dat.count)
                dat.start(rn)
                memAdapter = MymemRecyclerViewAdapter(rn, dat.vis) { position ->
                    dat.click(position)
                    memAdapter?.notifyDataSetChanged()
                }
                adapter = memAdapter
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            memFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    inner class gdata{
        //contain info for current game. soft reset ready.
        private var tick : Int = 0
        var gameOrder : List<Int>? = null
        var vis = mutableListOf<Boolean>()
        private var pause : Boolean = false
        private var started : Boolean = false
        var firstClick: Boolean = false
        var current : Int = 0
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
            if(mode > 8 && g == 3) mode = 8;

            gridrows = g
            when(g){
                3 -> grid = 9-1
                4 -> grid = 16-1
                5 -> grid = maxgrid
            }
//            if( (mode > 7) and (g == 3)) grid = 11
//            space = (width / gridrows ).toFloat()

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

//                listener.stopUpdate()
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

}