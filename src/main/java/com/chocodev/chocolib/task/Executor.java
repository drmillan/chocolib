package com.chocodev.chocolib.task;

/**
 * Created by drm on 25/09/15.
 */
public interface Executor<T,Q> {
    public Q execute(T input) throws Exception;
}