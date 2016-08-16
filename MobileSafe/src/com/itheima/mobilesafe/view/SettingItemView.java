package com.itheima.mobilesafe.view;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.itheima.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbUpdate;
	private String mUpdateTitle;
	private String mDescOn;
	private String mDescOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mUpdateTitle = attrs.getAttributeValue(NAMESPACE, "update_title");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		initView();
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}
	
	private void initView(){
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbUpdate = (CheckBox) findViewById(R.id.cb_Update);

		setTitle(mUpdateTitle);
		
		
	}
	
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	public void setDesc(String desc){
		tvDesc.setText(desc);
	}
	
	public boolean isChecked(){
		return cbUpdate.isChecked();
	}
	
	public void setChecked(boolean checked){
		cbUpdate.setChecked(checked);
		
		if(checked){
			setDesc(mDescOn);
		}else {
			setDesc(mDescOff);
		}
	}
	
}
