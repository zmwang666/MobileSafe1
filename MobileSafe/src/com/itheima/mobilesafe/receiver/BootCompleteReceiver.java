package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences mPref = context.getSharedPreferences("config", context.MODE_PRIVATE);
		boolean protect = mPref.getBoolean("protect", false);
		if(protect){
			String sim = mPref.getString("sim", null);
			if(!TextUtils.isEmpty(sim)){
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();
				if(currentSim.equals(sim)){
					System.out.println("手机安全！");
				}else{
					System.out.println("sim卡发生变化，发送报警短信！");
					String phone = mPref.getString("safe_phone", "");
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null, "Sim card changed!", null, null);
				}
			}
		}
	}
}
