package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences mPref;
	private TextView safePhone;
	private ImageView ivProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPref.getBoolean("configed", false);
		String safe_phone = mPref.getString("safe_phone", "");
		
		if(configed){
			setContentView(R.layout.activity_lost);
			//根据sp更新安全号码
			safePhone = (TextView) findViewById(R.id.tv_safePhone);
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			safePhone.setText(safe_phone);
			boolean protect = mPref.getBoolean("protect", false);
			//根据sp更新安全号码 
			if(protect){
				ivProtect.setImageResource(R.drawable.lock);
			}else{
				ivProtect.setImageResource(R.drawable.unlock);
			}
		}else{
			/*
			 * 跳转到手机设置向导页
			 * */
			startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
			finish();
		}
		
	}
	
	public void reEnter(View v){
		startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
		finish();
	}
}
