package com.acme.games

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.acme.games.databinding.ActivityMainBinding
import com.acme.games.other.FragListener
import com.acme.games.space.HeliFrag
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), FragListener {

    private lateinit var binding: ActivityMainBinding

    private var myFrag: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Games)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onClick(info: String) {
        log("Click detected: " + info)
    }

    override fun gameConnected(g : BlankFragment ) {
        log("game connected!")
        g?.startup("ABC")
    }
    fun log(msg: String){
        Log.d("ArMem: BlankFrag ",  msg)
    }
    private fun startGame() {
        log("Starting Game, adding helifragment")
        try {
            myFrag = HeliFrag()
            supportFragmentManager.beginTransaction()
                .add(R.id.homeFrame, myFrag!!).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun endGame() {
        log("Ending game!, removing helifragment")
        try {
            supportFragmentManager.beginTransaction()
                .remove(myFrag!!).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun gameOver(score: Int) {
        log("GameOVer! $score")
        endGame()
        log("Restarting!")
        startGame()
    }
}