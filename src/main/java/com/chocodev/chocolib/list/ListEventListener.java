package com.chocodev.chocolib.list;

/**
 * Created by DRM on 25/09/13.
 */
public interface ListEventListener<T> {
    public void onListEvent(int actionId,int position,BindableView<T> view);
}
