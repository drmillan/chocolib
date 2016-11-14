package com.chocodev.chocolib.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chocodev.chocolib.list.provider.AdapterViewProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DRM on 19/09/13.
 */
public class CDynamicRecyclerViewAdapter<T, Q extends BindableView<T>> extends RecyclerView.Adapter {

    public static final String TAG = CDynamicRecyclerViewAdapter.class.getName();
    private static final int EMPTY_VIEW = 10001;
    private int emptyViewId=-1;
    private Map<Integer,Class<BindableView>> viewClassesByType=new HashMap<>();

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
            v.baseBind(t,size,position);
        }
    }

    private AdapterViewProvider viewProvider;
    private ListEventListener listEventListener;
    private Map<Class,Method> builderMethods = new HashMap<>();

    public CDynamicRecyclerViewAdapter(AdapterViewProvider viewProvider,int emptyViewId) {
        this.emptyViewId=emptyViewId;
        this.viewProvider=viewProvider;
    }

    public <T> CDynamicRecyclerViewAdapter(final Class<BindableView<T>> viewClass,final List<T> objects, int emptyViewId)
    {
        this(viewClass,objects);
        this.emptyViewId=emptyViewId;
    }
    public <T> CDynamicRecyclerViewAdapter(final Class<BindableView<T>> viewClass,final List<T> objects)
    {
        viewProvider=new AdapterViewProvider<T>() {
            @Override
            public int getItemCount() {
                return objects.size();
            }

            @Override
            public T getObjectAtPosition(int position) {
                return objects.get(position);
            }

            @Override
            public Class<? extends BindableView<T>> getViewClassForPosition(int position) {
                return viewClass;
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        if(emptyViewId!=-1) {
            if (viewProvider.getItemCount() == 0) {
                return EMPTY_VIEW;
            }
        }
        Class viewClass=viewProvider.getViewClassForPosition(position);
        viewClassesByType.put(viewClass.hashCode(),viewClass);
        return viewClass.hashCode();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(emptyViewId, parent, false);
            EmptyViewHolder evh = new EmptyViewHolder(v);
            return evh;
        }

        ViewHolder vh = new ViewHolder<>(getView(parent,viewClassesByType.get(viewType)));
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {
            ((ViewHolder<Q>) holder).bind(viewProvider.getObjectAtPosition(position), viewProvider.getItemCount(), position);
        }
    }

    @Override
    public int getItemCount() {
        return viewProvider.getItemCount();
    }

    public BindableView<T> getView(ViewGroup parent,Class<BindableView> viewClass) {
        BindableView<T> viewGroup=null;
        Method builderMethod=builderMethods.get(viewClass);
        if(builderMethod==null)
        {
            try
            {
                builderMethod=viewClass.getMethod("build", Context.class);
                builderMethods.put(viewClass,builderMethod);
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

    public void notifyAction(int actionId, int position, BindableView<?> view) {
        if (listEventListener != null) {
            listEventListener.onListEvent(actionId, position, view);
        }
    }
}
