package com.yunfuyiren.handlerdemo2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author wang
 * handler的handleMessage(Message msg)方法，实现线程之间的消息传递。
 * 一个应用程序中有一个进度条和一个按钮，当点击按钮后，每隔一秒钟进度条前进一部分。 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		ProgressBar progressBar;
		Button btn;
		TextView tv;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			progressBar=(ProgressBar)rootView.findViewById(R.id.progress1);
			btn=(Button)rootView.findViewById(R.id.start);
			tv=(TextView)rootView.findViewById(R.id.text);
			btn.setOnClickListener(new OnClickListener(){  //此类相当于继承OnClickListener的子类
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub	
					//设置进度条为可见状态  
					progressBar.setVisibility(View.VISIBLE);
					handler.post(thread);
				}
				
			});
			return rootView;
		}
		
		@SuppressLint("HandlerLeak")
		//使用匿名内部类来复写Handler当中的handlerMessage()方法  
		Handler handler=new Handler(){
			@Override  
	        public void handleMessage(Message msg) {  
				progressBar.setProgress(msg.arg1);
				 //如果msg.arg1的值等于100  
		         if (msg.arg1 >= progressBar.getMax()){  
		             //将线程对象从队列中移除  
		             handler.removeCallbacks(thread);   
		             tv.setText("加载完毕");
		         } else{
		        	 handler.post(thread);  //将要执行的线程放入到队列当中
		         }
			}
		};
		
		//线程类，该类使用匿名内部类的方式进行声明  
		Runnable thread=new Runnable(){
			int pro=0;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.print("Begin Thread");
				pro+=10;
				//得到一个消息对象，Message类是android系统提供的  
				Message msg=handler.obtainMessage();
				//将Message对象的arg1参数的值设置为i
				 msg.arg1=pro;   //用arg1、arg2这两个成员变量传递消息，优点是系统性能消耗较少  
				 try{
					 Thread.sleep(1000); //让当前线程休眠1000毫秒
				 }catch(InterruptedException ex){
					 ex.printStackTrace();
				 }
				 
				//将Message对象加入到消息队列当中  
				 handler.sendMessage(msg); 
				
			}
			
		};
	}

}
