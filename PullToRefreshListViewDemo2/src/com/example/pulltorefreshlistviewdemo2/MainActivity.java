package com.example.pulltorefreshlistviewdemo2;

import java.util.ArrayList;

import com.example.pulltorefreshlistviewdemo2.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author wang
 * PullToRefreshListView�İ���
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
		private MyAdapter adapter;
		ArrayList<MarkerItem> list; 
		int pageIndex = 1;// ҳ��
		
		ProgressBar blogBody_progressBar;// ����ListView���ؿ�
		ImageButton blog_refresh_btn;// ˢ�°�ť
		ProgressBar blog_progress_bar;// ���ذ�ť
		
		private LinearLayout viewFooter;// footer view
		TextView tvFooterMore;// �ײ�������ʾ
		ProgressBar list_footer_progress;// �ײ�������
		
		Resources res;// ��Դ
		private int lastItem;
		
		public PlaceholderFragment() {
			list=new ArrayList<MarkerItem>();
			
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			res = this.getResources();
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.list_layout, container,
					false);
			InitialControls(rootView);	
			BindControls();
			return rootView;
		}
		/**
		 * ��ʼ���б�
		 */
		private void InitialControls(View rootView) {
			// TODO Auto-generated method stub
			listView=(ListView)rootView.findViewById(R.id.blog_list);
			blogBody_progressBar = (ProgressBar)rootView.findViewById(R.id.blogList_progressBar);
			blogBody_progressBar.setVisibility(View.VISIBLE);
			
			blog_refresh_btn = (ImageButton) rootView.findViewById(R.id.blog_refresh_btn);
			blog_progress_bar = (ProgressBar) rootView.findViewById(R.id.blog_progressBar);
			// �ײ�view
			LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewFooter = (LinearLayout) mInflater.inflate(R.layout.listview_footer,
					null, false);
			
//			adapter=new MyAdapter(getActivity(),list);
//			getData(list);
//			listView.setAdapter(adapter);
		}
		/**
		 * ���¼�
		 */
		private void BindControls() {
			// TODO Auto-generated method stub
			// ˢ��
			blog_refresh_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new PageTask(1,true).execute();
				}
			});
			// ����ˢ��
			((PullToRefreshListView) listView)
				.setOnRefreshListener(new OnRefreshListener(){

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						new PageTask(-1, true).execute();
					}
					
				});
			// ����ˢ��
			listView.setOnScrollListener(new OnScrollListener() {
				/**
				 * ���������һ��
				 */
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					//������״̬�����仯ʱ���������¼�
					if (lastItem == adapter.getCount()
							&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						pageIndex = pageIndex + 1;
						new PageTask(pageIndex, false).execute();
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					//��������������ʱ���������¼�
					// TODO Auto-generated method stub
					lastItem = firstVisibleItem - 2 + visibleItemCount;
				}
			});
