package com.foobnix.android.simple.activity;

import java.util.List;

import android.os.Bundle;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.activity.hierarchy.GeneralListActivity;
import com.foobnix.android.simple.adapter.ModelListAdapter;
import com.foobnix.mediaengine.MediaModel;

public class SearchActivity extends GeneralListActivity<MediaModel> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general);
        onActivate(this);
        disableSettingButton();

    }

    @Override
    public void onModelItemClickListener(MediaModel model) {

    }
    @Override
    public Class<? extends ModelListAdapter<MediaModel>> getAdapter() {
        return null;
    }

    @Override
    public List<MediaModel> getInitItems() {
        return null;
    }

}
