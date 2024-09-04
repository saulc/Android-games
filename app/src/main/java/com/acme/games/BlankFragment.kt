package com.acme.games



import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.acme.games.other.BlankViewModel
import com.acme.games.other.FragListener
import com.acme.games.other.GameControl
import com.acme.games.other.Gtype


class BlankFragment : Fragment() {

    companion object {
        fun newInstance() = BlankFragment()
    }


    fun log(msg: String){
        Log.d("ArMem: BlankFrag ",  msg)
    }

    private lateinit var viewModel: BlankViewModel

    private var mListener: FragListener? = null
    private var img : ImageView? = null
    private var control: GameControl? = null
    private var bar : SeekBar? = null
    private var bar2 : SeekBar? = null
    private var btext : TextView? = null
    private var text : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.blank_fragment, container, false)
        img = v.findViewById(R.id.bimg)
        bar = v.findViewById(R.id.bBar)
        bar2 = v.findViewById(R.id.bBar2)
        btext = v.findViewById(R.id.btext)
        text = v.findViewById(R.id.text)
        mListener?.gameConnected(this)
        return v
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement FragListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun touched(x:Float, y:Float){
        log("Touch event caught: " + x + " " + y + " control ready?: " + (control!=null))
        control?.touch(x, y)

    }

    public fun startup(msg:String) {
        log("Fragment startup.")
        btext?.text = msg
        // img = bimg //view?.findViewById(R.id.bimg)
        img?.setBackgroundColor(Color.BLACK)
        log("Image view ready: " + (img != null))
        // img?.setImageResource(R.drawable.ic_launcher_foreground)
        img?.setOnTouchListener { v, event ->

            log("w: " + v.width + " h " + v.height)
            if (event.action == MotionEvent.ACTION_UP) {
                val x = event.x
                val y = event.y
                touched(x, y)
            }

            return@setOnTouchListener true

        }

            bar2?.max = 2
        bar2?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                btext?.text = "Grid : $i"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

                log("Seekbar started: ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                log("Seekbar Stopped: ")
            }
        })
        bar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                btext?.text = "Level : $i"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

                log("Seekbar started: ")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                log("Seekbar Stopped: ")
            }
        })

//        val x = img?.width ?: 1080
        val x =   1080
        val w = view?.width ?: x
        val h = view?.height ?: x //1000
        log("Window size: " + w + " x " + h)
        control = GameControl(this, img, btext, w, h)
        restart()
    }

    fun restart(){
        log("game Restarting.")
        val m : Int = bar?.progress ?: 0
        var g : Int = bar2?.progress ?: 0
        g += 3
//        if(g > 7 ) g = 5
//        else if(g < 3) g = 3
//        else g = 4
        control?.startGame(m, g)
        loop  = Runnable {
            mhandler.postDelayed(loop, 100)
            control?.update()
        }


        mhandler.postDelayed(loop, 100)

    }

    fun stopUpdate(){ mhandler.removeCallbacks(loop) }
    fun gameOver(msg: String){
        mhandler.removeCallbacks(loop)
        log("Game OVer Caught.")

        btext?.text = "try again?" + msg
    }

    fun win(msg : String) {
        log("Winner winner, chicken dinner.")
        gameOver("")
        btext?.text = "You got it!" + msg

    }

    fun updateboard(games: ArrayList<Gtype>){
        var t = ""
        for(g in games)
            t += g.toString() + "\n"
        text?.text = t
        log( t)
    }
    fun gsignIn(){
//        val signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        //send score to play service leaderboard
//        Games.getLeaderboardsClient(activity as Activity, GoogleSignIn!!.getLastSignedInAccount(activity))
//            .submitScore(getString(R.string.leaderboard_id), 1337);

    }
    private val RC_LEADERBOARD_UI = 9004

//    private fun showLeaderboard() {
//        Games.getLeaderboardsClient(activity, GoogleSignIn.getLastSignedInAccount(activity)!!)
//            .getLeaderboardIntent(getString(R.string.leaderboard_id))
//            .addOnSuccessListener { intent -> startActivityForResult(intent, RC_LEADERBOARD_UI) }
//    }

    private val mhandler : Handler = Handler()

    private lateinit var loop:Runnable

}
