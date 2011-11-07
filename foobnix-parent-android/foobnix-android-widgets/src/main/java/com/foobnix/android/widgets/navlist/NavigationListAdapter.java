package com.foobnix.android.widgets.navlist;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foobnix.android.widgets.R;
import com.foobnix.android.widgets.list.ModelListAdapter;

public class NavigationListAdapter extends ModelListAdapter<NavigationItem> {
    public NavigationListAdapter(Context context, List<NavigationItem> items) {
        super(context, items, null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_nav;
    }

    @Override
    public void getView(NavigationItem item, View newView) {

        ImageView image = (ImageView) newView.findViewById(R.id.icon);
        if (item.getResId() != -1) {
            image.setImageResource(item.getResId());
        } else {
            image.setImageDrawable(null);
        }

        TextView title = (TextView) newView.findViewById(R.id.title);
        if (item.getTitle() != null) {
            title.setText(item.getTitle());
        }

        TextView summary = (TextView) newView.findViewById(R.id.summary);
        if (item.getSummary() != null) {
            summary.setText(item.getSummary());
            summary.setVisibility(View.VISIBLE);
        } else {
            summary.setVisibility(View.GONE);
        }

        View clickable = (View) newView.findViewById(R.id.clickable);
        clickable.setOnClickListener(new OnModelClick(item));
    }
}
