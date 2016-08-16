package com.itheima.mobilesafe.activity;

import com.itheima.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDetetor;
	public SharedPreferences mPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config",MODE_PRIVATE);
		mDetetor = new GestureDetector(this, new SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				if(Math.abs(e2.getRawY() - e1.getRawY()) > 200){
					Toast.makeText(BaseSetupActivity.this, "不能这样划哦！", Toast.LENGTH_SHORT).show();
					return true;
				}
				
				if(Math.abs(velocityX) < 1000){
					Toast.makeText(BaseSetupActivity.this, "滑动太慢哦！", Toast.LENGTH_SHORT).show();
					return true;
				}
				
				/*
				 * 手势识别上一页
				 * */
				if(e2.getRawX() - e1.getRawX() > 200){
					showPreviousPage();
					return true;
				}
				/*
				 * 手势识别下一页
				 * */
				if(e1.getRawX() - e2.getRawX() > 200){
					showNextPage();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}
	
	protected abstract void showNextPage();


	protected abstract void showPreviousPage();

	/*
	 * 鼠标点击下一页
	 * */
	public void next(View v){
		showNextPage();
	}
	/*
	 * 鼠标点击上一页
	 * */
	public void previous(View v){
		showPreviousPage();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetetor.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
