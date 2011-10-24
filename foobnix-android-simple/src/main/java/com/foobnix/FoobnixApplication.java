package com.foobnix;

import android.app.Application;

import com.foobnix.mediaengine.MediaServiceControls;
import com.foobnix.mediaengine.PlayListController;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public class FoobnixApplication extends Application {
    private PlayListController playlistCtr;
    private int scrollX, scrollY;

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

    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public int getScrollY() {
        return scrollY;
    }


}
