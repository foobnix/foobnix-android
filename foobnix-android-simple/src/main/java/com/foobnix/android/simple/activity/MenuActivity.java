package com.foobnix.android.simple.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.foobnix.android.simple.R;
import com.foobnix.commons.TimeUtil;
import com.foobnix.commons.ViewBinder;
import com.foobnix.mediaengine.MediaEngineState;
import com.foobnix.mediaengine.MediaService;

public class MenuActivity extends AppActivity {
    private SeekBar seekBar;
    private TextView trackTime;
    private TextView trackDuration;
    private ImageView playPause;

    @Override
    public void onActivate(Activity activity) {
        ViewBinder.onClick(activity, R.id.playlist_next, onNext);
        ViewBinder.onClick(activity, R.id.playlist_prev, onPrev);

        playPause = (ImageView) activity.findViewById(R.id.playlist_play_pause);
        playPause.setOnClickListener(onPlayPause);

        seekBar = (SeekBar) activity.findViewById(R.id.playilst_seek);
        seekBar.setOnSeekBarChangeListener(seekLisner);

        trackTime = (TextView) activity.findViewById(R.id.playlist_track_time);
        trackDuration = (TextView) activity.findViewById(R.id.playlist_track_duration);
    }

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
    public void onUpdateUIListener(MediaEngineState state) {
        seekBar.setMax(state.getDuration());
        seekBar.setProgress(state.getCurrentPosition());
        trackTime.setText(TimeUtil.durationToString(state.getCurrentPosition()));
        trackDuration.setText(TimeUtil.durationToString(state.getDuration()));

        if (state.isPlaying()) {
            playPause.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

}
