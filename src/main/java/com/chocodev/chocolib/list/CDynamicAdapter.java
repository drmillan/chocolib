package com.chocodev.chocolib.list;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chocodev.chocolib.list.provider.AdapterViewProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DRM on 19/09/13.
 */
public class CDynamicAdapter extends BaseAdapter {

    public static final String TAG = CDynamicAdapter.class.getName();

    private AdapterViewProvider viewProvider;
    private ListEventListener listEventListener;
    private Map<Class,Method> builderMethods = new HashMap<>();

    public CDynamicAdapter(AdapterViewProvider viewProvider) {
        this.viewProvider=viewProvider;
    }


    @Override
    public int getCount() {
        return viewProvider.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return viewProvider.getObjectAtPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BindableView viewGroup = (BindableView) convertView;
        Class<BindableView<?>> viewClass=viewProvider.getViewClassForPosition(position);
        if (viewGroup == null) {

            Method builderMethod=builderMethods.get(viewClass);
            if(builderMethod==null)
            {
                try
                {
                    builderMethod=viewClass.getMethod("build", Context.class);
                }
                catch(Exception ex)
                {
                    // NO build method, no android annotations
                }
            }
            if (builderMethod == null) {
                // has no build
                try {
                    Constructor constructor = viewClass.getConstructor(Context.class);
                    viewGroup = (BindableView<?>) constructor.newInstance(parent.getContext());
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
                    viewGroup = (BindableView<?>) builderMethod.invoke(null, new Object[]{parent.getContext()});
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    return null;
                }
            }
            builderMethods.put(viewClass,builderMethod);
        }
        if (listEventListener != null) {
            viewGroup.setListEventListener(listEventListener);
        }
        viewGroup.bind(getItem(position),viewProvider.getItemCount(),position);
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
