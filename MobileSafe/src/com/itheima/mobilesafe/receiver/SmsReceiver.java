package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

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
			}
			//是的，我想试试能不能从这修改呢！
		}
	}

}
