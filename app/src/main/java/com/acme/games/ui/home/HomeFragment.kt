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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

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

        game = BlankFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.homeFrame, game!!)
            .commit()

        return root
    }
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

}