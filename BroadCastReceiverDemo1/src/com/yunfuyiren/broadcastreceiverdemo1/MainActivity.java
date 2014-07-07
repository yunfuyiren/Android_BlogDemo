package com.yunfuyiren.broadcastreceiverdemo1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
/**
 * @author wang
 * 在Android中，Broadcast是一种广泛运用的在应用程序之间传输信息的机制。
 * 而BroadcastReceiver是对发送出来的 Broadcast进行过滤接受并响应的一类组件
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	BroadcastReceiverHelper  rhelper;
	
	@Override
    public void onResume(){
        //注册广播接收器
        rhelper=new BroadcastReceiverHelper(this);
        rhelper.registerAction("android.provider.Telephony.SMS_RECEIVED");
        super.onResume();
    }
	
	 @Override
	 public void onPause(){
	    //取消广播接收器
	    unregisterReceiver(rhelper);
	    super.onPause();
	 }
}
