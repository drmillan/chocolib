package com.chocodev.chocolib.list;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by DRM on 19/09/13.
 */
public class CBaseAdapter<T, Q extends BindableView<T>> extends BaseAdapter {

    public static final String TAG = CBaseAdapter.class.getName();

    private Class viewClass;
    private List<T> items;
    private ListEventListener listEventListener;
    private Method builderMethod = null;

    public CBaseAdapter(Class<Q> viewClass, List<T> items) {
        this.viewClass = viewClass;
        this.items = items;
        try {
            builderMethod = viewClass.getMethod("build", Context.class);
        } catch (Exception ex) {

        }
    }

    /**
     * Change the list objects to items
     *
     * @param items
     */
    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();

    }

    /**
     * Clear all items from the list
     */
    public void clearItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    /**
     * Add new items to the existing ones
     *
     * @param items
     */
    public void addItems(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BindableView<T> viewGroup = (BindableView<T>) convertView;
        if (viewGroup == null) {

            if (builderMethod == null) {
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
                    viewGroup = (BindableView<T>) builderMethod.invoke(null, new Object[]{parent.getContext()});
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    return null;
                }
            }
        }
        if (listEventListener != null) {
            viewGroup.setListEventListener(listEventListener);
        }
        viewGroup.baseBind((T) getItem(position),items.size(),position);
        return viewGroup;
    }

    public void setListEventListener(ListEventListener listEventListener) {
        this.listEventListener = listEventListener;
    }

    public void notifyAction(int actionId, int position, BindableView<?> view) {
        if (listEventListener != null) {
            listEventListener.onListEvent(actionId, position, view);
        }
    }
}
