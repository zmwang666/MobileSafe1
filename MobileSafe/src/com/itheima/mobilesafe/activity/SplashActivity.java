package com.itheima.mobilesafe.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;


public class SplashActivity extends Activity {
	
	protected static final int CODE_UPDATE_DIALOG = 0;

	protected static final int CODE_URL_ERROR = 1;

	protected static final int CODE_IO_ERROR = 2;

	protected static final int CODE_JSON_ERROR = 3;

	protected static final int CODE_ENTER_HOME = 4;

	private TextView tvVersion;
	private TextView tvProgress;
	
	private String mVersionName;
	private int mVersionCode;
	private String mDescription;
	private String mUpdateUrl;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdataDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_IO_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			default:
				break;
			}
			
		}
		
	};

	private SharedPreferences spUpte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        RelativeLayout rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText(getVersionName());
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        spUpte = getSharedPreferences("config", MODE_PRIVATE);
        boolean autoUpdate = spUpte.getBoolean("autoUpdate", true);
        if(autoUpdate){
        	checkVersion();
        }else{
        	handler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2*1000);
        }
        
        AlphaAnimation ala = new AlphaAnimation(0.3f, 1);
        ala.setDuration(2*1000);
        rl_splash.startAnimation(ala);
        
        
    }
    /*
     * 获取版本名称
     * */
    
    private String getVersionName(){
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			return "版本名是:" + versionName; 
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    /*
     * 获取版本号
     * */
    private int getVersionCode(){
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			return versionCode; 
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
    }
    /*
     * 从服务器获取检查版本更新
     * */
    private void checkVersion(){
    	final long currentTimeMillis = System.currentTimeMillis();
    	new Thread(){
			@Override
    		public void run() {
    			HttpURLConnection conn = null;
    			Message msg = handler.obtainMessage();
    			try {
    				URL url = new URL("http://10.107.31.84:8080/update.json");
    				conn = (HttpURLConnection)url.openConnection();
    				conn.setRequestMethod("GET");
    				conn.setConnectTimeout(5*1000);
    				conn.setReadTimeout(5*1000);
    				conn.connect();
    				if(conn.getResponseCode() == 200){
    					InputStream is = conn.getInputStream();
    					String result = StreamUtils.readToString(is);
    					System.out.println(result);
    					
    					JSONObject jo = new JSONObject(result);
    					mVersionName = jo.getString("versionName");
    					mVersionCode = jo.getInt("versionCode");
    					mDescription = jo.getString("description");
    					mUpdateUrl = jo.getString("updateUrl");
    					
//    					System.out.println(mVersionName+","+mVersionCode+","+mDescription+","+mUpdateUrl);
    					if(mVersionCode > getVersionCode()){
    						msg.what = CODE_UPDATE_DIALOG;
    					}else{
    						msg.what = CODE_ENTER_HOME;
    					}
    				}
    				
    				
    			} catch (MalformedURLException e) {
    				// TODO Auto-generated catch block
    				msg.what = CODE_URL_ERROR;
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				msg.what = CODE_IO_ERROR;
    				e.printStackTrace();
    			} catch (JSONException e) {
					// TODO Auto-generated catch block
    				msg.what = CODE_JSON_ERROR;
    				e.printStackTrace();
				}finally{
					long currentTimeMillis2 = System.currentTimeMillis();
					long resultTime = currentTimeMillis2 - currentTimeMillis;
					if(resultTime < 2000){
						try {
							sleep(2 * 1000 - resultTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
					if(conn != null){
						//关闭网络连接
						conn.disconnect();
					}
				}
    			
    		}
    	}.start();
    	
    }
    
    private void showUpdataDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("最新版本:" + mVersionName);
    	builder.setMessage(mDescription);
    	builder.setPositiveButton("立即更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("立即更新");
				download();
			}
		});
    	
    	builder.setNegativeButton("以后再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("以后再说");
				enterHome();
			}
		});
    	builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
    	builder.show();
    	
    }
    /*
     * 下载新版本apk
     * */
    protected void download() {
		// TODO Auto-generated method stub
    	tvProgress.setVisibility(View.VISIBLE);
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		String target = Environment.getExternalStorageDirectory() + "/update.apk";
    		HttpUtils httpUtils = new HttpUtils();
    		httpUtils.download(mUpdateUrl,target, new RequestCallBack<File>() {
    			
    			@Override
    			public void onLoading(long total, long current, boolean isUploading) {
    				// TODO Auto-generated method stub
    				super.onLoading(total, current, isUploading);
    				tvProgress.setText("当前下载进度为:" + current * 100/total + "%");
    				System.out.println("当前下载进度为:" + current * 100/total + "%");
    			}
    			@Override
    			public void onSuccess(ResponseInfo<File> arg0) {
    				// TODO Auto-generated method stub
    				System.out.println("下载成功");
//    				System.out.println(Environment.getExternalStorageDirectory() +"");
    				Intent intent = new Intent(Intent.ACTION_VIEW);
    				intent.addCategory(Intent.CATEGORY_DEFAULT);
    				intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
    				//startActivity(intent);
    				startActivityForResult(intent, 1);
    			}
    			
    			@Override
    			public void onFailure(HttpException arg0, String arg1) {
    				// TODO Auto-generated method stub
    				Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
    			}
    		});
    	}
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	enterHome();
    	super.onActivityResult(requestCode, resultCode, data);
    }

	private void enterHome(){
    	Intent intent = new Intent(this, HomeActivity.class);
    	startActivity(intent);
    	finish();
    	
    }
}
