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

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.PlayListManager;
import com.foobnix.model.FModel;
import com.foobnix.ui.MediaParentActivity;
import com.foobnix.ui.adapter.FolderAdapter;
import com.foobnix.ui.widget.CommandString;
import com.foobnix.ui.widget.Dialogs;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.FileComparator;
import com.foobnix.util.FolderUtil;
import com.foobnix.util.KEY;
import com.foobnix.util.PrefUtil;
import com.foobnix.util.SongUtil;

public class FolderActivity extends MediaParentActivity {

	private FolderAdapter navAdapter;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nav);
		setTitle("Navigation");

		list = (ListView) findViewById(R.id.dir_list);
		list.setOnItemLongClickListener(onDialog);
		list.setOnItemClickListener(onClick);

		navAdapter = new FolderAdapter(this, FolderUtil.getRootNavItems());
		navAdapter.update(PrefUtil.get(this, KEY.FOLDER_PREV_PATH, FolderUtil.ROOT_PATH));

		list.setAdapter(navAdapter);

		onAcitvateMenuImages(this);
	}

	OnItemClickListener onClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
			final FModel item = (FModel) adapter.getItemAtPosition(pos);
			if (item.isFile()) {
				// PlayListManager manager = app.getPlayListManager();
				// manager.add(item);
				// Toast.makeText(FolderActivity.this, getString(R.string.Added)
				// + ": " + item.getText(),
				// Toast.LENGTH_SHORT).show();
				FServiceHelper.getInstance().play(getApplicationContext(), item);
			} else {
				list.setSelection(0);
				String path = item.getPath();
				navAdapter.update(path);
				setTitle(path);
				PrefUtil.put(FolderActivity.this, KEY.FOLDER_PREV_PATH, path);
			}

		}
	};

	@Override
	protected void actionDialog(final FModel item) {
		final String downloadTo;

		if (item == null || item.getText().equals("..")) {
			downloadTo = navAdapter.getCurrentPath();
		} else {
			downloadTo = item.getPath();
		}

		new RunnableDialog(FolderActivity.this, getString(R.string.Folder_Action))//

		        .Action(getString(R.string.Play), new Runnable() {
			        @Override
			        public void run() {
				        FServiceHelper.getInstance().play(getApplicationContext(), item);
			        }
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Append), new Runnable() {
			        @Override
			        public void run() {
				        PlayListManager manager = app.getPlayListManager();
				        manager.add(item);
				        app.playOnAppend();

			        }
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Append_All), new Runnable() {
			        @Override
			        public void run() {

				        if (item == null) {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(navAdapter.getCurrentPath());
					        app.getPlayListManager().addAll(models);
				        } else if (item.isFile()) {
					        addAll(new File(item.getPath()).getParent());
				        } else {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(item.getPath());

					        Collections.sort(models, new FileComparator());

					        app.getPlayListManager().addAll(models);
				        }

				        app.playOnAppend();
				        showPlayer();
			        }
		        })//

		        .Action(getString(R.string.Set_As_Playlist), new Runnable() {
			        @Override
			        public void run() {
				        cleanPlayList();

				        if (item == null) {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(navAdapter.getCurrentPath());
					        app.getPlayListManager().addAll(models);
				        } else if (item.isFile()) {
					        addAll(new File(item.getPath()).getParent());
				        } else {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(item.getPath());

					        Collections.sort(models, new FileComparator());

					        app.getPlayListManager().addAll(models);
				        }

				        app.playOnAppend();
				        showPlayer();
			        }

		        })//

		        .Action(getString(R.string.Delete), new Runnable() {
			        @Override
			        public void run() {
				        FolderUtil.deleteFiles(item.getPath());
				        finish();
				        startActivity(new Intent(FolderActivity.this, OnlineActivity.class));
			        }
		        }, item != null)//

		        .Action(getString(R.string.Create_Folder), new Runnable() {
			        @Override
			        public void run() {
				        Dialogs.showCreateFolder(FolderActivity.this, new CommandString() {

					        @Override
					        public void run(String value) {
						        if (StringUtils.isEmpty(value)) {
							        return;
						        }
						        boolean createDir = FolderUtil.createDir(navAdapter.getCurrentPath(), value);

						        if (!createDir) {
							        ToastShort("Can't crate dir " + value);
						        } else {
							        finish();
							        startActivity(new Intent(FolderActivity.this, OnlineActivity.class));
						        }
					        }
				        });
			        }
		        })//

		        .Action(getString(R.string.Set_Download_To) + ": " + FolderUtil.getNameFromPath(downloadTo),
		                new Runnable() {
			        @Override
			        public void run() {
				        String to = FolderUtil.getFolderPath(downloadTo);
				        PrefUtil.put(FolderActivity.this, KEY.DOWNLOAD_TO, to);
				        ToastShort("Download Manager Set Download To " + to);
			        }
		        }, item == null || !item.isFile()).show();

	}

	OnItemLongClickListener onDialog = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> adapter, View arg1, final int pos, long arg3) {
			FModel item = (FModel) adapter.getItemAtPosition(pos);
			actionDialog(item);
			return true;
		}
	};

	public void addAll(String path) {
		List<FModel> list = FolderUtil.getNavItemsByPath(path);
		SongUtil.removeFolders(list);
		app.getPlayListManager().addAll(list);
		Toast.makeText(FolderActivity.this, "Append All items", Toast.LENGTH_SHORT).show();


	}

	@Override
	public String getActivityTitle() {
		return "Folder Navigation";
	}

	@Override
	public Class<?> activityClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
