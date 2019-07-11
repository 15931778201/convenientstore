package com.cs.activity;

import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

public class ShowMsgActivity extends Activity {
	private String msg;

	private TextView text_msg;

	private TextView text_time;

	private int seconds = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_msg);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		text_time = (TextView) findViewById(R.id.text_time);
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				handler.sendEmptyMessage(100);
			}
		},0,1000);
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==100){
				text_time.setText("发布成功"+seconds+"后返回主界面");
				seconds--;
				if(seconds==-1){
					Intent intent = new Intent(ShowMsgActivity.this,MainAdminActivity.class);
					finish();
					startActivity(intent);
					finish();
				}
			}
		}
	};
	
}
