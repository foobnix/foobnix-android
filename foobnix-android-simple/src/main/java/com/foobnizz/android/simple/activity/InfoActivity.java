package com.foobnizz.android.simple.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;

import com.foobnizz.android.simple.activity.hierarchy.GeneralSearchActivity;
import com.foobnizz.android.simple.mediaengine.MediaModel;

public class InfoActivity extends GeneralSearchActivity {

    @Override
    public Activity getChildAcitvity() {
        return this;
    }

    @Override
    public List<MediaModel> getInitItems() {
        return Collections.emptyList();
    }
}