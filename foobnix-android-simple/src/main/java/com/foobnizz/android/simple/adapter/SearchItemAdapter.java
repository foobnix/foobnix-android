package com.foobnizz.android.simple.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.mediaengine.MediaModel;

public class SearchItemAdapter extends ModelListAdapter<MediaModel> {
    public SearchItemAdapter(Activity context, List<MediaModel> items) {
        super(context, items);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_search;
    }

    @Override
    public void getView(MediaModel item, View newView) {

        ImageView image = (ImageView) newView.findViewById(R.id.fileImage);

        TextView name = (TextView) newView.findViewById(R.id.fileName);
        name.setText(item.getArtistTitle());

        if (item.isFile()) {
            image.setImageResource(R.drawable.music_file_w);
            name.setTypeface(null, Typeface.NORMAL);
        } else {
            image.setImageResource(R.drawable.music_folder1);
            name.setTypeface(null, Typeface.BOLD);
        }
    }
}
