package com.chocodev.chocolib.list;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by drm on 13/09/15.
 */
public class CBaseSpinnerAdapter<T,Q extends BindableView<T>,R extends BindableView<T>> implements SpinnerAdapter {
    public static final String TAG = CBaseSpinnerAdapter.class.getName();
    private Class<Q> viewClass;
    private Class<R> dropDownViewClass;
    private List<T> items;
    private Method viewBuilderMethod = null;
    private Method dropDownBuilderMethod = null;


    public CBaseSpinnerAdapter(Class<Q> viewClass,
                               Class<R> dropDownViewClass,
                               List<T> items) {
        this.dropDownViewClass=dropDownViewClass;
        this.viewClass = viewClass;
        this.items = items;
        try {
            viewBuilderMethod = viewClass.getMethod("build", Context.class);
        } catch (Exception ex) {

        }
        try {
            dropDownBuilderMethod = dropDownViewClass.getMethod("build", Context.class);
        } catch (Exception ex) {

        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        BindableView<T> viewGroup = (BindableView<T>) convertView;
        if (viewGroup == null) {

            if (dropDownBuilderMethod == null) {
                // has no build
                try {
                    Constructor constructor = dropDownViewClass.getConstructor(Context.class);
                    viewGroup = (BindableView<T>) constructor.newInstance(parent.getContext());
                } catch (InstantiationException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                try {
                    viewGroup = (BindableView<T>) dropDownBuilderMethod.invoke(null, new Object[]{parent.getContext()});
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    return null;
                }
            }
        }
        viewGroup.bind((T) getItem(position),items.size(),position);
        return viewGroup;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        // Ignore
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        // Ignore
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BindableView<T> viewGroup = (BindableView<T>) convertView;
        if (viewGroup == null) {

            if (viewBuilderMethod == null) {
                // has no build
                try {
                    Constructor constructor = viewClass.getConstructor(Context.class);
                    viewGroup = (BindableView<T>) constructor.newInstance(parent.getContext());
                } catch (InstantiationException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    Log.e(TAG, e.getMessage(), e);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                try {
                    viewGroup = (BindableView<T>) viewBuilderMethod.invoke(null, new Object[]{parent.getContext()});
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    return null;
                }
            }
        }
        viewGroup.bind((T) getItem(position),items.size(),position);
        return viewGroup;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
