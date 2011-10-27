package com.foobnix.activity.hierarchy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.foobnix.adapter.ModelListAdapter;
import com.foobnix.commons.log.LOG;
import com.foobnix.core.OnModelClickListener;
import com.foobnix.R;

public abstract class GeneralListActivity<T> extends PlayMenuActivity implements OnModelClickListener<T> {

    protected ModelListAdapter<T> adapter;
    private final List<T> items = new ArrayList<T>();
    protected ListView listView;

    @Override
    public void onActivate(Activity activity) {
        super.onActivate(activity);

        listView = (ListView) findViewById(R.id.listView);
        adapter = null;

        try {
            items.addAll(getInitItems());
            adapter = getAdapter().getDeclaredConstructor(Activity.class, List.class).newInstance(activity, items);
        } catch (Exception e) {
            LOG.e(e);
            new RuntimeException("Adapter Exception", e);
        }

        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);
    }

    
    @Override
    public void onModelItemLongClickListener(T model) {
    };
    

    public void hideList() {
        listView.setVisibility(View.GONE);
    }

    public abstract List<T> getInitItems();

    public void addItems(List<T> addItems) {
        items.addAll(addItems);
        adapter.notifyDataSetChanged();
    }

    public void setItems(List<T> addItems) {
        items.clear();
        items.addAll(addItems);
        adapter.notifyDataSetChanged();
    }

    public void cleanItems() {
        items.clear();
        adapter.notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    public abstract Class<? extends ModelListAdapter<T>> getAdapter();

}
