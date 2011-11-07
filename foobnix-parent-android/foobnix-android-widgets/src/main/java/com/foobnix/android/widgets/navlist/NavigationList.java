package com.foobnix.android.widgets.navlist;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.foobnix.android.widgets.R;
import com.foobnix.android.widgets.list.ModelListAdapter;
import com.foobnix.android.widgets.list.OnModelClickListener;


public class NavigationList extends LinearLayout implements OnModelClickListener<NavigationItem> {
    protected ModelListAdapter<NavigationItem> adapter;
    private final List<NavigationItem> items = new ArrayList<NavigationItem>();
    protected ListView listView;
	
	public NavigationList(Context context) {
        super(context, null);
	}
	
    public NavigationList(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.nav_list, this);

        listView = (ListView) layout.findViewById(R.id.listView);
        adapter = new NavigationListAdapter(context, items);

        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);

        // String path = Environment.getExternalStorageDirectory().getPath();

        items.add(new NavigationItem(R.drawable.icon, "title1", "summary1"));
        items.add(new NavigationItem(-1, "title2", null));
        items.add(new NavigationItem(R.drawable.icon, null, "summary3"));
        items.add(new NavigationItem(R.drawable.icon, "title4", "summary4"));

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onModelItemClickListener(NavigationItem model) {
        Log.d("DEBUG", "item click" + model.toString());

    }

    @Override
    public void onModelItemLongClickListener(NavigationItem model) {
        Log.d("DEBUG", "item long click" + model.toString());

    }

}
