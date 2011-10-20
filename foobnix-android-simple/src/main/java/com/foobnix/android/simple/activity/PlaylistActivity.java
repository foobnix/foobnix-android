package com.foobnix.android.simple.activity;

import java.util.List;

import android.os.Bundle;

import com.foobnix.android.simple.R;
import com.foobnix.android.simple.activity.hierarchy.GeneralListActivity;
import com.foobnix.android.simple.adapter.ModelListAdapter;
import com.foobnix.android.simple.adapter.PlaylistAdapter;
import com.foobnix.mediaengine.MediaEngineState;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaService;

public class PlaylistActivity extends GeneralListActivity<MediaModel> {

    @Override
    public Class<? extends ModelListAdapter<MediaModel>> getAdapter() {
        return PlaylistAdapter.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general);
        onActivate(this);
        disableSettingButton();
        MediaService.playAtPos(0);

    }

    @Override
    public List<MediaModel> getInitItems() {
        return app.getPlaylist().getPlayList();
    }

    @Override
    public void onModelItemClickListener(MediaModel model) {
        MediaService.playAtPos(model.getPosition());
    }

    @Override
    public void onUpdateUIListener(MediaEngineState state) {
        super.onUpdateUIListener(state);
        setInfoLineText("" + state.getModel().getTitle());
        ((PlaylistAdapter) adapter).setCurrent(state.getModel());
    }

}
