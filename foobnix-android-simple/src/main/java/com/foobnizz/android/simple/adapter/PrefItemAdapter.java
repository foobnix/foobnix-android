package com.foobnizz.android.simple.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.model.PrefItem;

public class PrefItemAdapter extends ModelListAdapter<PrefItem> {
    public PrefItemAdapter(Activity context, List<PrefItem> items) {
        super(context, items);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pref;
    }

    @Override
    public void getView(PrefItem item, View newView) {

        ImageView image = (ImageView) newView.findViewById(R.id.icon);
        image.setImageResource(item.getResId());

        TextView title = (TextView) newView.findViewById(R.id.title);
        title.setText(item.getTitle());

        TextView summary = (TextView) newView.findViewById(R.id.summary);
        summary.setText(item.getSummary());

        View clickable = (View) newView.findViewById(R.id.clickable);
        clickable.setOnClickListener(new OnModelClick(item));
    }
}
