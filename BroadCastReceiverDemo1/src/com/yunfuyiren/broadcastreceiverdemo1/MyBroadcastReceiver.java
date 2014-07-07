package com.yunfuyiren.broadcastreceiverdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author wang
 * BroadcastReceiver是对发送出来的 Broadcast进行过滤接受并响应的一类组件。
 * 静态注册的BroadcastReceiver，在AndroidManifest.xml中用标签生命注册，并在标签内用标签设置过滤器
 */
public class MyBroadcastReceiver extends BroadcastReceiver{
	//action名称
	String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(SMS_RECEIVED)){
			Toast.makeText(context, "收到消息", Toast.LENGTH_SHORT).show();
		}
	}		
}