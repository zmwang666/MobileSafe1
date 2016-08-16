package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ToastUtils;
import com.itheima.mobilesafe.view.SettingItemView;
/*
 * 第二個向导页
 * */

public class Setup2Activity extends BaseSetupActivity {
	private GestureDetector mDetetor;
	private SettingItemView siv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		
		siv = (SettingItemView) findViewById(R.id.siv_sim);
		
		String sim = mPref.getString("sim", null);
		if(!TextUtils.isEmpty(sim)){
			siv.setChecked(true);
		}else{
			siv.setChecked(false);
		}
		siv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv.isChecked()){
					siv.setChecked(false);
				mPref.edit().remove("sim").commit();//删除已绑定的SIM卡
				}else{
					siv.setChecked(true);
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					System.out.println(simSerialNumber);
					mPref.edit().putString("sim", simSerialNumber).commit();//将SIM卡信息保存到本地
				}
			}
		});
	}

	@Override
	protected void showNextPage() {
		String sim = mPref.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			ToastUtils.showShortToast(this, "必须绑定SIM卡!");
			return;
		}
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}

	@Override
	protected void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		
		overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
	}
	
	
	
}
