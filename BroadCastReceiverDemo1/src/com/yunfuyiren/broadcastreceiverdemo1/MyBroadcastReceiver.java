package com.yunfuyiren.broadcastreceiverdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author wang
 * BroadcastReceiver�ǶԷ��ͳ����� Broadcast���й��˽��ܲ���Ӧ��һ�������
 * ��̬ע���BroadcastReceiver����AndroidManifest.xml���ñ�ǩ����ע�ᣬ���ڱ�ǩ���ñ�ǩ���ù�����
 */
public class MyBroadcastReceiver extends BroadcastReceiver{
	//action����
	String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(SMS_RECEIVED)){
			Toast.makeText(context, "�յ���Ϣ", Toast.LENGTH_SHORT).show();
		}
	}		
}