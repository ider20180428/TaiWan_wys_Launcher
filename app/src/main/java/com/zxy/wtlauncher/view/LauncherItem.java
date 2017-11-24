package com.zxy.wtlauncher.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;

public class LauncherItem extends FrameLayout{
	private ImageView icon,icon2;
	private TextView lable;
	private String tag;
	private Application app;

	public LauncherItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		icon = (ImageView) findViewById(R.id.tool_icon);
		icon2 = (ImageView) findViewById(R.id.tool_icon2);
		lable = (TextView) findViewById(R.id.tool_label);
		this.tag = (String) getTag();
	}

	public LauncherItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		icon = (ImageView) findViewById(R.id.tool_icon);
		icon2 = (ImageView) findViewById(R.id.tool_icon2);
		lable = (TextView) findViewById(R.id.tool_label);
		this.tag = (String) getTag();
	}

	public LauncherItem(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		icon = (ImageView) findViewById(R.id.tool_icon);
		icon2 = (ImageView) findViewById(R.id.tool_icon2);
		lable = (TextView) findViewById(R.id.tool_label);
		this.tag = (String) getTag();
	}


	public void showApplication(Application app){
		this.app =app;
		icon.setImageDrawable(app.getIcon());
		lable.setText(app.getLabel());
		if (tag.equals("11")||tag.equals("12")){
			icon2.setVisibility(VISIBLE);
			icon.setVisibility(GONE);
			lable.setVisibility(GONE);
		}else {
			icon2.setVisibility(GONE);
			icon.setVisibility(VISIBLE);
			lable.setVisibility(VISIBLE);
		}

	}

	public void setIcon(int id){
		if (tag.equals("11")||tag.equals("12")){
			icon2.setImageResource(id);
		}else {
			icon.setImageResource(id);
		}
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}
	
	
}
