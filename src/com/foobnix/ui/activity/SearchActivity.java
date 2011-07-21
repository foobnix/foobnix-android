/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */
package com.foobnix.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.engine.PlayListManager;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.provider.IntegrationsQueryManager;
import com.foobnix.ui.adapter.FolderAdapter;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.C;
import com.foobnix.util.LOG;
import com.foobnix.util.Pref;
import com.foobnix.util.PrefKeys;
import com.foobnix.util.SongUtil;

public class SearchActivity extends MediaParentActivity {

    private IntegrationsQueryManager queryManager;
    private EditText editText;
    private FolderAdapter navAdapter;
    private List<FModel> items = new ArrayList<FModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);

        queryManager = app.getIntegrationsQueryManager();

        navAdapter = new FolderAdapter(this, items);
        navAdapter.setNotifyOnChange(true);

        items.addAll(queryManager.getSearchResult(new SearchQuery(SearchBy.SEARCH, "Madonna")));

        list = (ListView) findViewById(R.id.onlineListView);
        list.setAdapter(navAdapter);

        list.setOnItemLongClickListener(onDialog);
        list.setOnItemClickListener(onClick);

        FoobnixApplication app = (FoobnixApplication) getApplication();

        editText = (EditText) findViewById(R.id.onlineEditText);

        if (StringUtils.isEmpty(C.get().vkontakteToken) && app.isOnline()) {
            startActivity(new Intent(this, VkCheckActivity.class));
        }

        onAcitvateMenuImages(this);
        Pref.put(this, PrefKeys.ACTIVE_MEDIA_ACTIVITY, SearchActivity.class.getName());
    }

    private void paste() {
        String artist = app.getNowPlayingSong().getArtist();
        if (StringUtils.isNotEmpty(artist)) {
            editText.setText(artist);
        } else {
            editText.setText(app.getNowPlayingSong().getText());
        }
    }

    private void checkForEmpy(List<FModel> items) {
        if (items.isEmpty()) {
            items.add(FModelBuilder.PatternText(getString(R.string.Your_search_did_not_match_any_results))
                    .addArtist(""));
        }

    }

    private String capitilizeWords(String text) {
        String[] split = text.split(" ");
        StringBuilder result = new StringBuilder();
        for (String str : split) {
            String word = str.trim();
            if (StringUtils.isNotEmpty(word)) {
                String first = ("" + word.charAt(0)).toUpperCase();
                if (word.length() > 1) {
                    first = first + word.substring(1);
                }
                result.append(first);
                result.append(" ");
            }
        }
        return result.toString().trim();

    }

    OnItemClickListener onClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> a, View arg1, int pos, long arg3) {
            final FModel item = (FModel) a.getItemAtPosition(pos);
            LOG.d("Choose:", item.getText());

            if (item.isFile()) {
                FServiceHelper.getInstance().play(getApplicationContext(), item);
            } else {
                List<FModel> result = queryManager.getSearchResult(item.getSearchQuery());
                LOG.d("Found Items: ", result.size());
                SongUtil.updateType(result, TYPE.ONLINE);
                navAdapter.update(result);
                list.setSelection(0);

            }
        }
    };

    @Override
    protected void actionDialog(final FModel item) {
        new RunnableDialog(SearchActivity.this, getString(R.string.Online_Action))//

                .Action(String.format("%s: %s", getString(R.string.Paste), app.getNowPlayingSong().getArtist()),
                        new Runnable() {
                            @Override
                            public void run() {
                                paste();
                            }
                        }, StringUtils.isNotEmpty(app.getNowPlayingSong().getArtist()))//

                .Action(getString(R.string.Play), new Runnable() {
                    @Override
                    public void run() {
                        FServiceHelper.getInstance().play(getApplicationContext(), item);
                    }
                }, item != null && item.isFile())//

                .Action(getString(R.string.Set_As_Playlist), new Runnable() {
                    @Override
                    public void run() {
                        cleanPlayList();

                        List<FModel> items = navAdapter.getItems();
                        SongUtil.removeFolders(items);
                        app.getPlayListManager().addAll(items);

                        app.playOnAppend();
                        showPlayer();

                    }

                })//

                .Action(getString(R.string.Append), new Runnable() {
                    @Override
                    public void run() {
                        PlayListManager manager = app.getPlayListManager();
                        if (item.isFile()) {
                            manager.add(item);
                        } else {
                            ToastShort(item.getText() + "is not file");
                        }
                        app.playOnAppend();
                    }
                }, item != null)//

                .Action(getString(R.string.Append_All), new Runnable() {
                    @Override
                    public void run() {

                        List<FModel> items = navAdapter.getItems();
                        SongUtil.removeFolders(items);
                        app.getPlayListManager().addAll(items);
                        app.playOnAppend();
                        showPlayer();

                    }

                })//

                .Action(getString(R.string.Download), new Runnable() {
                    @Override
                    public void run() {
                        app.addToDownload(item);
                    }
                }, item != null && item.isFile())//

                .Action(getString(R.string.Download_All), new Runnable() {
                    @Override
                    public void run() {

                        List<FModel> items = navAdapter.getItems();
                        for (FModel current : items) {
                            app.addToDownload(current);
                        }

                    }
                }, item != null && item.isFile()).show();

    }

    OnItemLongClickListener onDialog = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(final AdapterView<?> adapter, View arg1, final int pos, long arg3) {
            final FModel item = (FModel) adapter.getItemAtPosition(pos);
            if (item.getType() != TYPE.ONLINE) {
                return false;
            }
            actionDialog(item);
            return true;
        }
    };
    private ListView list;

    @Override
    public String getActivityTitle() {
        return "Online Music Search";
    }

    @Override
    public Class<?> activityClazz() {
        return null;
    }

}
