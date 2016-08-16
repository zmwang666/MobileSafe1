package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ToastUtils;
/*
 * 第三個向导页
 * */

public class Setup3Activity extends BaseSetupActivity {
	private static final int REQUEST_CONTACTS = 1;
	private EditText etContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		etContact = (EditText) findViewById(R.id.et_contact);
		/*
		 * 每次进入这个界面时先检测是否有保存的安全号码，有就读到输入框中
		 * */
		String phone = mPref.getString("safe_phone", "");
		etContact.setText(phone);
	}
	
	@Override
	protected void showNextPage() {
		/*
		 * 先检测安全号码的正确性
		 * */
		String phone = etContact.getText().toString().trim();//过滤掉首尾的空格
		if(TextUtils.isEmpty(phone)){
			ToastUtils.showShortToast(this, "安全号码不能为空");
			return;
		}
		mPref.edit().putString("safe_phone", phone).commit();//将报警号码保存在SharedPreference中
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}

	@Override
	protected void showPreviousPage() {
		String phone = etContact.getText().toString().trim();//过滤掉首尾的空格
		if(!TextUtils.isEmpty(phone)){
			mPref.edit().putString("safe_phone", phone).commit();//将报警号码保存在SharedPreference中
		}
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
	}
	
	/*
	 * 选择添加联系人
	 * */
	public void chooseContact(View v){
		Intent intent = new Intent(this, ContactsActivity.class);
		startActivityForResult(intent, REQUEST_CONTACTS);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CONTACTS && resultCode == RESULT_OK){
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ", "");
			etContact.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
