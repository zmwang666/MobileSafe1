package com.itheima.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void showShortToast(Context cts,String str){
		Toast.makeText(cts, str, Toast.LENGTH_SHORT).show();
	}
	public static void showLongToast(Context cts,String str){
		Toast.makeText(cts, str, Toast.LENGTH_LONG).show();
	}
}
