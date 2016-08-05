package com.chocodev.chocolib.list;

/**
 * Created by DRM on 25/09/13.
 */
public interface ListEventListener {
    public <T> void onListEvent(int actionId,int position,BindableView<T> view);
}
