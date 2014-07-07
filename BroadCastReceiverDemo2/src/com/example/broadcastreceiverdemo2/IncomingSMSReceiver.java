package com.example.broadcastreceiverdemo2;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * @author wang
 * ���豸���յ�һ���µ�SMS��Ϣʱ���ͻ�㲥һ��������android.provider.
 * Telephony.SMS_RECEIVED������Intent��
 * SMS�㲥Intent����������SMS��ϸ�ڡ�Ϊ����ȡ��װ��SMS�㲥Intent��Bundle�е�
 * SmsMessage�������飬ʹ��pdus key����ȡSMS pdus���飬���У�ÿ�������ʾһ��SMS��Ϣ��
 * ��ÿ��pdu�ֽ�����ת����SmsMessage���󣬵���SmsMessage.createFromPdu��
 * ����ÿ���ֽ����飬ÿ��SmsMessage�������SMS ��Ϣ��ϸ�ڣ�����Դ��ַ���ֻ��ţ���ʱ�����Ϣ�塣
 * ʵ����onReceive��������������Ķ����Ƿ���@echo�ַ�����ʼ������ǣ�������ͬ���ı����Ǹ��ֻ���
 */
@SuppressLint("DefaultLocale")
public class IncomingSMSReceiver extends BroadcastReceiver {
	private static final String queryString="@echo";
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context _context, Intent _intent) {
		// TODO Auto-generated method stub
		//һ��BroadcastReceiver���Դ������㲥��Ϣ����������Ϊ��onReceive��������
		//����Intent������getAction�жϴ������Ķ��������ɽ��в�ͬ�Ĵ���
		if(_intent.getAction().equals(SMS_RECEIVED)){
			SmsManager sms=SmsManager.getDefault();
			Bundle bundle=_intent.getExtras();
			if(bundle!=null){
				Object[] pdus=(Object[])bundle.get("pdus");
				SmsMessage[] messages=new SmsMessage[pdus.length];
				for(int i=0;i<pdus.length;i++)
				{
					messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
					for(SmsMessage message:messages)
					{
						String msg=message.getMessageBody();
						String to=message.getOriginatingAddress();
						if(msg.toLowerCase().startsWith(queryString))
						{
							String out=new String(msg.substring(queryString.length()));
							
							sms.sendTextMessage(to, null, out, null, null);
						}
					}
				}				
			}
		}
	}	
}
