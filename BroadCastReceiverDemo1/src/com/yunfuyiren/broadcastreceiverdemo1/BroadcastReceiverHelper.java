package com.yunfuyiren.broadcastreceiverdemo1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author wang
 * ��̬ע���пɽ�BroadcastReceiver�ļ̳�����з�װ��
 * ��ӹ��캯����BroadcastReceiverע��
 */
public class BroadcastReceiverHelper extends BroadcastReceiver{
	// NotificationManager��Notification��������֪ͨ��
    //֪ͨ�����õȲ�����ԱȽϼ򵥣�������ʹ�÷�ʽ�������½�һ��Notification����
	//Ȼ�����ú�֪ͨ�ĸ��������Ȼ��ʹ��ϵͳ��̨���е�NotificationManager����֪ͨ��������
	NotificationManager mn=null;
	Notification notification=null;
	Context ct=null;
	BroadcastReceiverHelper receiver;
	public BroadcastReceiverHelper(Context c)
	{
		ct=c;
		receiver=this;
	}
	
    //ע����˶���
    public void registerAction(String action){
        IntentFilter filter=new IntentFilter();
        filter.addAction(action);
        ct.registerReceiver(receiver, filter);
    }
    
    
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String msg=intent.getStringExtra("msg");
		int id=intent.getIntExtra("who", 0);
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			// getSystemService��Android����Ҫ��һ��API������Activity��һ��������
			//���ݴ����NAME��ȡ�ö�Ӧ��Object��Ȼ��ת������Ӧ�ķ������
	         mn=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
             notification=new Notification(R.drawable.icon, id+"���͹㲥", System.currentTimeMillis());
             Intent it = new Intent(context,MainActivity.class);
             PendingIntent contentIntent=PendingIntent.getActivity(context,
                     0, it, 0);
             notification.setLatestEventInfo(context, 
                     "msg", msg, contentIntent);
             mn.notify(0, notification);
             intent.putExtra("msg", "hi,��ͨ���㲥������Ϣ��");
             context.sendBroadcast(intent);
		}
	}
}
