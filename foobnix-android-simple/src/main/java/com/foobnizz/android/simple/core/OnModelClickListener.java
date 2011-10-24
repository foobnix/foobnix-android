package com.foobnizz.android.simple.core;

public interface OnModelClickListener<T> {
    
    public void onModelItemClickListener(T model);

    public void onModelItemLongClickListener(T model);
    
}
