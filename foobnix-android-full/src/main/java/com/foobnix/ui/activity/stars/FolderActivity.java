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
package com.foobnix.ui.activity.stars;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.PlaylistManager;
import com.foobnix.model.FModel;
import com.foobnix.ui.activity.TabActivity;
import com.foobnix.ui.widget.CommandString;
import com.foobnix.ui.widget.Dialogs;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.FileComparator;
import com.foobnix.util.FolderUtil;
import com.foobnix.util.LOG;
import com.foobnix.util.SongUtil;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Prefs;

public class FolderActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		onCreateActivity(this, R.layout.search);

		listView.setAdapter(navigationAdapter);
		navigationAdapter.update(Pref.getStr(this, Prefs.FOLDER_PREV_PATH, FolderUtil.ROOT_PATH));

		View controls = (View) findViewById(R.id.folderControls);
		controls.setVisibility(View.VISIBLE);
		menuLyaout.setVisibility(View.GONE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
		final FModel item = (FModel) adapter.getItemAtPosition(pos);
		if (item.isFile()) {
			if (Pref.getInt(this, Pref.PLAYLIST_MODE) == Pref.PLAYLIST_MODE_AUTOMATIC) {
				List<FModel> navItesm = navigationAdapter.getItems();
				List<FModel> cleanList = SongUtil.getListWithouFolders(navItesm);
				app.getPlayListManager().setList(cleanList);
				int indexOf = cleanList.indexOf(item);
				FServiceHelper.getInstance().playAtPos(getApplicationContext(), indexOf);
			} else {
				FServiceHelper.getInstance().play(getApplicationContext(), item);
			}

		} else {
			String path = item.getPath();
			navigationAdapter.update(path);
			Pref.putStr(FolderActivity.this, Pref.FOLDER_PREV_PATH, path);
		}

	}

	@Override
	protected void actionDialog(final FModel item) {
		final String downloadTo;

		if (item == null || item.getText().equals("..")) {
			downloadTo = navigationAdapter.getCurrentPath();
		} else {
			downloadTo = item.getPath();
		}

		new RunnableDialog(FolderActivity.this, getString(R.string.Folder_Action))//

		        .Action(getString(R.string.Set_As_Playlist), new Runnable() {
			        @Override
			        public void run() {
				        app.getPlayListManager().clear();

				        if (item == null) {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(navigationAdapter.getCurrentPath());
					        app.getPlayListManager().setList(models);
				        } else if (item.isFile()) {
					        addAll(new File(item.getPath()).getParent());
				        } else {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(item.getPath());

					        Collections.sort(models, new FileComparator());

					        app.getPlayListManager().setList(models);
				        }

				        app.playOnAppend();
				        showPlayer();
			        }

		        })//

		        .Action(getString(R.string.Add_To_Playlist), new Runnable() {
			        @Override
			        public void run() {
				        PlaylistManager manager = app.getPlayListManager();
				        manager.add(item);
				        app.playOnAppend();

			        }
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Add_All_To_Playlist), new Runnable() {
			        @Override
			        public void run() {

				        if (item == null) {
					        List<FModel> models = FolderUtil.getAllFilesRecursive(navigationAdapter.getCurrentPath());
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
				        try {
					        Thread.sleep(200);
				        } catch (InterruptedException e) {
				        }
				        navigationAdapter.update(navigationAdapter.getCurrentPath());
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

						        LOG.d("Create folder", navigationAdapter.getCurrentPath(), value);

						        boolean createDir = FolderUtil.createDir(navigationAdapter.getCurrentPath(), value);

						        if (!createDir) {
							        Toast.makeText(FolderActivity.this, "Can't crate dir " + value, Toast.LENGTH_LONG)
							                .show();
						        }
						        navigationAdapter.update(navigationAdapter.getCurrentPath());
					        }
				        });
			        }
		        })//

		        .Action(getString(R.string.Set_Download_To) + ": " + FolderUtil.getNameFromPath(downloadTo),
		                new Runnable() {
			                @Override
			                public void run() {
				                String to = FolderUtil.getFolderPath(downloadTo);
				                Pref.putStr(FolderActivity.this, Prefs.DOWNLOAD_TO, to);
				                Toast.makeText(FolderActivity.this, "Download Manager Set Download To " + to,
				                        Toast.LENGTH_LONG).show();
			                }
		                }, item == null || !item.isFile()).show();

	}

	public void addAll(String path) {
		List<FModel> list = FolderUtil.getNavItemsByPath(path);
		SongUtil.removeFolders(list);
		app.getPlayListManager().setList(list);
		Toast.makeText(FolderActivity.this, "Append All items", Toast.LENGTH_SHORT).show();

	}

}
