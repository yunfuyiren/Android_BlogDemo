package com.example.broadcastreceiverdemo2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author wang
 * 该demo实现了一个自定义broadcast。
	发送端这个activity中创建了一个按钮，当按钮被按下的时候通过sendBroadcast()发送一个broadcast。
 */
public class MainActivity extends Activity {
	private final String ACTION_SEND = "com.forrest.action.SENDMESSAGE",  
            ACTION_CLEAR = "com.forrest.action.CLEARNOTIFICATION";  
	final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IntentFilter filter=new IntentFilter(SMS_RECEIVED);
		BroadcastReceiver receiver=new IncomingSMSReceiver(); 
		registerReceiver(receiver, filter);
		((Button)findViewById(R.id.btn1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickMenuItem(ACTION_SEND);
			}
			
		});  
		((Button)findViewById(R.id.btn2)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickMenuItem(ACTION_CLEAR); 
			}
			
		});
	}
	//发送广播
	private void clickMenuItem(final String action) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(action);
		sendBroadcast(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
