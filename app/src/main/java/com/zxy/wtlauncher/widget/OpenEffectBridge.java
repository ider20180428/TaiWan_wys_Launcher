package com.zxy.wtlauncher.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class OpenEffectBridge extends BaseEffectBridgeWrapper {

	private static final int DEFUALT_TRAN_DUR_ANIM = 300;
	private int mTranDurAnimTime = DEFUALT_TRAN_DUR_ANIM;
	private AnimatorSet mCurrentAnimatorSet;
	private boolean isInDraw = false;
	private boolean mIsHide = false;
	private boolean mAnimEnabled = true;
	private boolean isDrawUpRect = true;
	private View mFocusView;
	private NewAnimatorListener mNewAnimatorListener;

	@Override
	public void onInitBridge(MainUpView view) {
		super.onInitBridge(view);
		view.setVisibility(View.INVISIBLE);
	}

	public void setDrawUpRectEnabled(boolean isDrawUpRect) {
		this.isDrawUpRect = isDrawUpRect;
		getMainUpView().invalidate();
	}

	public void setTranDurAnimTime(int time) {
		mTranDurAnimTime = time;
	}
	
	public int getTranDurAnimTime() {
		return this.mTranDurAnimTime;
	}
	
	public void setAnimEnabled(boolean animEnabled) {
		this.mAnimEnabled = animEnabled;
	}
	
	public boolean isAnimEnabled() {
		return this.mAnimEnabled;
	}
	
	public void setVisibleWidget(boolean isHide) {
		this.mIsHide = isHide;
		getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
	}
	
	public boolean isVisibleWidget() {
		return this.mIsHide;
	}
	
	public interface NewAnimatorListener {
		public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation);
		public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation);
	}

	public void setOnAnimatorListener(NewAnimatorListener newAnimatorListener) {
		mNewAnimatorListener = newAnimatorListener;
	}
	
	public NewAnimatorListener getNewAnimatorListener() {
		return mNewAnimatorListener;
	}
	
	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
		if (!mAnimEnabled)
			return;
		if (oldFocusView != null) {
			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
		}
	}

	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		mFocusView = focusView;
		if (!mAnimEnabled)
			return;
		if (focusView != null) {
			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
			runTranslateAnimation(focusView, scaleX, scaleY);
		}
	}

	@Override
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

		if (mCurrentAnimatorSet != null)
			mCurrentAnimatorSet.cancel();

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
		mAnimatorSet.setDuration(mTranDurAnimTime);
		mAnimatorSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
				if (mIsHide) {
					getMainUpView().setVisibility(View.INVISIBLE);
				}
				if (mNewAnimatorListener != null)
					mNewAnimatorListener.onAnimationStart(OpenEffectBridge.this, focusView, animation);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = true;
				getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
				if (mNewAnimatorListener != null)
					mNewAnimatorListener.onAnimationEnd(OpenEffectBridge.this, focusView, animation);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
			}
		});
		mAnimatorSet.start();
		mCurrentAnimatorSet = mAnimatorSet;
	}

	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
		canvas.save();
		if (!isDrawUpRect) {
			onDrawShadow(canvas);
			onDrawUpRect(canvas);
		}
		if (mFocusView != null && (!isDrawUpRect && isInDraw)) {
			onDrawFocusView(canvas);
		}
		//
		if (isDrawUpRect) {
			onDrawShadow(canvas);
			onDrawUpRect(canvas);
		}
		canvas.restore();
		return true;
	}

	public void onDrawFocusView(Canvas canvas) {
		View view = mFocusView;
		canvas.save();
		float scaleX = (float) (getMainUpView().getWidth()) / (float) view.getWidth();
		float scaleY = (float) (getMainUpView().getHeight()) / (float) view.getHeight();
		canvas.scale(scaleX, scaleY);
		view.draw(canvas);
		canvas.restore();
	}

}
