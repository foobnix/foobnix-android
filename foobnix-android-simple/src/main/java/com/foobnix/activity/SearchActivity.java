package com.foobnix.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.foobnix.activity.auth.VkLoginActivity;
import com.foobnix.activity.hierarchy.GeneralSearchActivity;
import com.foobnix.commons.StringUtils;
import com.foobnix.exception.VkErrorException;
import com.foobnix.integrations.SearchBy;
import com.foobnix.integrations.SearchQuery;
import com.foobnix.mediaengine.MediaModel;
import com.foobnix.util.SongUtil;

public class SearchActivity extends GeneralSearchActivity {

    @Override
    public Activity getChildAcitvity() {
        return SearchActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
