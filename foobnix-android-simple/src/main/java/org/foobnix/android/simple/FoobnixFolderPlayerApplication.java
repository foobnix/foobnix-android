package org.foobnix.android.simple;

import java.util.ArrayList;
import java.util.List;

import org.foobnix.android.simple.mediaengine.MediaServiceControls;
import org.foobnix.android.simple.mediaengine.Model;

import android.app.Application;

public class FoobnixFolderPlayerApplication extends Application {
    private final List<Model> items = new ArrayList<Model>();

    @Override
    public void onCreate() {
        super.onCreate();
        MediaServiceControls.setContext(this);
    }

    public List<Model> getItems() {
        return items;
    }

}
