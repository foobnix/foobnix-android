package com.foobnix.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;

import com.foobnix.activity.hierarchy.GeneralSearchActivity;
import com.foobnix.commons.log.LOG;
import com.foobnix.commons.pref.Pref;
import com.foobnix.exception.VkErrorException;
import com.foobnix.integrations.SearchBy;
import com.foobnix.integrations.SearchQuery;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.prefs.Prefs;

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