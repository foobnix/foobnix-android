package org.foobnix.android.simple.activity;

import org.foobnix.android.simple.R;
import org.foobnix.android.simple.core.OnModelClickListener;
import org.foobnix.android.simple.core.playlist.PlaylistAdapter;
import org.foobnix.android.simple.mediaengine.MediaEngineState;
import org.foobnix.android.simple.mediaengine.MediaService;
import org.foobnix.android.simple.mediaengine.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.foobnix.commons.LOG;
import com.foobnix.commons.TimeUtil;
import com.foobnix.commons.ViewBinder;

public class PlaylistActivity extends AppActivity implements OnModelClickListener<Model> {
    private SeekBar seekBar;
    private TextView trackTime;
    private TextView trackDuration;
    private TextView infoLine;
    private ImageButton playPause;

    private PlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        ListView listView = (ListView) findViewById(R.id.listView);

        adapter = new PlaylistAdapter(this, app.getItems());
        adapter.setOnModelClickListener(this);
        listView.setAdapter(adapter);

        ViewBinder.onClickActivity(this, R.id.playlistAdd, FoldersActivity.class);

        ViewBinder.onClick(this, R.id.playlist_next, onNext);
        ViewBinder.onClick(this, R.id.playlist_prev, onPrev);

        playPause = (ImageButton) findViewById(R.id.playlist_play_pause);
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
        seekBar.setMax(state.getDuration());
        seekBar.setProgress(state.getCurrentPosition());
        trackTime.setText(TimeUtil.durationToString(state.getCurrentPosition()));
        trackDuration.setText(TimeUtil.durationToString(state.getDuration()));
        infoLine.setText("" + state.getModel().getName());

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
    public void onClick(Model model) {
        MediaService.playAtPos(model.getPosition());
    }

}
