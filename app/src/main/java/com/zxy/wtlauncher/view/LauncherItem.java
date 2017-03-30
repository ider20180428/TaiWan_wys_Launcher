package com.zxy.wtlauncher.view;


import com.zxy.wtlauncher.Application;
import com.zxy.wtlauncher.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class LauncherItem extends FrameLayout{
	private ImageView icon;
	private TextView lable;
	private Application app;
	public LauncherItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		icon = (ImageView) findViewById(R.id.tool_icon);
		lable = (TextView) findViewById(R.id.tool_label);
	}

	public LauncherItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		icon = (ImageView) findViewById(R.id.tool_icon);
		lable = (TextView) findViewById(R.id.tool_label);
	}

	public LauncherItem(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		icon = (ImageView) findViewById(R.id.tool_icon);
		lable = (TextView) findViewById(R.id.tool_label);
	}
	
	public void showApplication(Application app){
		this.app =app;
		icon.setImageDrawable(app.getIcon());
		lable.setText(app.getLabel());
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}
	
	
}
