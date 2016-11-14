package com.chocodev.chocolib.list.provider;

import com.chocodev.chocolib.list.BindableView;

/**
 * Created by drm on 05/08/16.
 */

public interface AdapterViewProvider<T> {
    int getItemCount();
    T getObjectAtPosition(int position);
    Class<? extends BindableView<T>> getViewClassForPosition(int position);
}
