package com.chocodev.chocolib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by drm on 26/04/15.
 */
public class InverseSquareView extends RelativeLayout {
    public InverseSquareView(Context context) {
        super(context);
    }

    public InverseSquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public InverseSquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size=Math.max(widthMeasureSpec,heightMeasureSpec);
        super.onMeasure(size,size);
    }
}
