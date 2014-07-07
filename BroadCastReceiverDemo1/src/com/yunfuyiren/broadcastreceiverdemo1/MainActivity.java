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
 * ��Android�У�Broadcast��һ�ֹ㷺���õ���Ӧ�ó���֮�䴫����Ϣ�Ļ��ơ�
 * ��BroadcastReceiver�ǶԷ��ͳ����� Broadcast���й��˽��ܲ���Ӧ��һ�����
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
        //ע��㲥������
        rhelper=new BroadcastReceiverHelper(this);
        rhelper.registerAction("android.provider.Telephony.SMS_RECEIVED");
        super.onResume();
    }
	
	 @Override
	 public void onPause(){
	    //ȡ���㲥������
	    unregisterReceiver(rhelper);
	    super.onPause();
	 }
}
