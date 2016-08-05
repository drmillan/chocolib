package com.chocodev.chocolib.list.provider;

import com.chocodev.chocolib.list.BindableView;

/**
 * Created by drm on 05/08/16.
 */

public interface AdapterViewProvider {
    int getItemCount();
    Object getObjectAtPosition(int position);
    Class<BindableView<?>> getViewClassForPosition(int position);
}
