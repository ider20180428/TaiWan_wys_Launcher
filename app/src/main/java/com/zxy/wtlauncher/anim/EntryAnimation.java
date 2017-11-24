package com.zxy.wtlauncher.anim;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * Created by ider-eric on 2016/12/29.
 */

public class EntryAnimation {

    public static ObjectAnimator createFocusAnimator(View view) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY);
        animator.setDuration(200);
        //view.bringToFront();

        return animator;
    }

    public static ObjectAnimator createLoseFocusAnimator(View view) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.3f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.3f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY);
        animator.setDuration(100);
        return animator;
    }

}
