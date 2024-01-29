package com.acme.games.space;

import android.os.Handler;
import android.os.Message;

import com.acme.games.MainActivity;

import java.lang.ref.WeakReference;

class HandlerExtension extends Handler {
    
	  private final WeakReference<MainActivity> currentActivity;
	  
	  public HandlerExtension(MainActivity activity){
	    currentActivity = new WeakReference<MainActivity>(activity);
	  }
	  
	  @Override
	  public void handleMessage(Message message){
		  MainActivity activity = currentActivity.get();
	    if (activity!= null){
//	    	String s = message.getData().getString("INFO");
//	       activity.setInfo(s);
	     //  if(s == null)
	       activity.gameOver(message.getData().getInt("OVER"));
	    }
	  }
	}


