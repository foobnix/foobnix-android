package org.foobnix.android.simple.activity;

import java.util.ArrayList;
import java.util.List;

import org.foobnix.android.simple.R;
import org.foobnix.android.simple.core.OnModelClickListener;
import org.foobnix.android.simple.core.playlist.PlaylistAdapter;
import org.foobnix.android.simple.mediaengine.MediaService;
import org.foobnix.android.simple.mediaengine.Model;
import org.foobnix.android.simple.mediaengine.Models;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.foobnix.commons.ViewBinder;

public class PlaylistActivity extends Activity implements OnModelClickListener<Model> {
    PlaylistAdapter adapter;
    final List<Model> items = new ArrayList<Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        ListView listView = (ListView) findViewById(R.id.listView);

        Models models = (Models) getIntent().getSerializableExtra("Models");
        if (models != null) {
            items.addAll(models.getItems());
            MediaService.setPlaylist(models);
        }

        adapter = new PlaylistAdapter(this, items);
        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);

        ViewBinder.onClickActivity(this, R.id.playlistAdd, FoldersActivity.class);

        ViewBinder.onClick(this, R.id.playlist_next, onNext);
        ViewBinder.onClick(this, R.id.playlist_prev, onPrev);
        ViewBinder.onClick(this, R.id.playlist_play_pause, onPlayPause);
    }

    View.OnClickListener onNext = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MediaService.next();
        }
    };
    View.OnClickListener onPlayPause = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MediaService.playPause();
        }
    };

    View.OnClickListener onPrev = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MediaService.prev();
        }
    };

    @Override
    public void onClick(Model model) {
        MediaService.playPath(model.getPath());
    }

}
