package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	private static final String PATH = "data/data/com.itheima.mobilesafe/files/address.db";
	public static String getAddress(String number){
		String address = "未知号码";
		
		/*
		 * 此处的欲打开数据库，数据库的路径必须存放在data/data/com.itheima.mobilesafe/files/address.db，否则数据库访问不到
		 * */
		SQLiteDatabase addressDB = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = addressDB.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", 
				new String[]{number.substring(0, 7)});
		if(cursor.moveToNext()){
			address = cursor.getString(0);
		}
		
		return address;
		
	}
}
