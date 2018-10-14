package com.sourcedev.kryptoquiz.util;

import android.support.v4.view.ViewPager;
import android.view.View;

class VerticalPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -1 *position);

        // [-Infinity,-1)
        // This page is way off-screen to the left.
        if(position < -1)
            view.setAlpha(0);

            // top page
        else if(position <= 0) {
            view.setTranslationY(view.getHeight() * position);
        }
        // for upcoming (adjacent) bottom page

        else if(position<=1f){
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1f - 0.25f*Math.abs(position));
            view.setAlpha(1 - position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
        // (1,+Infinity]
        // This page is way off-screen to the right.
        else
            view.setAlpha(0);
    }
}