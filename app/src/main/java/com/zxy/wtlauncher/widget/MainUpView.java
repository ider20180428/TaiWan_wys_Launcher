/*
Copyright 2016 The Open Source Project

Author: hailongqiu <356752238@qq.com>
Maintainer: hailongqiu <356752238@qq.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.zxy.wtlauncher.widget;

import com.zxy.wtlauncher.widget.BaseEffectBridge;
import com.zxy.wtlauncher.widget.OpenEffectBridge;
import com.zxy.wtlauncher.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

public class MainUpView extends FrameLayout {

	private static final String TAG = "MainUpView";
	private static final float DEFUALT_SCALE = 1.0f;

	private BaseEffectBridge mEffectBridge;

	public MainUpView(Context context) {
		super(context, null, 0);
		if (context != null && context instanceof Activity) {
			attach2Window((Activity) context);
		}
		init(context, null);
	}

	/**
	 * 鎵嬪姩娣诲姞锛屼笉鍦╔ML娣诲姞鐨勮瘽.
	 */
	private void attach2Window(Activity activity) {
		ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rootView.addView(this, layoutParams);
		rootView.setClipChildren(false);
		rootView.setClipToPadding(false);
	}

	public MainUpView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init(context, attrs);
	}

	public MainUpView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setWillNotDraw(false);
		initEffectBridge();
		// 鍒濆锟�??.
		if (attrs != null) {
			TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.MainUpView);//
			Drawable drawableUpRect = tArray.getDrawable(R.styleable.MainUpView_upImageRes); //
			Drawable drawableShadow = tArray.getDrawable(R.styleable.MainUpView_shadowImageRes); //
			setUpRectDrawable(drawableUpRect);
			setShadowDrawable(drawableShadow);
			tArray.recycle();
		}
	}

	private void initEffectBridge() {
		BaseEffectBridge baseEffectBridge = new OpenEffectBridge();
		baseEffectBridge.onInitBridge(this);
		baseEffectBridge.setMainUpView(this);
		setEffectBridge(baseEffectBridge);
	}

	public void setUpRectResource(int resId) {
		if (mEffectBridge != null)
			mEffectBridge.setUpRectResource(resId);
	}

	public void setUpRectDrawable(Drawable upRectDrawable) {
		if (mEffectBridge != null)
			mEffectBridge.setUpRectDrawable(upRectDrawable);
	}

	public Drawable getUpRectDrawable() {
		if (mEffectBridge != null) {
			return mEffectBridge.getUpRectDrawable();
		}
		return null;
	}

	public void setDrawUpRectPadding(int size) {
		setDrawUpRectPadding(new Rect(size, size, size, size));
	}

	public void setDrawUpRectPadding(Rect rect) {
		if (mEffectBridge != null) {
			mEffectBridge.setDrawUpRectPadding(rect);
			invalidate();
		}
	}

	public Rect getDrawUpRect() {
		if (mEffectBridge != null) {
			return mEffectBridge.getDrawUpRect();
		}
		return null;
	}

	public void setShadowResource(int resId) {
		if (mEffectBridge != null) {
			this.mEffectBridge.setShadowResource(resId);
			invalidate();
		}
	}

	public void setShadowDrawable(Drawable shadowDrawable) {
		if (mEffectBridge != null) {
			this.mEffectBridge.setShadowDrawable(shadowDrawable);
			invalidate();
		}
	}

	public Drawable getShadowDrawable() {
		if (mEffectBridge != null) {
			return this.mEffectBridge.getShadowDrawable();
		}
		return null;
	}

	public void setDrawShadowPadding(int size) {
		setDrawShadowRectPadding(new Rect(size, size, size, size));
	}
	public void setDrawShadowRectPadding(Rect rect) {
		if (mEffectBridge != null) {
			mEffectBridge.setDrawShadowRectPadding(rect);
			invalidate();
		}
	}

	public Rect getDrawShadowRect() {
		if (mEffectBridge != null) {
			return mEffectBridge.getDrawShadowRect();
		}
		return null;
	}

	public void setFocusView(View newView, View oldView, float scale) {
		setFocusView(newView, scale);
		setUnFocusView(oldView);
	}

	public void setFocusView(View view, float scale) {
		setFocusView(view, scale, scale);
	}

	public void setFocusView(View view, float scaleX, float scaleY) {
		if (this.mEffectBridge != null)
			this.mEffectBridge.onFocusView(view, scaleX, scaleY);
	}
	public void setUnFocusView(View view) {
		setUnFocusView(view, DEFUALT_SCALE, DEFUALT_SCALE);
	}

	public void setUnFocusView(View view, float scaleX, float scaleY) {
		if (this.mEffectBridge != null)
			this.mEffectBridge.onOldFocusView(view, scaleX, scaleY);
	}
	public void setEffectBridge(BaseEffectBridge effectBridge) {
		this.mEffectBridge = effectBridge;
		if (this.mEffectBridge != null) {
			this.mEffectBridge.onInitBridge(this);
			this.mEffectBridge.setMainUpView(this);
			invalidate();
		}
	}

	public BaseEffectBridge getEffectBridge() {
		return this.mEffectBridge;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (this.mEffectBridge != null) {
			if (this.mEffectBridge.onDrawMainUpView(canvas)) {
				return;
			}
		}
		super.onDraw(canvas);
	}

}
