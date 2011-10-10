package org.foobnix.android.simple.mediaengine;

import java.io.IOException;

import org.foobnix.android.simple.FoobnixFolderPlayerApplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;

import com.foobnix.commons.LOG;

public class MediaPlayerCore extends MediaPlayerFeatures implements OnCompletionListener {
    private final PlayListController playlistCtr;
    private final Handler handler = new Handler();
    public MediaPlayerCore(Context context) {
        super(context);
        FoobnixFolderPlayerApplication app = (FoobnixFolderPlayerApplication) context.getApplicationContext();
        playlistCtr = new PlayListController();
        playlistCtr.setPlaylist(app.getItems());

        setOnCompletionListener(this);
    }

    Runnable timer = new Runnable() {

        @Override
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public void next(){
        Model nextModel = playlistCtr.getNextModel();
        play(nextModel);
    }

    public void prev() {
        Model nextModel = playlistCtr.getPrevModel();
        play(nextModel);
    }

    public void playAtPot(int pos) {
        play(playlistCtr.getAtPos(pos));
    }

    public void play(Model model) {
        play(model.getPath());
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
