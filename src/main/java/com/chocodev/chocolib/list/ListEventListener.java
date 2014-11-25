package com.chocodev.chocolib.list;

import android.view.View;

/**
 * Created by DRM on 25/09/13.
 */
public interface ListEventListener<T> {
    public void onListEvent(int actionId, T item, View view);
}
