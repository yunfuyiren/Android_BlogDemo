package com.example.broadcastreceiverdemo2;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver2 extends BroadcastReceiver{
	private Context context; 
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		context=arg0;
		deleteNotification();
	}
	private void deleteNotification() {
		// TODO Auto-generated method stub
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);  
        //É¾³ýÍ¨Öª¡£
		notificationManager.cancel(Receiver1.NOTIFICATION_ID); 
	}

}
