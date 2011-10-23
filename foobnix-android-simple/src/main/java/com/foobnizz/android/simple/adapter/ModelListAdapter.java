package com.foobnizz.android.simple.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.foobnizz.android.simple.core.OnModelClickListener;

public abstract class ModelListAdapter<T> extends ArrayAdapter<T> {

    protected OnModelClickListener<T> onModelClickListener;
    protected final Activity context;

    public ModelListAdapter(Activity context, List<T> items, OnModelClickListener<T> onModelClickListener) {
        super(context, -1, items);
        this.context = context;
        setNotifyOnChange(true);
        this.onModelClickListener = onModelClickListener;

    }

    public abstract int getLayoutId();

    public abstract void getView(final T item, View layout);

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final T item = getItem(position);

        View newView;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            newView = inflater.inflate(getLayoutId(), null);
        } else {
            newView = convertView;
        }

        getView(item, newView);

        return newView;
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

