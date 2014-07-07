package com.example.broadcastreceiverdemo2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author wang
 * ʵ�ַ���һ��֪ͨ��Receiver2ɾ�����֪ͨ��
 */
public class Receiver1 extends BroadcastReceiver {
	private Context context;
	public static final int NOTIFICATION_ID=10001;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		this.context=arg0;
		showNotification();
	}

	@SuppressWarnings("deprecation")
	private void showNotification() {
		// TODO Auto-generated method stub
		 //����֪ͨ��״̬����ʾ
		Notification notification=new Notification(R.drawable.icon,
				"���绰��...", System.currentTimeMillis());
	     //��Ҫ�����õ��֪ͨʱ��ʾ���ݵ���
		PendingIntent contentIntent = PendingIntent.getActivity(
				context, 0, new Intent(context, MainActivity.class), 
				PendingIntent.FLAG_CANCEL_CURRENT); 	
		//����֪ͨ��ʾ�Ĳ���
		notification.setLatestEventInfo(context, "���绰��...�ٺ�", "�Ͻ��ӵ绰�������������",
				contentIntent);  
		//NotificationManager --- ״̬��֪ͨ�Ĺ����࣬����֪ͨ�����֪ͨ��
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(  
                android.content.Context.NOTIFICATION_SERVICE);  
		 //�������Ϊִ�����֪ͨ
		notificationManager.notify(NOTIFICATION_ID, notification);  
	}

}
