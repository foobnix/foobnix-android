package com.foobnix.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;

import com.foobnix.activity.hierarchy.GeneralSearchActivity;
import com.foobnix.mediaengine.MediaModel;

public class DownloadsActivity extends GeneralSearchActivity {

    @Override
    public Activity getChildAcitvity() {
        return this;
    }

    @Override
    public List<MediaModel> getInitItems() {
        return Collections.emptyList();
    }
}