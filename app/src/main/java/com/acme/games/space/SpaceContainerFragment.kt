package com.acme.games.space

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acme.games.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpaceContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_space_container, container, false)
        
        val fab: FloatingActionButton = root.findViewById(R.id.resetFab)
        fab.setOnClickListener {
            resetHeli()
        }
        
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.spaceFrame, HeliFrag(), "HELI_FRAG")
                .commit()
        }
        
        return root
    }

    private fun resetHeli() {
        childFragmentManager.beginTransaction()
            .replace(R.id.spaceFrame, HeliFrag(), "HELI_FRAG")
            .commit()
    }
}
