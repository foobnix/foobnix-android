package com.foobnix.android.simple.adapter;

import java.util.List;

import com.foobnix.android.simple.core.OnModelClickListener;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;

public class ModelListAdapter<T> extends ArrayAdapter<T> {

    protected OnModelClickListener<T> onModelClickListener;
    protected final Activity context;

    public ModelListAdapter(Activity context, List<T> items, OnModelClickListener<T> onModelClickListener) {
        super(context, -1, items);
        this.context = context;
        setNotifyOnChange(true);
        this.onModelClickListener = onModelClickListener;

    }

    public ModelListAdapter(Activity context, List<T> items) {
        this(context, items, null);
    }

    public void setOnModelClickListener(OnModelClickListener<T> onModelClickListener) {
        this.onModelClickListener = onModelClickListener;
    }

    public OnModelClickListener<T> getOnModelClickListener() {
        return onModelClickListener;
    }

    public class OnModelClick implements View.OnClickListener {
        T item;

        public OnModelClick(T item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (onModelClickListener != null) {
                onModelClickListener.onModelItemClickListener(item);
                notifyDataSetChanged();
            }

        }
    }
}

