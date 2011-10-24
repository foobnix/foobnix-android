package com.foobnix.activity.hierarchy;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.activity.auth.VkLoginActivity;
import com.foobnix.adapter.ModelListAdapter;
import com.foobnix.adapter.SearchItemAdapter;
import com.foobnix.commons.LOG;
import com.foobnix.commons.StringUtils;
import com.foobnix.exception.VkErrorException;
import com.foobnix.integrations.IntegrationsQueryManager;
import com.foobnix.integrations.LastFmApiAdapter;
import com.foobnix.integrations.VkontakteApiAdapter;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.mediaengine.MediaService;
import com.foobnix.prefs.Prefs;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Res;
import com.foobnix.vkontakte.VkAudio;
import com.foobnix.widgets.AsyncDialog;

public abstract class GeneralSearchActivity extends GeneralListActivity<MediaModel> {
    protected IntegrationsQueryManager queryManager;
    protected VkontakteApiAdapter vkAdapter;
    protected EditText musicSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_search);

        if (StringUtils.isEmpty(Pref.getStr(this, Prefs.VKONTAKTE_TOKEN))) {
            startActivity(new Intent(this, VkLoginActivity.class));
        }

        vkAdapter = new VkontakteApiAdapter(Pref.getStr(this, Prefs.VKONTAKTE_TOKEN));
        LastFmApiAdapter lfmAdapter = new LastFmApiAdapter(Res.getStr(this, R.string.LAST_FM_API_KEY));
        queryManager = new IntegrationsQueryManager(vkAdapter, lfmAdapter);

        musicSearchText = (EditText) findViewById(R.id.musicSearchLine);

        musicSearchText.setVisibility(View.GONE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        onActivate(getChildAcitvity());
        disableSecondTopLine();
    }

    public abstract Activity getChildAcitvity();

    protected void onResume() {
        super.onResume();
        vkAdapter.setToken(Pref.getStr(this, Prefs.VKONTAKTE_TOKEN));
    };


    @Override
    public void onModelItemClickListener(final MediaModel model) {
        if (model.isFile()) {
            if (StringUtils.isNotEmpty(model.getPath())) {
                MediaService.playPath(model.getPath());
            } else {
                asyncPlay(model);
            }
        } else {
            asyncNavigation(model);
        }
    }

    private void asyncPlay(final MediaModel model) {
        new AsyncDialog(this, "Searching...") {

            private VkAudio audio;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    audio = vkAdapter.getMostRelevantSong(model.getArtistTitle());
                } catch (Exception e) {
                    LOG.e("async Play", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    if (audio == null || StringUtils.isEmpty(audio.getUrl())) {
                        Toast.makeText(getApplicationContext(), "Song not found", Toast.LENGTH_LONG).show();
                        return;
                    }
                    model.setPath(audio.getUrl());
                    MediaService.playPath(model.getPath());
                } else {
                    Toast.makeText(getApplicationContext(), "Search not complete", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), VkLoginActivity.class));
                }
            }
        };
    }

    private void asyncNavigation(final MediaModel model) {
        new AsyncDialog(this, "Searching...") {

            private List<MediaModel> searchResult;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    searchResult = queryManager.getSearchResult(model.getSearchQuery());
                } catch (VkErrorException e) {
                    return false;
                } catch (Exception e) {
                    LOG.e("getSearchResult Exception", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    setItems(searchResult);
                } else {
                    Toast.makeText(getApplicationContext(), "Search not complete", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), VkLoginActivity.class));
                }
            }
        };
    }

    @Override
    public Class<? extends ModelListAdapter<MediaModel>> getAdapter() {
        return SearchItemAdapter.class;
    }

}
