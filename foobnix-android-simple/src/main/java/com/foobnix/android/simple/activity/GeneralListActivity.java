package com.foobnix.android.simple.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.ListView;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.core.ModelListAdapter;
import com.foobnix.android.simple.core.OnModelClickListener;
import com.foobnix.commons.LOG;

public abstract class GeneralListActivity<T> extends TopBarActivity implements OnModelClickListener<T> {

    protected ModelListAdapter<T> adapter;
    private final List<T> items = new ArrayList<T>();

    @Override
    public void onActivate(Activity activity) {
        setContentView(R.layout.general);
        super.onActivate(activity);

        ListView listView = (ListView) findViewById(R.id.listView);
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

    public abstract List<T> getInitItems();

    public void addItems(List<T> addItems) {
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
