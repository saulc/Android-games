package com.acme.games.space;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HeliFrag extends Fragment {
	

	 private static final String TAG = HeliFrag.class.getSimpleName();
	
	  private TextView info;

	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	      Log.d(TAG, "Creating HeliFrag!");
		  return new GamePanel(getActivity());
	    }
	  
	 
		
}