//			// �����ת
//			listView.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View v,
//						int position, long id) {
//					RedirectDetailActivity(v);
//				}
//			});
//			// �����¼�
//			listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
//				@Override
//				public void onCreateContextMenu(ContextMenu menu, View v,
//						ContextMenuInfo menuInfo) {
//					MenuInflater inflater = getMenuInflater();
//					inflater.inflate(R.menu.blog_list_contextmenu, menu);
//					menu.setHeaderTitle(R.string.menu_bar_title);
//				}
//			});
		}
		/**
		 * ���߳������������������ء���ʼ�������ؼ��ء�ˢ�£�
		 * 
		 */
		public class PageTask extends AsyncTask<String,Integer,ArrayList<MarkerItem>>{
			boolean isRefresh=false;    //ˢ��ʱΪ1
			int curPageIndex = 0;
			public PageTask(int page, boolean isRefresh) {
				curPageIndex = page;
				this.isRefresh = isRefresh;
			}
			@Override
			protected ArrayList<MarkerItem> doInBackground(String... params) {
				// TODO Auto-generated method stub
				int _pageIndex = curPageIndex;
				if (_pageIndex <= 0) {
					_pageIndex = 1;
				}
				
				ArrayList<MarkerItem> listData=new ArrayList<MarkerItem>();
				getData(listData);
				switch(curPageIndex){
					case -1 :// ����
						ArrayList<MarkerItem> listTmp=new ArrayList<MarkerItem>();
						if(list!=null &&list.size()>0){// ������ҳ������ʱ
							if(listData!=null &&listData.size()>0){ //����ʱ��ȡ����������
								int size=listData.size();
								for(int i=0;i<size;i++){
									if(!list.contains(listData.get(i))){// ��������ظ�,��list�б������listData�е�����ʱ�������롣
										listTmp.add(listData.get(i));
									}
								}
							}
						}
						return listTmp;
					case 0:// �״μ���
					case 1:// ˢ��
						if(listData!=null&&listData.size()>0)
							return listData;
						break;
					default://����
						ArrayList<MarkerItem> listT=new ArrayList<MarkerItem>();
						if(list!=null &&list.size()>0){// ������ҳ������ʱ
							if(listData!=null &&listData.size()>0){ //����ʱ��ȡ����������
								int size=listData.size();
								for(int i=0;i<size;i++){
									if(!list.contains(listData.get(i))){// ��������ظ�,��list�б������listData�е�����ʱ�������롣
										listT.add(listData.get(i));
									}
								}
							}
						}
						return listT;
				}
				return null;
			}
			
			@Override
			protected void onCancelled() {
				super.onCancelled();
			}
			/**
			 * ��������
			 * ����ʹ����doInBackground �õ��Ľ���������UI��
			 * �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			 */
			@Override
			protected void onPostExecute(ArrayList<MarkerItem> result) {
				// ���Ͻ�
				blog_progress_bar.setVisibility(View.GONE);
				blog_refresh_btn.setVisibility(View.VISIBLE);
				
				// ���粻���ò��ұ���û�б�������
				if (result == null || result.size() == 0) {// û��������
					((PullToRefreshListView)listView).onRefreshComplete();
//					if (!NetHelper.networkIsAvailable(getActivity().getApplicationContext())
//							&& curPageIndex > 1) {// ��������û������
//						Toast.makeText(getActivity().getApplicationContext(),
//								R.string.sys_network_error, Toast.LENGTH_SHORT)
//								.show();
//						// listView.removeFooterView(viewFooter);
//					}
					return;
				}
				int size = result.size();
				if(size>=BLOG_PAGE_SIZE&&listView.getFooterViewsCount()==0){
					listView.addFooterView(viewFooter);
				}
				
				if (curPageIndex == -1) {// ����ˢ��
					adapter.InsertData(result);
				}else if(curPageIndex == 0){// �״μ���
					list=result;
					blogBody_progressBar.setVisibility(View.GONE);
					
					adapter=new MyAdapter(getActivity().getApplicationContext(),
							list);
					listView.setAdapter(adapter);
					
					// ���ݲ���
					((PullToRefreshListView) listView).SetDataRow(list.size());
					((PullToRefreshListView) listView).SetPageSize(BLOG_PAGE_SIZE);
				}else if (curPageIndex == 1) {// ˢ��
					try {// ������ҳ��������أ���ˢ�°�ť
						list=result;
						if(adapter!=null&&adapter.GetData()!=null){
							adapter.GetData().clear();
							adapter.AddMoreData(list);
						}else if(result!=null){
							adapter=new MyAdapter(getActivity().getApplicationContext(),list);
							listView.setAdapter(adapter);
						}
						// ���ݲ���
						((PullToRefreshListView) listView).SetDataRow(list.size());
						((PullToRefreshListView) listView).SetPageSize(BLOG_PAGE_SIZE);
					}catch(Exception ex){
						// Log.e("BlogActivity", ex.getMessage());
					}
				}else{//����
					adapter.AddMoreData(result);
				}
				
				if (isRefresh) {// ˢ��ʱ����
					((PullToRefreshListView) listView).onRefreshComplete();
				}
			}
			@Override
			//��execute(Params... params)�����ú�����ִ�У�
			//һ��������ִ�к�̨����ǰ��UI��һЩ��ǡ�
			protected void onPreExecute() {
				// ���������
				if (listView.getCount() == 0) {
					blogBody_progressBar.setVisibility(View.VISIBLE);
				}
				// ���Ͻ�
				blog_progress_bar.setVisibility(View.VISIBLE);
				blog_refresh_btn.setVisibility(View.GONE);

				if (!isRefresh) {// �ײ��ؼ���ˢ��ʱ��������
					TextView tvFooterMore = (TextView) getActivity().findViewById(R.id.tvFooterMore);
					tvFooterMore.setText(R.string.pull_to_refresh_refreshing_label);
					tvFooterMore.setVisibility(View.VISIBLE);
					ProgressBar list_footer_progress = (ProgressBar) getActivity().findViewById(R.id.list_footer_progress);
					list_footer_progress.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
			}
		}
		// ****************************************����Ϊ�˵�����
		
		public static final int BLOG_PAGE_SIZE = 10;// ���ͷ�ҳ����
		private void getData(ArrayList<MarkerItem> listData){
			MarkerItem item;
			for(int i=0;i<10;i++)
			{
				item=new MarkerItem();
				item.name="name "+String.valueOf(i);
				item.description="desc "+String.valueOf(i);
				item.createTime="creatTime "+String.valueOf(i);
				listData.add(item);
			}
		}
	}
}
class MarkerItem{
	String name;
	String description;
	String createTime;
}