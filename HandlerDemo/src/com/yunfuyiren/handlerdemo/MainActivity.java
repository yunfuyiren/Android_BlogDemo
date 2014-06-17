package com.yunfuyiren.handlerdemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author wang
 * Handler案例1
 * 一个应用程序中有2个按钮（start、end），当点击start按钮时，执行一个线程，
 * 这个线程在控制台输出一串字符串，并且每隔3秒再执行一次线程，直到点击end按钮为止，线程停止。 
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
		private Button startButton;
		private Button endButton;
		private TextView text;
		private int i=0;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			//根据id获得控件对象
			text=(TextView)rootView.findViewById(R.id.text);
			startButton=(Button)rootView.findViewById(R.id.start);
			endButton=(Button)rootView.findViewById(R.id.end);
			startButton.setOnClickListener(new StartButtonListener());
			endButton.setOnClickListener(new EndButtonListener());
			return rootView;
		}
		
		class StartButtonListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				 //调用Handler的post()方法，将要执行的线程对象放到队列当中  
				// TODO Auto-generated method stub
				
				text.setText("线程启动");
				handler.post(updateThread);
			}
			
		}
		
		class EndButtonListener implements OnClickListener{

			@Override
			public void onClick(View arg0) {
				//调用Handler的removeCallbacks()方法，删除队列当中未执行的线程对象  
				// TODO Auto-generated method stub
				handler.removeCallbacks(updateThread);
				text.setText("线程终止");
			}
			
		}
		 //创建Handler对象
		Handler handler=new Handler();
		
		//新建一个线程对象  
		Runnable updateThread =new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("updateThread");
				//调用Handler的postDelayed()方法  
	            //这个方法的作用是：将要执行的线程对象放入到队列当中，待时间结束后，运行制定的线程对象  
	            //第一个参数是Runnable类型：将要执行的线程对象  
	            //第二个参数是long类型：延迟的时间，以毫秒为单位  
				i+=1;
				text.setText("线程执行"+i);
				handler.postDelayed(updateThread, 2000);  //线程的嵌套执行
			}
		};
	}

}
