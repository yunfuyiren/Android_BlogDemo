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
 * 动态注册中可将BroadcastReceiver的继承类进行封装，
 * 添加构造函数和BroadcastReceiver注册
 */
public class BroadcastReceiverHelper extends BroadcastReceiver{
	// NotificationManager和Notification用来设置通知。
    //通知的设置等操作相对比较简单，基本的使用方式就是用新建一个Notification对象，
	//然后设置好通知的各项参数，然后使用系统后台运行的NotificationManager服务将通知发出来。
	NotificationManager mn=null;
	Notification notification=null;
	Context ct=null;
	BroadcastReceiverHelper receiver;
	public BroadcastReceiverHelper(Context c)
	{
		ct=c;
		receiver=this;
	}
	
    //注册过滤动作
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
			// getSystemService是Android很重要的一个API，它是Activity的一个方法，
			//根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。
	         mn=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
             notification=new Notification(R.drawable.icon, id+"发送广播", System.currentTimeMillis());
             Intent it = new Intent(context,MainActivity.class);
             PendingIntent contentIntent=PendingIntent.getActivity(context,
                     0, it, 0);
             notification.setLatestEventInfo(context, 
                     "msg", msg, contentIntent);
             mn.notify(0, notification);
             intent.putExtra("msg", "hi,我通过广播发送消息了");
             context.sendBroadcast(intent);
		}
	}
}
