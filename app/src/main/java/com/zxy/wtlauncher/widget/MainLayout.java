package com.zxy.wtlauncher.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 
* @ClassName: MainLayout 
* @Description:将控件显示在上层.
* @author zhaoyufei
* @date 2016-7-1 下午5:01:04
 */
public class MainLayout extends FrameLayout {

	public MainLayout(Context context) {
		super(context);
		init(context);
	}

	public MainLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	WidgetTvViewBring mWidgetTvViewBring;

	private void init(Context context) {
		this.setChildrenDrawingOrderEnabled(true);
		mWidgetTvViewBring = new WidgetTvViewBring(this);
	}

	@Override
	public void bringChildToFront(View child) {
		mWidgetTvViewBring.bringChildToFront(this, child);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
	}

}
