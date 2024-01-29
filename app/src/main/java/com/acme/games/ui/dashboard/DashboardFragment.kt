package com.acme.games.ui.dashboard

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
import com.acme.games.databinding.FragmentDashboardBinding
import com.acme.games.space.HeliFrag
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val fab: FloatingActionButton = binding.floatingActionButtonDash
        fab.setOnClickListener {
            fabClicked()
        }

        startGame()


        return root
    }
    private var game : HeliFrag? = null

    fun startGame(){
        game = HeliFrag()
        childFragmentManager.beginTransaction()
            .replace(R.id.dashFrame, game!!)
            .commit()
    }
    fun fabClicked(){
        log("Fab clicked: Restarting game.")
//        game?.restart()
        startGame()

    }
    fun log(msg: String){
        Log.d("ArMem: BlankFrag ",  msg)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}