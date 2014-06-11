package com.example.viewholderdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * @author wang
 * ViewHolder实现Listview每个Item的快速加载
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
		ListView listView;
		MarkerItemAdapter  adapter;
		ArrayList<MarkerItem> markerItems;
		public PlaceholderFragment() {
			listView=null;
			markerItems=new ArrayList<MarkerItem>();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			listView=(ListView)rootView.findViewById(R.id.list);
			adapter=new MarkerItemAdapter(getActivity(), markerItems);
			getData();
			listView.setAdapter(adapter);
			return rootView;
		}

		private void getData() {
			// TODO Auto-generated method stub
				MarkerItem people;  
		        for(int i=1;i<10;i++){  
		            people = new MarkerItem();  
		            people.name="张三";  
		            people.description = "男";  
		            people.createTime ="22";  
		            markerItems.add(people);
		     }  
		}
	}
}

class MarkerItem{
	String name;
	String description;
	String createTime;
}