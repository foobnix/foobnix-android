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
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.PlayListManager;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.provider.LastFmQueryManager;
import com.foobnix.ui.adapter.FolderAdapter;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.LOG;
import com.foobnix.util.Pref;
import com.foobnix.util.PrefKeys;
import com.foobnix.util.SongUtil;

public class LastFMActivity extends MediaParentActivity {

	private FolderAdapter adapter;
	private List<FModel> items = new ArrayList<FModel>();
	private ListView list;
	private LastFmQueryManager queryManager;
	
	private String currentUser = "ivanivanenko";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav);

		queryManager = new LastFmQueryManager(this);
		adapter = new FolderAdapter(this, items);
		adapter.setNotifyOnChange(true);
		
		items.addAll(queryManager.getSearchResult(new SearchQuery(SearchBy.LAST_FM_USER, currentUser)));

		list = (ListView) findViewById(R.id.dir_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(onClick);
		list.setOnItemLongClickListener(onDialog);

		onAcitvateMenuImages(this);
		queryManager.emtyStack();
		Pref.put(this, PrefKeys.ACTIVE_MEDIA_ACTIVITY, LastFMActivity.class.getName());
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
				adapter.update(result);
				list.setSelection(0);

			}
		}
	};

	@Override
	public void onBackPressed() {
		List<FModel> result = queryManager.previousPage();

		if (result.equals(Collections.EMPTY_LIST)) {
			finish();
			return;
		}

		LOG.d("Found Items: ", result.size());
		SongUtil.updateType(result, TYPE.ONLINE);
		adapter.update(result);
		list.setSelection(0);
	};

	OnItemLongClickListener onDialog = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> adapter, View arg1, final int pos, long arg3) {
			final FModel item = (FModel) adapter.getItemAtPosition(pos);
			actionDialog(item);
			return true;
		}
	};

	@Override
	protected void actionDialog(final FModel item) {
		final List<FModel> items = adapter.getItems();

		new RunnableDialog(LastFMActivity.this, getString(R.string.Last_fm_Action))//

		        .Action(getString(R.string.Open), new Runnable() {
			        @Override
			        public void run() {
				        List<FModel> result = queryManager.getSearchResult(item.getSearchQuery());
				        LOG.d("Found Items: ", result.size());
				        SongUtil.updateType(result, TYPE.ONLINE);
				        adapter.update(result);
				        list.setSelection(0);

			        }
		        }, item != null && item.isFolder())//

		        .Action(getString(R.string.Set_As_Playlist), new Runnable() {
			        @Override
			        public void run() {
				        cleanPlayList();

				        SongUtil.removeFolders(items);
				        app.getPlayListManager().addAll(items);

				        app.playOnAppend();
				        showPlayer();

			        }
		        }, SongUtil.isFileInList(items))//

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
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Append_All), new Runnable() {
			        @Override
			        public void run() {


				        SongUtil.removeFolders(items);
				        app.getPlayListManager().addAll(items);
				        app.playOnAppend();
				        showPlayer();

			        }

		        }, SongUtil.isFileInList(items))//

		        .Action(getString(R.string.Download), new Runnable() {
			        @Override
			        public void run() {
				        app.addToDownload(item);
			        }
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Download_All), new Runnable() {
			        @Override
			        public void run() {
				        for (FModel current : items) {
					        app.addToDownload(current);
				        }

			        }
		        }, SongUtil.isFileInList(items)).show();

	}


	@Override
	public String getActivityTitle() {
		return "LAST.FM";
	}

	@Override
	public Class<?> activityClazz() {
		return null;
	}

}

