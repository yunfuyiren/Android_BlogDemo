package com.yunfuyiren.pxlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.yunfuyiren.pxlistview.XListView.IXListViewListener;

public class MainActivity extends Activity implements IXListViewListener{
	private XListView mListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		geneItems();
		mListView=(XListView)findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);  //使上翻成为可能，mFooterView的出现
		mAdapter=new ArrayAdapter<String>(this,R.layout.list_item,items);
		mListView.setAdapter(mAdapter);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
	}

	private void geneItems() {
		// TODO Auto-generated method stub
		for (int i = 0; i != 20; ++i) {
			items.add("refresh cnt " + (++start));
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

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	
	//下拉处理操作
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				start = ++refreshCnt;
				items.clear();
				geneItems();
				mAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, items);
				mListView.setAdapter(mAdapter);
				onLoad();
			}
			
		}, 2000);
	}
	
	//上滑更新操作
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				geneItems();
				//notifyDataSetChanged()可以在修改适配器绑定的数组后，
				//不用重新刷新Activity，通知Activity更新ListView
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
			
		}, 2000);
	}

}
