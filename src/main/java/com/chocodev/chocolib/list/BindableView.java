package com.chocodev.chocolib.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by DRM on 19/09/13.
 */
public abstract class BindableView<T> extends RelativeLayout {
    private T object;
    private int position;
    private int total;
    private ListEventListener listEventListener;

    public BindableView(Context context) {
        super(context);
    }

    public BindableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    public void baseBind(T object,int total, int position)
    {
        this.object=object;
        this.total=total;
        this.position=position;
        bind(object,total,position);
    }
    public abstract void bind(T object,int total, int position);

    public void setListEventListener(ListEventListener listEventListener) {
        this.listEventListener = listEventListener;
    }

    public ListEventListener getListEventListener() {
        return this.listEventListener;
    }
    public void send(int actionId,T item)
    {
        if(getListEventListener()!=null)
        {
            getListEventListener().onListEvent(actionId,position,this);
        }
    }
}
