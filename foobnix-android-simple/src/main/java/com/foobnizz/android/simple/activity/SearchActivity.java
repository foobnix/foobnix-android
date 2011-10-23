package com.foobnizz.android.simple.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.foobnix.commons.StringUtils;
import com.foobnix.exception.VkErrorException;
import com.foobnix.util.pref.Pref;
import com.foobnizz.android.simple.activity.auth.VkLoginActivity;
import com.foobnizz.android.simple.activity.hierarchy.GeneralSearchActivity;
import com.foobnizz.android.simple.integrations.SearchBy;
import com.foobnizz.android.simple.integrations.SearchQuery;
import com.foobnizz.android.simple.mediaengine.MediaModel;
import com.foobnizz.android.simple.prefs.Prefs;
import com.foobnizz.android.simple.util.SongUtil;

public class SearchActivity extends GeneralSearchActivity {

    @Override
    public Activity getChildAcitvity() {
        return SearchActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (StringUtils.isEmpty(Pref.getStr(this, Prefs.VKONTAKTE_TOKEN))) {
            startActivity(new Intent(this, VkLoginActivity.class));
        }

        musicSearchText.setVisibility(View.VISIBLE);
        musicSearchText.addTextChangedListener(onTextChange);
    }



    TextWatcher onTextChange = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            String text = query.toString();
            if (!StringUtils.isEmpty(text)) {
                text = SongUtil.capitilizeWords(text);
                try {
                    setItems(queryManager.getSearchResult(new SearchQuery(SearchBy.SEARCH, text, text), true));
                } catch (VkErrorException e) {
                    startActivity(new Intent(getApplicationContext(), VkLoginActivity.class));
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };


    @Override
    public List<MediaModel> getInitItems() {
        try {
            return queryManager.getSearchResult(new SearchQuery(SearchBy.SEARCH, ""), true);
        } catch (VkErrorException e) {
            startActivity(new Intent(getApplicationContext(), VkLoginActivity.class));
            return Collections.emptyList();
        }
    }

}
