package com.zxy.wtlauncher.widget;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class BaseEffectBridgeWrapper extends BaseEffectBridge {

	private static final float DEFUALT_SCALE = 1.0f;
	private static final int DEFUALT_TRAN_DUR_ANIM = 300;

	private MainUpView mMainUpView;
	private Drawable mDrawableShadow;
	private Drawable mDrawableUpRect;
	private Context mContext;
	private Rect mUpPaddingRect = new Rect(0, 0, 0, 0);
	private Rect mShadowPaddingRect = new Rect(0, 0, 0, 0);
	
	@Override
	public void onInitBridge(MainUpView view) {
		mContext = view.getContext();
	}


	@Override
	public void setShadowResource(int resId) {
		try {
			this.mDrawableShadow = mContext.getResources().getDrawable(resId); // 绉诲姩鐨勮竟锟�??.
		} catch (Exception e) {
			this.mDrawableShadow = null;
			e.printStackTrace();
		}
	}

	@Override
	public void setShadowDrawable(Drawable shadowDrawable) {
		this.mDrawableShadow = shadowDrawable;
	}

	@Override
	public Drawable getShadowDrawable() {
		return this.mDrawableShadow;
	}

	public void setDrawShadowPadding(int size) {
		setDrawShadowRectPadding(new Rect(size, size, size, size));
	}

	@Override
	public void setDrawShadowRectPadding(Rect rect) {
		mShadowPaddingRect.set(rect);
	}

	@Override
	public Rect getDrawShadowRect() {
		return this.mShadowPaddingRect;
	}

	/**
	 */

	@Override
	public void setUpRectResource(int resId) {
		try {
			this.mDrawableUpRect = mContext.getResources().getDrawable(resId); // 绉诲姩鐨勮竟锟�??.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 */
	@Override
	public void setUpRectDrawable(Drawable upRectDrawable) {
		this.mDrawableUpRect = upRectDrawable;
	}

	@Override
	public Drawable getUpRectDrawable() {
		return this.mDrawableUpRect;
	}

	/**
	 *
	 */
	public void setDrawUpRectPadding(int size) {
		setDrawUpRectPadding(new Rect(size, size, size, size));
	}

	/**
	 */
	@Override
	public void setDrawUpRectPadding(Rect rect) {
		mUpPaddingRect.set(rect);
	}

	@Override
	public Rect getDrawUpRect() {
		return this.mUpPaddingRect;
	}

	/**
	 */

	public void setFocusView(View newView, View oldView, float scale) {
		setFocusView(newView, scale);
		setUnFocusView(oldView);
	}

	/**
	 */
	public void setFocusView(View view, float scale) {
		setFocusView(view, scale, scale);
	}

	public void setFocusView(View view, float scaleX, float scaleY) {
		onFocusView(view, scaleX, scaleY);
	}

	/**
	 */
	public void setUnFocusView(View view) {
		setUnFocusView(view, DEFUALT_SCALE, DEFUALT_SCALE);
	}

	public void setUnFocusView(View view, float scaleX, float scaleY) {
		onOldFocusView(view, scaleX, scaleY);
	}

	/**
	 */
	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
		if (oldFocusView != null) {
			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFUALT_TRAN_DUR_ANIM).start();
		}
	}

	/**
	 */
	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		if (focusView != null) {
			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFUALT_TRAN_DUR_ANIM).start();
			runTranslateAnimation(focusView, scaleX, scaleY);
		}
	}

	/**
	 */

	/**
	 */
	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
		canvas.save();
		// 缁樺埗闃村奖.
		onDrawShadow(canvas);
		// 缁樺埗锟�??涓婂眰鐨勮竟锟�??.
		onDrawUpRect(canvas);
		canvas.restore();
		return true;
	}

	/**
	 */
	public void onDrawShadow(Canvas canvas) {
		Drawable drawableShadow = getShadowDrawable();
		if (drawableShadow != null) {
			Rect shadowPaddingRect = getDrawShadowRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			drawableShadow.getPadding(padding);
			drawableShadow.setBounds(-padding.left - (shadowPaddingRect.left), -padding.top - (shadowPaddingRect.top),
					width + padding.right + (shadowPaddingRect.right),
					height + padding.bottom + (shadowPaddingRect.bottom));
			drawableShadow.draw(canvas);
		}
	}

	/**
	 */
	public void onDrawUpRect(Canvas canvas) {
		Drawable drawableUp = getUpRectDrawable();
		if (drawableUp != null) {
			Rect paddingRect = getDrawUpRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			// 杈规鐨勭粯锟�??.
			drawableUp.getPadding(padding);
			drawableUp.setBounds(-padding.left - (paddingRect.left), -padding.top - (paddingRect.top),
					width + padding.right + (paddingRect.right), height + padding.bottom + (paddingRect.bottom));
			drawableUp.draw(canvas);
		}
	}

	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		try {
			Rect fromRect = findLocationWithView(getMainUpView());
			Rect toRect = findLocationWithView(toView);
			float x = toRect.left - fromRect.left;
			float y = toRect.top - fromRect.top;
			flyWhiteBorder(toView, x, y, scaleX, scaleY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Rect findLocationWithView(View view) {
		ViewGroup root = (ViewGroup) getMainUpView().getParent();
		Rect rect = new Rect();
		root.offsetDescendantRectToMyCoords(view, rect);
		return rect;
	}

	public void flyWhiteBorder(final View focusView, float x, float y, float scaleX, float scaleY) {
		int newWidth = 0;
		int newHeight = 0;
		int oldWidth = 0;
		int oldHeight = 0;
		if (focusView != null) {
			newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
			newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
			x = x + (focusView.getMeasuredWidth() - newWidth) / 2;
			y = y + (focusView.getMeasuredHeight() - newHeight) / 2;
		}

		oldWidth = getMainUpView().getMeasuredWidth();
		oldHeight = getMainUpView().getMeasuredHeight();

		ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMainUpView(), "translationX", x);
		ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMainUpView(), "translationY", y);
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "width", oldWidth,
				(int) newWidth);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "height", oldHeight,
				(int) newHeight);
		//
		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(DEFUALT_TRAN_DUR_ANIM);
		mAnimatorSet.start();
	}

	/**
	 */
	public class ScaleView {
		private View view;
		private int width;
		private int height;

		public ScaleView(View view) {
			this.view = view;
		}

		public int getWidth() {
			return view.getLayoutParams().width;
		}

		public void setWidth(int width) {
			this.width = width;
			view.getLayoutParams().width = width;
			view.requestLayout();
		}

		public int getHeight() {
			return view.getLayoutParams().height;
		}

		public void setHeight(int height) {
			this.height = height;
			view.getLayoutParams().height = height;
			view.requestLayout();
		}
	}

	/**
	 */
	@Override
	public void setMainUpView(MainUpView view) {
		this.mMainUpView = view;
	}

	/**
	 */
	@Override
	public MainUpView getMainUpView() {
		return this.mMainUpView;
	}

}
