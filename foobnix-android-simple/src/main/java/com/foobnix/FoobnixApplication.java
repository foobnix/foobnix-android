package com.foobnix;

import android.app.Application;

import com.foobnix.mediaengine.MediaServiceControls;
import com.foobnix.mediaengine.PlayListController;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public class FoobnixApplication extends Application {
    private PlayListController playlistCtr;

    @Override
    public void onCreate() {
        super.onCreate();
        playlistCtr = new PlayListController();

        MediaServiceControls.setContext(this);
        Caller.getInstance().setCache(new MemoryCache());
    }

    public PlayListController getPlaylist() {
        return playlistCtr;
    }

}
