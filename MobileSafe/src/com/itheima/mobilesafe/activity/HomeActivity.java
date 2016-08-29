package com.itheima.mobilesafe.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[]  home_item_name = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
	private int[] home_item_icon = new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps
			,R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,R.drawable.home_sysoptimize
			,R.drawable.home_tools,R.drawable.home_settings};
	private SharedPreferences spUpte;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		spUpte = getSharedPreferences("config", MODE_PRIVATE);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new MyAdapter());
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					/*
					 * 显示密码框
					 * */
					showPasswordDialog();
					break;
					
				case 7:
					startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
					break;
				case 8:
					System.out.println(position);
					startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}
	/*
	 * 显示密码框
	 * */
	private void showPasswordDialog() {
		String savedPassword = spUpte.getString("password", null);
		if(!TextUtils.isEmpty(savedPassword)){
			//如果已经设置密码，则显示登录密码框
			showPasswordInputDialog();
		}else{
			//若果没有设置密码，则显示设置密码框
			showPasswordSetDialog();
		}
	}

	private void showPasswordInputDialog() {
		AlertDialog.Builder al = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = al.create();
		View view = View.inflate(this, R.layout.password_input_dialog, null);
		alertDialog.setView(view, 0, 0, 0, 0);
		
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				if(!TextUtils.isEmpty(password)){
					String savedPassword = spUpte.getString("password", null);
					if(MD5Utils.encode(password).equals(savedPassword)){
//						Toast.makeText(HomeActivity.this, "登陆成功！", Toast.LENGTH_LONG).show();
						alertDialog.dismiss();
						/*
						 * 跳转到手机防盗页
						 * */
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else{
						Toast.makeText(HomeActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	private void showPasswordSetDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder al = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = al.create();
		View view = View.inflate(this, R.layout.password_set_dialog, null);
		alertDialog.setView(view, 0, 0, 0, 0);
		
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
		
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();
				
				if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)){
					if(password.equals(passwordConfirm)){
						spUpte.edit().putString("password", MD5Utils.encode(password)).commit();
						alertDialog.dismiss();
//						Toast.makeText(HomeActivity.this, "登陆成功！", Toast.LENGTH_LONG).show();
						/*
						 * 跳转到手机防盗页
						 * */
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else{
						Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return home_item_name.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return home_item_name[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view  = View.inflate(HomeActivity.this, R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			ivItem.setImageResource(home_item_icon[position]);
			tvItem.setText(home_item_name[position]);
			
			return view;
		}
		
	}
}
