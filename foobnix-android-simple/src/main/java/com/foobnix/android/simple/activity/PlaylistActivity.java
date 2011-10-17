package com.foobnix.android.simple.activity;

import java.util.Collections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.core.OnModelClickListener;
import com.foobnix.android.simple.core.playlist.PlaylistAdapter;
import com.foobnix.commons.LOG;
import com.foobnix.commons.TimeUtil;
import com.foobnix.commons.ViewBinder;
import com.foobnix.mediaengine.MediaEngineState;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaModels;
import com.foobnix.mediaengine.MediaService;

public class PlaylistActivity extends AppActivity implements OnModelClickListener<MediaModel> {
    private SeekBar seekBar;
    private TextView trackTime;
    private TextView trackDuration;
    private TextView infoLine;
    private ImageView playPause;

    private PlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        ListView listView = (ListView) findViewById(R.id.listView);

        MediaModels models = (MediaModels) getIntent().getSerializableExtra(MediaModels.class.getName());
        if (models == null) {
            models = new MediaModels(Collections.EMPTY_LIST);
            LOG.d("Playlist is empty");
        }
        LOG.d("Models items size", models.getItems().size());

        MediaService.setPlaylist(models);

        adapter = new PlaylistAdapter(this, models.getItems());
        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);

        ViewBinder.onClickActivity(this, R.id.playlistAdd, FoldersActivity.class);

        ViewBinder.onClick(this, R.id.playlist_next, onNext);
        ViewBinder.onClick(this, R.id.playlist_prev, onPrev);

        playPause = (ImageView) findViewById(R.id.playlist_play_pause);
        playPause.setOnClickListener(onPlayPause);

        seekBar = (SeekBar) findViewById(R.id.playilst_seek);
        seekBar.setOnSeekBarChangeListener(seekLisner);
        trackTime = (TextView) findViewById(R.id.playlist_track_time);
        trackDuration = (TextView) findViewById(R.id.playlist_track_duration);
        infoLine = (TextView) findViewById(R.id.playlist_info_line);
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(updateProggressReciver, new IntentFilter("UpdateProggress"));
    };

    protected void onPause() {
        super.onPause();
        unregisterReceiver(updateProggressReciver);
    };

    OnSeekBarChangeListener seekLisner = new OnSeekBarChangeListener() {
        private int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            MediaService.seekTo(progress);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MediaService.seekTo(progress);
        }

    };

    public void updateUi(MediaEngineState state) {
        if (state == null) {
            return;
        }
        seekBar.setMax(state.getDuration());
        seekBar.setProgress(state.getCurrentPosition());
        trackTime.setText(TimeUtil.durationToString(state.getCurrentPosition()));
        trackDuration.setText(TimeUtil.durationToString(state.getDuration()));
        infoLine.setText("" + state.getModel().getTitle());

        if (state.isPlaying()) {
            playPause.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playPause.setImageResource(android.R.drawable.ic_media_play);
        }
        adapter.setCurrent(state.getModel());
        adapter.notifyDataSetChanged();
    }

    BroadcastReceiver updateProggressReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LOG.br("recive BR updateUI");
            MediaEngineState state = (MediaEngineState) intent.getSerializableExtra("MediaEngineState");
            if (state != null) {
                updateUi(state);
            }
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
    View.OnClickListener onNext = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MediaService.next();

        }
    };

    @Override
    public void onClick(MediaModel model) {
        LOG.d(model.getPath(), model.getPosition());
        MediaService.playAtPos(model.getPosition());
    }

}
