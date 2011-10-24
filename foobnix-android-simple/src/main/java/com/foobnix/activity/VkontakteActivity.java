package com.foobnix.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;

import com.foobnix.activity.hierarchy.GeneralSearchActivity;
import com.foobnix.commons.LOG;
import com.foobnix.exception.VkErrorException;
import com.foobnix.integrations.SearchBy;
import com.foobnix.integrations.SearchQuery;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.prefs.Prefs;
import com.foobnix.util.pref.Pref;

public class VkontakteActivity extends GeneralSearchActivity {

    @Override
    public Activity getChildAcitvity() {
        return this;
    }

    @Override
    public List<MediaModel> getInitItems() {
        String userId = Pref.getStr(this, Prefs.VKONTAKTE_USER_ID);
        try {
            return queryManager.getSearchResult(new SearchQuery(SearchBy.VK_USER_ID, userId));
        } catch (VkErrorException e) {
            LOG.e(e);
            return Collections.emptyList();
        }
    }

}