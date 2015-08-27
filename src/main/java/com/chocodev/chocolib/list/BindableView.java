package com.chocodev.chocolib.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by DRM on 19/09/13.
 */
public abstract class BindableView<T> extends RelativeLayout {
    private ListEventListener<T> listEventListener;

    public BindableView(Context context) {
        super(context);
    }

    public BindableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(T object,int total, int position)
    {

    }

    public void setListEventListener(ListEventListener<T> listEventListener) {
        this.listEventListener = listEventListener;
    }

    public ListEventListener<T> getListEventListener() {
        return this.listEventListener;
    }
    public void send(int actionId,T item)
    {
        if(getListEventListener()!=null)
        {
            getListEventListener().onListEvent(actionId,item,this);
        }
    }
    public void send(int actionId,T item,View view)
    {
        if(getListEventListener()!=null)
        {
            getListEventListener().onListEvent(actionId,item,view);
        }
    }

}
