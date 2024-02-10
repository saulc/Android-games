package com.acme.games.space;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

public class Background implements OnTouchListener, SensorEventListener {

	private static final String TAG = Background.class.getSimpleName();
	
	private GamePanel gameView;
	private Bitmap res;
	private int width, height;
	private long ti;
	private int x, y, speedX, speedY, dx, dy;
	private ArrayList<GraphicsItem> gItems;
	private Gravity gravity;
	//private ButtonItem button;
	private Bitmap buttomBit;
	private MyThread mThread;
	private Paint mPaint, aPaint;
	private Bitmap bullet, b2;
	

	private SensorManager mSensorManager;
	private Sensor mSensor;
	
	private float zStart = -1;
	
	public Background(Resources r, GamePanel sv){


		try {
			gameView = sv;
			gameView.setOnTouchListener(this);
			res = BitmapFactory.decodeStream(r.getAssets().open("bg.jpg"));
			width = res.getWidth();
			height = res.getHeight();
			x = 0;
			y = -height;
			speedX = 0;
			speedY = 0;
			ti = 0;

			bullet = BitmapFactory.decodeStream(r.getAssets().open("bullet.png"));
			b2 = BitmapFactory.decodeStream(r.getAssets().open("bullet2.png"));

			mPaint = new Paint();
			mPaint.setColor(Color.WHITE);
			//mPaint.setStyle(Style.FILL);
			mPaint.setTextSize(40);

			aPaint = new Paint();
			aPaint.setColor(Color.BLACK);


			//gyroscope
			mSensorManager = (SensorManager) gameView.getContext().getSystemService(Context.SENSOR_SERVICE);
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

			log("Sensors ready...");
			//	buttomBit = BitmapFactory.decodeResource(r, R.drawable.ic_launcher);
			//button = new ButtonItem(buttomBit, kind.BUTTON);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void log(String s){
				Log.d(TAG, s);
	}
	public void setGravityEnabled(boolean b){
		if(b && hasGravity())
			return;
		else if(!b && !hasGravity())
			return;
		else if(!b && hasGravity())
			gravity = null;
		else if(b && !hasGravity())
			gravity = new Gravity();
		Log.d(TAG, "Gravity is on: " + b);
	}
	public boolean hasGravity(){
		return gravity != null;
	}
	public void setResource(Bitmap b){
		res = b;
	}
	public void updatePos(long t){
		if(hasGravity())
			gravity.updateGravity(t);
		
		  //Log.d(TAG, "Updateing bg t: " + t);
		y += speedX * (t - ti);
		y += speedY * (t - ti);


		gItems.get(0).left();
		gItems.get(0).right();
		
		for(int i=0; i<gItems.size(); i++)
		if(gItems.get(i).getState() == GraphicsItem.state.FIXEDTOBACKGROUND){
			//Log.d(TAG, "Shifting coin by: " + (speedX ));
			gItems.get(i).setX(  (int) (gItems.get(i).getX() +( speedX * (t - ti))));
		}
		
		ti = t;
	}
	public void setSpeedX(int x){
		speedX = x;
	}
	public void setSpeedY(int y){
		speedY = y;
	}
	public int getSpeedX(){
		return speedX;
	}
	public int getSpeedY(){
		return speedY;
	}
	public int getDistance(){
		return dx - y; //dx - x;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public GraphicsItem removeItem(GraphicsItem g){
		if(gItems != null){
			
			return gItems.remove(gItems.indexOf(g));
		}
		return null;
	}
	public void addItem(GraphicsItem g){
		if(gItems == null)
			gItems = new ArrayList<GraphicsItem>();
		gItems.add(g);
	}
	public ArrayList<GraphicsItem> getItems(){
		return gItems;
	}
	public void drawItems(Canvas canvas){
		if(gItems == null)
			return;
		//Log.d(TAG, "Drawing graphicsItems");
		for(int i=0; i<gItems.size(); i++){
			if(gItems.get(i).getState() == GraphicsItem.state.GRAVITYFIXEDLOCATION){

			  gItems.get(i).setY(gravity.getY() );
			//Log.d(TAG, "Robot at: x:" + gItems.get(i).getDX() + " y: " + gItems.get(i).getDY());
			} else if(gItems.get(i).getState() == GraphicsItem.state.FIXEDTOBACKGROUND){
				gItems.get(i).setX(  gItems.get(i).getX());
				if(gItems.get(i).getKind()== GraphicsItem.kind.BULLET)
					gItems.get(i).setY(  gItems.get(i).getY() + GraphicsItem.BulletSpeed);
			}
			
			gItems.get(i).draw(canvas);
		}
			
		
	}


	private void shiftX(){
		//  Log.d(TAG, "Shifting X bg");
		dx -= x;
		x += width;
	}
	private void shiftY(){
		dy += y;
		y -=height;
	}

	//vertical scroll
	public void draw(Canvas canvas, int s){
		if(canvas == null){
			log("Canvas is null, nothing to draw on...");
			return;
		}
		int yy =  height+y+dy;
		Log.d(TAG, "Drawing bg " + y + " h: " + yy);


		if(height >  y +1000)
			canvas.drawBitmap(res, 0, yy, null);
		else {
			//the end of the bg image.. loop the image
			// or end the level...!!!


			//	  Log.d(TAG, "Drawing bg at:" + -x );
			//if( height < y)
				shiftY();
			//canvas.drawBitmap(res, 0, -(2*height)-y, null);
			//canvas.drawBitmap(res, 0, -height - y, null);
		}

		drawItems(canvas);

		canvas.drawRect(canvas.getWidth() * .6f, 10, canvas.getWidth(), 70, aPaint);
		canvas.drawText("Score: " + s, canvas.getWidth()*.6f, 60, mPaint);
	}

//	//side scrolling
//	public void draw(Canvas canvas, int s){
//		  //Log.d(TAG, "Drawing bg");
//
//
//		  if(canvas.getWidth() < (width + x) )
//			  canvas.drawBitmap(res, x, 0, null);
//		  else {
//		//	  Log.d(TAG, "Drawing bg at:" + -x );
//			  if(-x > width)
//				shiftX();
//			  canvas.drawBitmap(res, x, 0, null);
//			  canvas.drawBitmap(res, width + x, 0, null);
//		  }
//
//		  drawItems(canvas);
//		  canvas.drawText("Score: " + s, canvas.getWidth()*.7f, 60, mPaint);
//	  }

	public void drawOver(Canvas canvas, int s, boolean won){
		//draw(canvas, s);

		Paint p = new Paint();
		p.setColor(Color.WHITE);

		mPaint.setStyle(Paint.Style.FILL);
		p.setTextSize(80);

		canvas.drawText("Game Over", canvas.getWidth()*.25f, 300, p);
		canvas.drawText("" + s,  canvas.getWidth()*.4f, 420, p);
		String m = "----____----";
		if(won)
			m = "----^__^----";
		canvas.drawText(m,  canvas.getWidth()*.24f, 530, p);


	}
	void printSamples(MotionEvent ev) {
		final int historySize = ev.getHistorySize();
		final int pointerCount = ev.getPointerCount();
		for (int h = 0; h < historySize; h++) {
			System.out.printf("At time %d:", ev.getHistoricalEventTime(h));
			for (int p = 0; p < pointerCount; p++) {
				System.out.printf("  pointer %d: (%f,%f)",
						ev.getPointerId(p), ev.getHistoricalX(p, h), ev.getHistoricalY(p, h));
			}
		}
		System.out.printf("At time %d:", ev.getEventTime());
		for (int p = 0; p < pointerCount; p++) {
			System.out.printf("  pointer %d: (%f,%f)",
					ev.getPointerId(p), ev.getX(p), ev.getY(p));
		}
	}
	public void fire(){
		GraphicsItem b = new GraphicsItem(bullet, GraphicsItem.kind.BULLET);
		 b.setX(gItems.get(0).getX()+gItems.get(0).getBoundingRect().width/2);
		 b.setY(gItems.get(0).getY());
		addItem(b);
	}
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		//printSamples(ev);
			log("x: " + ev.getX() + " y: " + ev.getY());
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if(v == gameView){
				if(hasGravity()){

				gravity.jump();
				gItems.get(0).animate();
					if(ev.getX() > v.getWidth()/3*2)
						gItems.get(0).setDir(-1);
					else if(ev.getX() < v.getWidth()/3)
						gItems.get(0).setDir(1);
					else fire();
//					else gItems.get(0).setDir(0);
				//gItems.get(0).setDrawState(1);
				return true;
				}
			}
		}
		if(ev.getAction() == MotionEvent.ACTION_UP){
			if(v == gameView){
				if(hasGravity()){
				gravity.noJump();
					gItems.get(0).center();
				//gItems.get(0).setDrawState(0);
				return true;
				}
			}
		}

		return false;
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		int m = 1;
		if (zStart == -1) {
			 zStart = 0;
			//zStart = event.values[1] * m;

			log("zStart: " + zStart);
		}

		else {
			float z = event.values[1]*m;
			float a = .01f;
//		if(z > zStart + a) gItems.get(0).left();
//		else if(z < zStart - a) gItems.get(0).right();
		
		//log(zStart + "   "  + z);
		
		}
//		String s = "x: " + event.values[0] + " y: " + event.values[1] + " z: " + event.values[2];
//		System.out.println(s);
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		log("Accuracy Changed");
		
	}

}
