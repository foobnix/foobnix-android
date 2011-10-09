package org.foobnix.android.simple;

import org.foobnix.android.simple.mediaengine.MediaServiceControls;

import android.app.Application;

public class FoobnixFolderPlayerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MediaServiceControls.setContext(this);
    }

}
