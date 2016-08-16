package com.itheima.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.itheima.mobilesafe.R;

public class ContactsActivity extends Activity {
	private ListView lvContacts;
	private ArrayList<HashMap<String, String>> readContacts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		lvContacts = (ListView) findViewById(R.id.lv_contacts);
		readContacts = readContacts();
		System.out.println(readContacts);
		lvContacts.setAdapter(new SimpleAdapter(this, readContacts, 
				R.layout.contact_list_item, new String []{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_phone}));
		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = readContacts.get(position).get("phone");// 读取当前item的电话号码
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(Activity.RESULT_OK, intent);// 将数据放在intent中返回给上一个页面

				finish();
			}
		});
	}
	
	public ArrayList<HashMap<String, String>> readContacts() {
		/*
		 * 1.首先从raw_contacts表中读取联系人的contact_id;
		 * */
		Uri rawContactUri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		ArrayList<HashMap<String, String>> contactsList = new ArrayList<HashMap<String,String>>();
		Cursor contactsIdCursor = getContentResolver().query(rawContactUri, new String[] { "contact_id" }, null,
				null, null);
		if(contactsIdCursor != null){
			while(contactsIdCursor.moveToNext()){
				String contactsId = contactsIdCursor.getString(0);
//				System.out.println("contactsId:"+contactsId);
				/*
				 * 2.其次根据读到的contact_id从data表中查询相应的电话号码和联系人名称
				 * */
				Cursor dataCursor = getContentResolver().query(dataUri, new String []{"data1","mimetype"}, "contact_id=?", 
						new String[]{contactsId}, null);
				if(dataCursor != null){
					HashMap<String, String> map = new HashMap<String, String>();
					while (dataCursor.moveToNext()) {
						String data1 = dataCursor.getString(0);
						String mimetype = dataCursor.getString(1);
						
//						System.out.println("contactsId:"+ contactsId);
//						System.out.println("data1:"+ data1);
//						System.out.println("mimetype:"+ mimetype);
//						
						if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
							map.put("phone", data1);
						}else if("vnd.android.cursor.item/name".equals(mimetype)){
							map.put("name", data1);
						}
					}
					contactsList.add(map);
					dataCursor.close();
				}
			}
			contactsIdCursor.close();
			
		}
		return contactsList;
	}
}
