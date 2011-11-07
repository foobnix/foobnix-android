package com.foobnix.activity;

import java.util.List;

import android.os.Bundle;

import com.foobnix.activity.hierarchy.GeneralListActivity;
import com.foobnix.adapter.ModelListAdapter;
import com.foobnix.adapter.PlaylistAdapter;
import com.foobnix.mediaengine.MediaEngineState;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaService;
import com.foobnix.R;

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
        adapter.notifyDataSetChanged();
    }

}
