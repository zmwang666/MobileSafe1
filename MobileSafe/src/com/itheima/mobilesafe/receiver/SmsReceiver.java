package com.itheima.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

	private DevicePolicyManager mDPM;
	private ComponentName componentName;
	

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = message.getOriginatingAddress();
			String messageBody = message.getMessageBody();
			
			if("#*alarm*#".equals(messageBody)){
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(0.2f, 0.2f);
				player.setLooping(true);
				player.start();
				
				abortBroadcast();
			}else if("#*location*#".equals(messageBody)){
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
				String location = sp.getString("location", "");
				System.out.println(location);
				
				abortBroadcast();
			}else if("#*wipedata*#".equals(messageBody)){
				mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			}else if("#*lockscreen*#".equals(messageBody)){
				mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				componentName = new ComponentName(context, DeviceReceiver.class);
				if(mDPM.isAdminActive(componentName)){
					mDPM.lockNow();
				}else{
					Toast.makeText(context, "请激活设备管理器", Toast.LENGTH_SHORT).show();
					Intent in = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"激活管理器！");
			        context.startActivity(intent);
				}
				
			}
			//是的，我想试试能不能从这修改呢！
		}
	}

}
