package com.foobnix.mediaengine;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;

import com.foobnix.commons.LOG;
import com.foobnix.commons.TimeUtil;

public class MediaPlayerCore extends MediaPlayerFeatures implements MediaPlayer.OnCompletionListener {
    private final PlayListController playlistCtr;

    private final Handler handler = new Handler();

    public MediaPlayerCore(Context context) {
        super(context);
        playlistCtr = new PlayListController();
        setOnCompletionListener(this);
    }

    Runnable timer = new Runnable() {

        public void run() {
            onTimeSecond();
            handler.postDelayed(timer, 1000);
        }
    };

    public void onTimeSecond() {
        LOG.d("Send onTimeSecond");
        Intent intent = new Intent();
        intent.setAction("UpdateProggress");
        intent.putExtra("MediaEngineState", MediaEngineState.build(this));
        context.sendBroadcast(intent);
    }

    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public void next(){
        MediaModel nextModel = playlistCtr.getNextModel();
        play(nextModel);
    }

    public void prev() {
        MediaModel nextModel = playlistCtr.getPrevModel();
        play(nextModel);
    }

    public void playAtPot(int pos) {
        play(playlistCtr.getAtPos(pos));
    }

    public void play(MediaModel model) {
        if (model != null) {
            play(model.getPath());
        }
        model.setDuration(TimeUtil.durationToString(getDuration()));
    }
    public void play(String path) {
        try {
            playPath(path);
            handler.removeCallbacks(timer);
            handler.post(timer);
        } catch (IllegalArgumentException e) {
            LOG.e(e);
        } catch (IllegalStateException e) {
            LOG.e(e);
        } catch (IOException e) {
            LOG.e(e);
        }

    }

    public PlayListController getPlaylistCtr() {
        return playlistCtr;
    }


}
