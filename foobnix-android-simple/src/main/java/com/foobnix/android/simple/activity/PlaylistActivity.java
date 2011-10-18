package com.foobnix.android.simple.activity;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;

import com.foobnix.android.simple.core.ModelListAdapter;
import com.foobnix.android.simple.core.playlist.PlaylistAdapter;
import com.foobnix.commons.LOG;
import com.foobnix.mediaengine.MediaEngineState;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaModels;
import com.foobnix.mediaengine.MediaService;

public class PlaylistActivity extends GeneralListActivity<MediaModel> {
    private MediaModels models;

    @Override
    public Class<? extends ModelListAdapter<MediaModel>> getAdapter() {
        return PlaylistAdapter.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onActivate(this);

        models = (MediaModels) getIntent().getSerializableExtra(MediaModels.class.getName());

        if (models != null) {
            addItems(models.getItems());
            MediaService.setPlaylist(models);
        } else {
            MediaService.setPlaylist(MediaModels.empty());
        }

        disableSettingButton();

    }

    @Override
    public List<MediaModel> getInitItems() {
        return Collections.emptyList();
    }

    @Override
    public void onModelItemClickListener(MediaModel model) {
        LOG.d(model.getPath(), model.getPosition());
        MediaService.playAtPos(model.getPosition());
    }

    @Override
    public void onUpdateUIListener(MediaEngineState state) {
        super.onUpdateUIListener(state);
        setInfoLineText("" + state.getModel().getTitle());
        ((PlaylistAdapter) adapter).setCurrent(state.getModel());
    }

}
