package com.chocodev.chocolib.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chocodev.chocolib.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by DRM on 19/09/13.
 */
public class CBaseRecyclerViewAdapter<T, Q extends BindableView<T>> extends RecyclerView.Adapter {

    public static final String TAG = CBaseRecyclerViewAdapter.class.getName();
    private static final int EMPTY_VIEW = 10001;
    private int emptyViewId=-1;

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
    public static class ViewHolder<Q extends BindableView> extends RecyclerView.ViewHolder {
        Q v;
        public ViewHolder(Q v) {
            super(v);
            this.v=v;
        }

        public void bind(Object t, int size, int position) {
            v.bind(t,size,position);
        }
    }

    private Class viewClass;
    private Class objectClass;
    private List<T> items;
    private ListEventListener listEventListener;
    private Method builderMethod = null;

    public CBaseRecyclerViewAdapter(Class<T> objectClass, Class<Q> viewClass, List<T> items,int emptyViewId) {
        this(objectClass,viewClass,items);
        this.emptyViewId=emptyViewId;
    }

    public CBaseRecyclerViewAdapter(Class<T> objectClass, Class<Q> viewClass, List<T> items) {
        this.objectClass = objectClass;
        this.viewClass = viewClass;
        this.items = items;
        try {
            builderMethod = viewClass.getMethod("build", Context.class);
        } catch (Exception ex) {

        }
    }


    @Override
    public int getItemViewType(int position) {
        if(emptyViewId!=-1) {
            if (items.size() == 0) {
                return EMPTY_VIEW;
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(emptyViewId, parent, false);
            EmptyViewHolder evh = new EmptyViewHolder(v);
            return evh;
        }

        ViewHolder vh = new ViewHolder<>(getView(parent));
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {
            ((ViewHolder<Q>) holder).bind(items.get(position), items.size(), position);
        }
    }

    @Override
    public int getItemCount() {
        if(emptyViewId!=-1) {
            return items.size() > 0 ? items.size() : 1;
        }
        return items.size();
    }

    public BindableView<T> getView(ViewGroup parent) {
        BindableView<T> viewGroup=null;
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
        if (listEventListener != null) {
            viewGroup.setListEventListener(listEventListener);
        }
        return viewGroup;
    }

    public void setListEventListener(ListEventListener listEventListener) {
        this.listEventListener = listEventListener;
    }

    public void notifyAction(int actionId, T object, View view) {
        if (listEventListener != null) {
            listEventListener.onListEvent(actionId, object, view);
        }
    }
}
