package com.zxy.wtlauncher.adapter;

import java.util.List;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TvGridAdapter extends TvBaseAdapter {

	
	private List<Application> appList;
	private LayoutInflater inflater;
	
	public TvGridAdapter(Context context,List<Application> appList){
		this.inflater=LayoutInflater.from(context);
		this.appList=appList;
	}
	
	@Override
	public int getCount() {
		return appList.size();
	}

	@Override
	public Object getItem(int position) {
		return appList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View contentView, ViewGroup parent) {
		ViewHolder holder=null;
		if (contentView==null) {
			contentView=inflater.inflate(R.layout.item_grid, null);
			holder=new ViewHolder();
			holder.tv_title=(TextView) contentView.findViewById(R.id.tv_title);
			holder.tiv_icon=(ImageView) contentView.findViewById(R.id.tiv_icon);
			contentView.setTag(holder);
		}else{
			holder=(ViewHolder) contentView.getTag();
		}
		
		Application app=appList.get(position);
		holder.tv_title.setText(app.getLabel());
		holder.tiv_icon.setImageDrawable(app.getIcon());
		
		return contentView;
	}
	
	public void addItem(Application item) {
		appList.add(item);
	}

	public void clear() {
		appList.clear();
	}

	public void flush(List<Application> appListNew) {
		appList = appListNew;
	}

	
	static class ViewHolder{
		TextView tv_title;
		ImageView tiv_icon;
	}
}
