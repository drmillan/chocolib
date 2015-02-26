package com.chocodev.chocolib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.chocodev.chocolib.R;
import com.chocodev.chocolib.helper.FontFactory;


/**
 * Created by DRM on 26/07/13.
 */
public class ExLabel extends TextView {
    public ExLabel(Context context) {
        super(context);
    }

    public ExLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
    }

    public ExLabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFromAttributes(context,attrs);
    }

    private void initFromAttributes(Context context,AttributeSet attrs) {
        if(!this.isInEditMode())
        {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExLabel, 0, 0);
            String fontName = ta.getString(R.styleable.ExLabel_fontName);
            if(fontName!=null && !fontName.equals(""))
            {
                Typeface typeface= FontFactory.getInstance().getTypeface(context,fontName);
                if(typeface!=null)
                {
                    setTypeface(typeface);
                }
            }
        }
    }
}
