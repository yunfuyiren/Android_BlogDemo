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
 * 当设备接收到一条新的SMS消息时，就会广播一个包含了android.provider.
 * Telephony.SMS_RECEIVED动作的Intent。
 * SMS广播Intent包含了新来SMS的细节。为了提取包装在SMS广播Intent的Bundle中的
 * SmsMessage对象数组，使用pdus key来提取SMS pdus数组，其中，每个对象表示一个SMS消息。
 * 将每个pdu字节数组转化成SmsMessage对象，调用SmsMessage.createFromPdu，
 * 传入每个字节数组，每个SmsMessage对象包含SMS 消息的细节，包括源地址（手机号），时间和消息体。
 * 实现了onReceive函数来检查新来的短信是否以@echo字符串开始，如果是，发送相同的文本给那个手机：
 */
@SuppressLint("DefaultLocale")
public class IncomingSMSReceiver extends BroadcastReceiver {
	private static final String queryString="@echo";
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context _context, Intent _intent) {
		// TODO Auto-generated method stub
		//一个BroadcastReceiver可以处理多个广播消息，具体做法为在onReceive（）方法
		//调用Intent参数的getAction判断传进来的动作，即可进行不同的处理。
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
