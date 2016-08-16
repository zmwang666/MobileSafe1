package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.view.SettingItemView;

public class SettingsActivity extends Activity {
	private SettingItemView siv;
	private SharedPreferences spUpte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		siv = (SettingItemView) findViewById(R.id.siv);
		//siv.setTitle("自动更新设置");
		spUpte = getSharedPreferences("config", MODE_PRIVATE);
		boolean autoUpdate = spUpte.getBoolean("autoUpdate", true);
		if(autoUpdate){
			//siv.setDesc("自动更新已开启");
			siv.setChecked(true);
		}else{
			//siv.setDesc("自动更新已关闭");
			siv.setChecked(false);
		}
		
		
		
		siv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(siv.isChecked()){
					siv.setChecked(false);
					//siv.setDesc("自动更新已关闭");
					spUpte.edit().putBoolean("autoUpdate", false).commit();
					
				}else {
					siv.setChecked(true);
					//siv.setDesc("自动更新已开启");
					spUpte.edit().putBoolean("autoUpdate", true).commit();
				}
			}
		});
	}
}
