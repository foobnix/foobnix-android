package com.foobnix.android.simple.core.playlist;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.core.ModelListAdapter;
import com.foobnix.mediaengine.MediaModel;

public class PlaylistAdapter extends ModelListAdapter<MediaModel> {
    
    private MediaModel current;

    public PlaylistAdapter(Activity context, List<MediaModel> items) {
        super(context, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MediaModel item = getItem(position);

        View newView;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            newView = inflater.inflate(R.layout.playlist_item, null);
        } else {
            newView = convertView;
        }

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
            layout.setBackgroundColor(Color.argb(60, 255, 255, 0));
        } else {
            layout.setBackgroundColor(Color.TRANSPARENT);
        }

        layout.setOnClickListener(new OnModelClick(item));

        return newView;
    }


    public void setCurrent(MediaModel current) {
        this.current = current;
    }

    public MediaModel getCurrent() {
        return current;
    }

}
