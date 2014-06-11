package com.example.pulltorefreshlistviewdemo2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<MarkerItem> listData;
	public MyAdapter(Context context)
	{
		this.context=context;
	}
	public MyAdapter(Context context,ArrayList<MarkerItem> list)
	{
		this.context=context;
		listData=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		MarkerItem item=null;
		if(listData!=null)
			item=listData.get(arg0);
		return item;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView==null)
		{
			viewHolder=new ViewHolder();
			LayoutInflater mInflater=LayoutInflater.from(context);
			convertView=mInflater.inflate(R.layout.item_marker_item, null);
			viewHolder.name=(TextView)convertView.findViewById(R.id.name);
			viewHolder.description=(TextView)convertView.findViewById(R.id.description);
			viewHolder.createTime = (TextView) convertView
	                    .findViewById(R.id.createTime);
			convertView.setTag(viewHolder);
		}else
			viewHolder=(ViewHolder)convertView.getTag(); //获得已有的视图
		
		// set item values to the viewHolder:
		MarkerItem markerItem=(MarkerItem)getItem(position);
		if(null!=markerItem)
		{
			viewHolder.name.setText(markerItem.name);
			viewHolder.description.setText(markerItem.description);
			viewHolder.createTime.setText(markerItem.createTime);
		}
		return convertView;
	}

	/**
	 * 插入
	 * 向MyAdapter类自带的数据赋值
	 * @param list
	 */
	public void InsertData(List<MarkerItem> list) {
		this.listData.addAll(0, list);
		this.notifyDataSetChanged();
		//notifyDataSetChanged()可以在修改适配器绑定的数组后，
		//不用重新刷新Activity，通知Activity更新ListView。
	}
	/**
	 * 得到数据
	 * 
	 * @return
	 */
	public List<MarkerItem> GetData() {
		return this.listData;
	}
	/**
	 * 增加数据
	 * 
	 * @param list
	 */
	public void AddMoreData(List<MarkerItem> list) {
		this.listData.addAll(list);
		this.notifyDataSetChanged();
	}
	private static class ViewHolder{
		TextView name;
	    TextView description;
	    TextView createTime;
	}
}
