package com.example.broadcastreceiverdemo2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author wang
 * 实现发送一个通知，Receiver2删除这个通知。
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
		 //设置通知在状态栏显示
		Notification notification=new Notification(R.drawable.icon,
				"来电话啦...", System.currentTimeMillis());
	     //主要是设置点击通知时显示内容的类
		PendingIntent contentIntent = PendingIntent.getActivity(
				context, 0, new Intent(context, MainActivity.class), 
				PendingIntent.FLAG_CANCEL_CURRENT); 	
		//设置通知显示的参数
		notification.setLatestEventInfo(context, "来电话啦...嘿嘿", "赶紧接电话，否则误大事了",
				contentIntent);  
		//NotificationManager --- 状态栏通知的管理类，负责发通知，清除通知等
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(  
                android.content.Context.NOTIFICATION_SERVICE);  
		 //可以理解为执行这个通知
		notificationManager.notify(NOTIFICATION_ID, notification);  
	}

}
