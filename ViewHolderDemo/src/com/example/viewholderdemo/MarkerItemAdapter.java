package com.example.viewholderdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MarkerItemAdapter extends BaseAdapter{
	Context context;
	List<MarkerItem> listData;
	public MarkerItemAdapter(Context context, List<MarkerItem> markerItems)
	{
		this.context=context;
		listData=markerItems;
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
			convertView=mInflater.inflate(R.layout.item_marker_item,null);
			viewHolder.name=(TextView)convertView.findViewById(R.id.name);
			viewHolder.description=(TextView)convertView.findViewById(R.id.description);
			viewHolder.createTime = (TextView) convertView
	                    .findViewById(R.id.createTime);
			convertView.setTag(viewHolder);
		}else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// set item values to the viewHolder:
		MarkerItem markerItem=(MarkerItem) getItem(position);
		if(null!=markerItem)
		{
			viewHolder.name.setText(markerItem.name);
			viewHolder.description.setText(markerItem.description);
			viewHolder.createTime.setText(markerItem.createTime);
		}
		return convertView;
	}
	
	private static class ViewHolder{
		 TextView name;
	     TextView description;
	     TextView createTime;
	}	
}
