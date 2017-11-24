package com.zxy.wtlauncher.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zxy.wtlauncher.anim.EntryAnimation;

/**
 * Created by Eric on 2017/11/23.
 */

public class MyText extends TextView {

    private Animator animator;
    public MyText(Context context) {
        this(context, null);
    }

    public MyText(Context context, AttributeSet attrs) {
        super(context, attrs);



    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {


        if (gainFocus) {
            performClick();
            animator = EntryAnimation.createFocusAnimator(this);
        } else {

            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
            animator = EntryAnimation.createLoseFocusAnimator(this);

        }
        animator.start();

    }
}
