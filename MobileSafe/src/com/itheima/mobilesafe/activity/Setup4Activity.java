package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itheima.mobilesafe.R;
/*
 * 第四個向导页
 * */

public class Setup4Activity extends BaseSetupActivity {
	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cbProtect = (CheckBox) findViewById(R.id.cb_protect);
		boolean protect = mPref.getBoolean("protect", false);
		if(protect){
			cbProtect.setText("防盗保护已经开启");
		}else{
			cbProtect.setText("防盗保护没有开启");
		}
		cbProtect.setChecked(protect);
		
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cbProtect.setText("防盗保护已经开启");
					mPref.edit().putBoolean("protect", true).commit();
				}else{
					cbProtect.setText("防盗保护没有开启");
					mPref.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}
	
	@Override
	protected void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
		mPref.edit().putBoolean("configed", true).commit();//表示已经展示过向导页，以后不用展示		
	}

	@Override
	protected void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
	}
}
