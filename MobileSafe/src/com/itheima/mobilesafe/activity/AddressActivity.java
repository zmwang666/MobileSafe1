package com.itheima.mobilesafe.activity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private EditText etNumber;
	private TextView tvAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvAddress = (TextView) findViewById(R.id.tv_address);
	}
	
	public void addressQuery(View v){
		String number = etNumber.getText().toString();
		String address = AddressDao.getAddress(number);
		tvAddress.setText(address);
	}
}
