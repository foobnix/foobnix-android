package com.foobnix.android.simple;

import android.app.Application;

import com.foobnix.mediaengine.MediaServiceControls;
import com.foobnix.mediaengine.PlayListController;

public class FoobnixApplication extends Application {
    private PlayListController playlistCtr;

    @Override
    public void onCreate() {
        super.onCreate();
        playlistCtr = new PlayListController();
        MediaServiceControls.setContext(this);
    }

    public PlayListController getPlaylist() {
        return playlistCtr;
    }



}
