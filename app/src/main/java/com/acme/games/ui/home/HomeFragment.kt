package com.acme.games.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.acme.games.BlankFragment
import com.acme.games.R
import com.acme.games.databinding.FragmentHomeBinding
import com.acme.games.other.FragListener
import com.acme.games.other.GameListener
import com.acme.games.other.Gtype
import com.acme.games.other.memFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(), GameListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val fab: FloatingActionButton = binding.floatingActionButton
        fab.setOnClickListener {
            fabClicked()
        }

        game = childFragmentManager.findFragmentById(R.id.homeFrame) as? BlankFragment
        if (game == null) {
            game = BlankFragment.newInstance()
            childFragmentManager.beginTransaction()
                .add(R.id.homeFrame, game!!)
                .commit()
        }

        return root
    }
//    private var game : memFragment? = null
    private var game : BlankFragment? = null
    fun fabClicked(){
        log("Fab clicked: Restarting game.")
        game?.restart()

    }
    fun log(msg: String){
        Log.d("ArMem: BlankFrag ",  msg)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun win(msg: String) {
        log("Winner winner, chicken dinner.")
        binding.textHome.text = "You got it! $msg"
    }

    override fun gameOver(msg: String) {
        log("Game Over Caught.")
        binding.textHome.text = "Try again? $msg"
    }

    override fun updateboard(games: ArrayList<Gtype>) {
        var t = ""
        for (g in games)
            t += g.toString() + "\n"
        log(t)
    }

}