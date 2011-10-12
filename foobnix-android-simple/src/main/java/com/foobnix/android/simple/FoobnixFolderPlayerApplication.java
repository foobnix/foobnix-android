package com.foobnix.android.simple;

import android.app.Application;

import com.foobnix.mediaengine.MediaServiceControls;

public class FoobnixFolderPlayerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MediaServiceControls.setContext(this);
    }


}
