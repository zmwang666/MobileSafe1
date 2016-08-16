package com.itheima.mobilesafe.service;

import java.util.List;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
/*
 * 获取经纬度坐标的service
 * */
public class LocationService extends Service {
	private LocationManager lm;
	private MyListener listener;
	private SharedPreferences mPref;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);//是否允许付费，比如使用3G网络定位
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        listener = new MyListener();
        lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}
	class MyListener implements LocationListener{
    	
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("get location!");
			mPref.edit().putString("location", "j:" + location.getLongitude() + ";w:" +  location.getLatitude()).commit();
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
    }
    @Override
	public void onDestroy() {
    	super.onDestroy();
    	lm.removeUpdates(listener);//当ACTIVITY 销毁，停止位置更新
    }
	
}
