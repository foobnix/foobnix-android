package org.foobnix.android.simple.mediaengine;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.foobnix.commons.LOG;

public class MediaPlayerCore extends MediaPlayerFeatures implements OnCompletionListener {
    private final PlayListController playlistCtr;
    
    public MediaPlayerCore(Context context) {
        super(context);
        playlistCtr = new PlayListController();
        setOnCompletionListener(this);
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

    public void play(Model model) {
        play(model.getPath());
    }
    public void play(String path) {
        try {
            playPath(path);
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
