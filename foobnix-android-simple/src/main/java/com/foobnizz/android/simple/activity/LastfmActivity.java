package com.foobnizz.android.simple.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;

import com.foobnix.commons.LOG;
import com.foobnix.exception.VkErrorException;
import com.foobnix.util.pref.Pref;
import com.foobnizz.android.simple.activity.hierarchy.GeneralSearchActivity;
import com.foobnizz.android.simple.integrations.SearchBy;
import com.foobnizz.android.simple.integrations.SearchQuery;
import com.foobnizz.android.simple.mediaengine.MediaModel;
import com.foobnizz.android.simple.prefs.Prefs;

public class LastfmActivity extends GeneralSearchActivity {

    @Override
    public Activity getChildAcitvity() {
        return this;
    }

    @Override
    public List<MediaModel> getInitItems() {
        String lastUser = Pref.getStr(this, Prefs.LASTFM_USER, "foobnix");
        try {
            return queryManager.getSearchResult(new SearchQuery(SearchBy.LAST_FM_USER, lastUser));
        } catch (VkErrorException e) {
            LOG.e(e);
            return Collections.emptyList();
        }
    }

}