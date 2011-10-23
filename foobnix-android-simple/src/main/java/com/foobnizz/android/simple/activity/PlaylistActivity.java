package com.foobnizz.android.simple.activity;

import java.util.List;

import android.os.Bundle;

import com.foobnizz.android.simple.R;
import com.foobnizz.android.simple.activity.hierarchy.GeneralListActivity;
import com.foobnizz.android.simple.adapter.ModelListAdapter;
import com.foobnizz.android.simple.adapter.PlaylistAdapter;
import com.foobnizz.android.simple.mediaengine.MediaEngineState;
import com.foobnizz.android.simple.mediaengine.MediaModel;
import com.foobnizz.android.simple.mediaengine.MediaService;

public class PlaylistActivity extends GeneralListActivity<MediaModel> {

    @Override
    public Class<? extends ModelListAdapter<MediaModel>> getAdapter() {
        return PlaylistAdapter.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list);
        onActivate(this);

        disableSecondTopLine();
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
