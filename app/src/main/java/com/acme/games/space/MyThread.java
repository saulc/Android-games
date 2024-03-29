package com.acme.games.space;

import static androidx.core.content.res.ResourcesCompat.FontCallback.getHandler;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.acme.games.MainActivity;
import com.acme.games.R;

import java.io.IOException;
import java.util.ArrayList;




public class MyThread extends Thread {

	public interface MyThreadInterface {
		public void gameOver(int score);
	}


	private static final String TAG = MyThread.class.getSimpleName();

	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	private boolean running;
	private Gravity gravity;
	private Bitmap heli_boom, coin;
	private Bitmap[] bomb, heli;
	private int bgWidth = 600, bgHeight;
	private final int robotAt = 200, speedX = -5;
	private Background bg;
	private GraphicsItem robotItem;
	private Locations coins, bombs;
	private Resources res;
	private MainActivity mListener;
	private Handler mHandler;
	private int mScore;
	private final int coinVal = 100;
	private boolean won = false;
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	public MyThread(SurfaceHolder surfaceHolder, GamePanel gamePanel, Resources res) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		this.res = res;
		mListener = (MainActivity) gamePanel.getContext();
//		mHandler = mListener?.getHandler();
		gravity = new Gravity();
		heli = new Bitmap[1];
//		heli[0] = BitmapFactory.decodeResource(res, R.drawable.heli1);
//		heli[1] = BitmapFactory.decodeResource(res, R.drawable.heli2);
//		heli[2] = BitmapFactory.decodeResource(res, R.drawable.heli3);
//		heli[0] = BitmapFactory.decodeResource(res, R.assets.user);
		//heli[2] = BitmapFactory.decodeResource(res, R.drawable.heligo3);


//		coin = BitmapFactory.decodeResource(res, R.assets.pink);
		try {

			heli[0] = BitmapFactory.decodeStream(res.getAssets().open("user.png"));
			coin = BitmapFactory.decodeStream(res.getAssets().open("supra.png"));


		bomb = new Bitmap[2];
		bomb[0] = BitmapFactory.decodeStream(res.getAssets().open("bullet.png"));
		bomb[1] = BitmapFactory.decodeStream(res.getAssets().open("bullet2.png"));
//		bomb[0] = BitmapFactory.decodeResource(res, R.drawable.blue);
//		bomb[1] = BitmapFactory.decodeResource(res, R.drawable.green);
//		bomb[2] = BitmapFactory.decodeResource(res, R.drawable.yellow);
//		bomb[3] = BitmapFactory.decodeResource(res, R.drawable.red);
//		bomb[4] = BitmapFactory.decodeResource(res, R.drawable.orange);
//		bomb[5] = BitmapFactory.decodeResource(res, R.drawable.purple);
//		bomb[6] = BitmapFactory.decodeResource(res, R.drawable.pink);
		} catch (IOException e) {
//			throw new RuntimeException(e);

		}
		mScore = 0;
	}

	private void init(){
		//loadCoinsAndBombs(coins, bombs);
		Log.d(TAG, "Init game");
		coins = loadCoins();
		bg = new Background(res, gamePanel);
		robotItem = new GraphicsItem(heli[0], GraphicsItem.kind.ROBOT);
		
		robotItem.setLocation(robotAt, 50);
		robotItem.setState(GraphicsItem.state.GRAVITYFIXEDLOCATION);
		
		robotItem.setAnimation(heli, 80);
		bg.addItem(robotItem);
		bg.setGravityEnabled(true);
		bg.setSpeedX(0);
//		bg.setListener(this);
//		bg.ShowButton();
		bgHeight = gamePanel.getHeight();
		bg.setGravityEnabled(true);
		bg.setSpeedX(0);
        bg.setSpeedY(speedX);
	}
	
	private void setMessage(String s){
		 Bundle msgBundle = new Bundle();
		    msgBundle.putString("INFO", s);
		    Message msg = new Message();
		    msg.setData(msgBundle);
//		    mHandler.sendMessage(msg);

	}
	
	private void over(){
		 Bundle msgBundle = new Bundle();
		    msgBundle.putInt("OVER", mScore);
		    Message msg = new Message();
		    msg.setData(msgBundle);
//		    mHandler.sendMessage(msg);
	}
	
	
	private void countDown(){
		try {
			String s = "Get Ready.";
			setMessage(s);
			Log.d(TAG, s);
			sleep(1000);
			s = "Set..";
			setMessage(s);
			Log.d(TAG, s);
			sleep(1000);
			s = "Go!!!";
			setMessage(s);
			Log.d(TAG, s);
			sleep(500);
			bg.setGravityEnabled(true);
			bg.setSpeedX(speedX);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}
	
	@Override
	public void run() {

		init();
		//countDown();
		
		long tickCount = 0L;
		Log.d(TAG, "Starting game loop");
		while (running) {
			//tickCount++;
			//Log.d(TAG, "Game loop executing " + tickCount + " times");
		
                Canvas c = null;

                 try {
                       c = surfaceHolder.lockCanvas(null);
                       synchronized (surfaceHolder) {

                    	  
                    	   bg.updatePos(tickCount++);
                    	   
                    	   bg.draw(c, ++mScore);

						   if(c == null) break;
						   addCoins();
						   checkColliding();


						   Log.d(TAG, "tick :" + tickCount);
						   if(!running){
							   Log.d(TAG, "Showing game overscreen");
							   //user lost
							   bg.drawOver(c, mScore, won);
							   //sleep(3000);
							   //Log.d(TAG, "closing game overscreen");
						   }
                  		 
                    	   sleep(6);
                     }
                 } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

                     if (c != null) {
                    	 surfaceHolder.unlockCanvasAndPost(c);
                    	 
                     }
                 }
//                 if(tickCount < 2)
//          			 countDown();
			
		}
		Log.d(TAG, "Game loop executed " + tickCount + " times");
		restartGame();
		
	}
	
	
	
	public void restartGame() {
		//setMessage("Score: " + mScore);
		over();
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//((MyThreadInterface) mListener).gameOver(mScore);
		mListener= null;
		this.surfaceHolder = null;
		this.gamePanel = null;
		this.res = null;
		mHandler =null;
		Log.d(TAG, "no really, you're done.");
	}
	private void scoreCoin(){
		mScore += coinVal;
		//setMessage("Score: " + mScore); 
	}

	public boolean isOutOfBounds() {
		Rect me = robotItem.getBoundingRect();
		//Log.d("Robot", "x = " + me.getX() + " y = " + me.getY() + " height:" + me.getHeight());
		if(me.getY() + me.getHeight() > bgHeight || me.getY() + .5* me.getHeight() < -bgHeight)
			return true;
			
		return false;
	}

	private void checkColliding(){
		
		if(isOutOfBounds()){
			robotItem.animate();
			lose();
			Log.d(TAG, "Lost!");
			return;
		}
		
		 ArrayList<GraphicsItem> items = bg.getItems();
  	   if(items != null)
  	   for(int i=0; i<items.size(); i++){
  		   if(robotItem.isColliding(items.get(i))){
  			   if(items.get(i).getKind() == GraphicsItem.kind.COIN){
  				   bg.removeItem(items.get(i));
  		   			scoreCoin();
  		   			Log.d(TAG, "Got a coin!");
  			   }
  			   else if(items.get(i).getKind() == GraphicsItem.kind.BOMB){
  				   	
 		   			Log.d(TAG, "Hit a Bomb!!");
				   	items.get(i).animate();
				    robotItem.animate();
 		   				lose();
 			   }
  		   }
  	   }
	}
	private void lose() {
		won = false;
		running = false;
			
	}

	private void win(){
		won = true;
		running = false;
	}
	
	private boolean rand(){
		int a = 12 - mScore/1000;
		if(a < 2) a = 2;
		//return ((Math.random() * 10) % a) == 0;
		return (mScore % a) == 0;
	}

	private void addCoins(){
		if(addingCoin)
			return;

		if(coins.getCurrent() >= coins.size()-1)
			win();

		Log.d(TAG, "I'm at "+  bg.getDistance() + " next coin is at " + coins.peek());
		final int tollerance = 2;
			if(bg.getDistance() - bgHeight >= coins.peek() - tollerance
					&& bg.getDistance() - bgHeight <= coins.peek() + tollerance){
				addingCoin = true;
				coins.next();
				//Log.d(TAG, "Random = " + (((Math.random()*1000) % 2)));
				if( rand())
					addBomb(coins.getY(), bg.getX()+ coins.getX() - 1000);
				else
				addCoin(coins.getY(), bg.getX()+ coins.getX() - 1000);
				
			}
			addingCoin = false;
	}
	private boolean addingCoin = false;
	private void addCoin(int x, int y){
		
		Log.d(TAG, "Adding Coin at x= " + x + " y= " + y);
		//bg.bgPos();
	
	   GraphicsItem coinItem = new GraphicsItem(coin, GraphicsItem.kind.COIN);
	   coinItem.setState(GraphicsItem.state.FIXEDTOBACKGROUND);
	   coinItem.setY(-y);
	   coinItem.setX(x);
	   bg.addItem(coinItem);
	}
	
	private void addBomb(int x, int y){
		
		Log.d(TAG, "Adding Bomb at x= " + x + " y= " + y);
		//bg.bgPos();
	
	   GraphicsItem b = new GraphicsItem(bomb[3], GraphicsItem.kind.BOMB);
	   b.setState(GraphicsItem.state.FIXEDTOBACKGROUND);
	   b.setY(y);
	   b.setX(x);
	   b.setAnimation(bomb, 60);
	   b.animate();
	   bg.addItem(b);
	 
	}
	

	private Locations loadCoins(){
		int[] cdat = res.getIntArray(R.array.coin_locations);
		/*<!-- coin location:
    	starting location x
    	satrting location y
    	number of coins
    	xSpacing
    	ySpacing
    	Y space from yi
    	 --> */
		if(cdat.length != 6){
			Log.e(TAG, "Invalid level.xml");
			return null;
		}
		int x,y,num, xSpace, ySpace, dy;
		x = cdat[0];
		y = cdat[1];
		num = cdat[2];
		xSpace = cdat[3];
		ySpace = cdat[4];
		dy = cdat[5];
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(x, y));	//first point
		boolean up = false;
		for(int i=1; i<num; i++){
			int yTemp ;
			if(i%11==3 || Math.random()*100%3 ==1){
				up = !up;
				Log.d(TAG, "Coin switch!");
			}
			if(up)
				yTemp = (y -= ySpace);
			else yTemp = (y += ySpace);

			if(yTemp > dy){
				up = !up;
				yTemp = (y -= 2*ySpace);
			}
			else if(yTemp < 0){
				up = !up;
				yTemp = (y += 2*ySpace);
			}
			points.add(new Point(yTemp, x + i*xSpace));

			//Log.d(TAG, "point at: x = " + points.get(i).getX() + " y = " + points.get(i).getY());
		}
		Log.d(TAG, "Coins loaded.");
		return new Locations(points);
	}

	


}

class Locations {
	private ArrayList<Point> loc;
	private int current;
	public Locations(ArrayList<Point> items){
		loc = items;
		current = 0;
	}
	public int getCurrent(){
		return current;
	}
	public int size(){
		return loc.size();
	}
	public int getX(){
		return loc.get(current).getX();
	}
	public int getY(){
		return loc.get(current).getY();
	}
	public int peek(){
		return loc.get(current).getX();
	}
	
	public void next(){
		Log.d("Locations", "locatins moving to next item");
		current++;
	}
}

