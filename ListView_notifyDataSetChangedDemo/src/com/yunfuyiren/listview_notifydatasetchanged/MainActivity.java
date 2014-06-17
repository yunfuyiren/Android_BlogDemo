package com.yunfuyiren.listview_notifydatasetchanged;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author wang
 *notifyDataSetChanged()可以在修改适配器绑定的数组后，不用重新刷新Activity，
 *通知Activity更新ListView。今天的例子就是通过Handler AsyncTask两种方式来动态更新ListView.
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
		ListView lv;
		ArrayAdapter<String> Adapter;
		ArrayList<String> arr=new ArrayList<String>();
		public PlaceholderFragment() {
		}
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			lv=(ListView)rootView.findViewById(R.id.lv);
			arr.add("123");
			arr.add("234");
			arr.add("345");
			Adapter=new ArrayAdapter<String>(getActivity(),R.layout.playlist,arr);
			lv.setAdapter(Adapter);
			lv.setOnItemClickListener(lvLis);
			editItem edit=new editItem();
			edit.execute("0","第一项");  //把第一项内容改为"第一项"
			Handler handler=new Handler();// 主要接受子线程发送的数据, 并用此数据配合主线程更新UI.
			handler.postDelayed(add, 2000);//post类方法允许你排列一个Runnable对象到主线程队列中
			return rootView;
		}
		
		Runnable add=new Runnable(){
			//可以分发Message对象和Runnable对象到主线程中, 
			//(1):  安排消息或Runnable 在某个主线程中某个地方执行, 
			//(2)安排一个动作在不同的线程中执行
			@Override
			public void run() {
				// TODO Auto-generated method stub
				arr.add("增加1项");
				 Adapter.notifyDataSetChanged();   
			}
			
		};
		class editItem extends AsyncTask<String,Integer,String>{

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				arr.set(Integer.parseInt(params[0]),params[1]);
				//params得到的是一个数组，params[0]在这里是"0",params[1]是"第1项"
				//Adapter.notifyDataSetChanged();
				//执行添加后不能调用 Adapter.notifyDataSetChanged()更新UI，因为与UI不是同线程
				//下面的onPostExecute方法会在doBackground执行后由UI线程调用
				return null;
			}
			
			@Override
			protected void onPostExecute(String result){
				super.onPostExecute(result);
				Adapter.notifyDataSetChanged();
				//执行完毕，更新UI
			}
		}
		
		private OnItemClickListener lvLis=new OnItemClickListener(){
			 @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				getActivity().setTitle(String.valueOf(arr.get(arg2)));	
			}
		};
	}

}
