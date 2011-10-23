package com.foobnizz.android.simple.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.mediaengine.MediaModel;

public class PlaylistAdapter extends ModelListAdapter<MediaModel> {

    private MediaModel current;

    public PlaylistAdapter(Activity context, List<MediaModel> items) {
        super(context, items);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_playlist;
    }

    @Override
    public void getView(MediaModel item, View newView) {
        TextView pos = (TextView) newView.findViewById(R.id.playlist_track_pos);
        pos.setText("" + (item.getPosition() + 1));

        TextView title = (TextView) newView.findViewById(R.id.playlist_title);
        title.setText(item.getTitle());

        TextView artist = (TextView) newView.findViewById(R.id.playlist_artist);
        artist.setText(item.getArtist());

        TextView time = (TextView) newView.findViewById(R.id.playlist_time);
        time.setText(item.getDuration());

        // playlist_item_layout
        LinearLayout layout = (LinearLayout) newView.findViewById(R.id.playlist_item_layout);

        if (item.equals(current)) {
            layout.setBackgroundColor(Color.argb(200, 0, 0, 255));
        } else {
            layout.setBackgroundColor(Color.TRANSPARENT);
        }

        layout.setOnClickListener(new OnModelClick(item));

    }

    public void setCurrent(MediaModel current) {
        this.current = current;
    }

    public MediaModel getCurrent() {
        return current;
    }

}
