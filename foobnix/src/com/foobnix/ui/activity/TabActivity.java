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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.PlaylistManager;
import com.foobnix.model.FModel;
import com.foobnix.provider.IntegrationsQueryManager;
import com.foobnix.ui.activity.other.DownloadActitivy;
import com.foobnix.ui.activity.other.VkCheckActivity;
import com.foobnix.ui.activity.stars.FolderActivity;
import com.foobnix.ui.activity.stars.LastWithVKActivity;
import com.foobnix.ui.activity.stars.PlaylistActivity;
import com.foobnix.ui.activity.stars.SearchActivity;
import com.foobnix.ui.adapter.NavigationAdapter;
import com.foobnix.ui.adapter.PlaylistAdapter;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.ui.widget.StarTab;
import com.foobnix.util.LOG;
import com.foobnix.util.SongUtil;
import com.foobnix.util.StarTabHelper;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Prefs;

public abstract class TabActivity extends MenuActivity {
	protected PlaylistAdapter playlistAdapter;
	protected NavigationAdapter navigationAdapter;
	protected List<FModel> items = new ArrayList<FModel>();
	protected IntegrationsQueryManager queryManager;
	protected ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			navigationAdapter.notifyDataSetChanged();
			if (playlistAdapter != null) {
				playlistAdapter.notifyDataSetChanged();
			}
			LOG.d("Notify playlist changed");
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(PlaylistActivity.class.getName()));
		navigationAdapter.notifyDataSetChanged();
		if (playlistAdapter != null) {
			playlistAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);

	}

	public boolean checkVkontakteToken() {
		if (StringUtils.isEmpty(Pref.getStr(this, Pref.VKONTAKTE_TOKEN)) && app.isOnline()) {
			startActivityForResult(new Intent(this, VkCheckActivity.class),VkCheckActivity.OK_RESULT);
			return false;
		}
		return true;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.d("onActivityResult", requestCode, resultCode, data);
        if (resultCode == VkCheckActivity.OK_RESULT) {
            finish();
        }
    }

	public void onActivateStarTabs(Activity context, int contentView) {
		setContentView(contentView);

		Pref.putStr(this, Prefs.ACTIVE_MEDIA_ACTIVITY, context.getClass().getName());

		StarTab pl = StarTabHelper.bindStarTab(this, context, R.id.playlistStartTab, PlaylistActivity.class,
		        R.string.Playlist);
		pl.setOnLongClickListener(onLongPl);

		StarTabHelper.bindStarTab(this, context, R.id.folderStartTab, FolderActivity.class, R.string.Folders);
		StarTabHelper.bindStarTab(this, context, R.id.onlineStartTab, SearchActivity.class, R.string.Search);
		StarTabHelper.bindStarTab(this, context, R.id.lastfmStartTab, LastWithVKActivity.class, R.string.Last_fm_VK);

		StarTab dm = StarTabHelper.bindStarTab(this, context, R.id.dmStartTab, DownloadActitivy.class, R.string.DM);
		dm.setOnLongClickListener(onLongDm);

	}

	View.OnLongClickListener onLongPl = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View arg0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(TabActivity.this);
			dialog.setTitle("Playlist Mode");
			dialog.setSingleChoiceItems(new String[] { "Automatic Add", "Manual Add" },
			        Pref.getInt(getApplicationContext(), Prefs.PLAYLIST_MODE), new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface arg0, int pos) {
					        LOG.d("POS", pos);
					        if (pos == 0) {
						        Pref.putInt(getApplicationContext(), Prefs.PLAYLIST_MODE, Prefs.PLAYLIST_MODE_AUTOMATIC);
						        LOG.d("simple");
					        } else if (pos == 1) {
						        Pref.putInt(getApplicationContext(), Prefs.PLAYLIST_MODE, Prefs.PLAYLIST_MODE_MANUAL);
						        LOG.d("complex");

					        }
					        arg0.dismiss();
				        }
			        });
			dialog.show();
			return true;
		}
	};

	View.OnLongClickListener onLongDm = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View arg0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(TabActivity.this);
			dialog.setTitle("Download Format");
			dialog.setSingleChoiceItems(new String[] { "[to]/[Artist]/Artist - Title", "[to]/Artist - Title" },
			        Pref.getInt(getApplicationContext(), Prefs.DOWNLOAD_MODE), new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface arg0, int pos) {
					        LOG.d("POS", pos);
					        if (pos == 0) {
						        Pref.putInt(getApplicationContext(), Prefs.DOWNLOAD_MODE, Prefs.DOWNLOAD_MODE_COMPLEX);
						        LOG.d("complex");
					        } else if (pos == 1) {
						        Pref.putInt(getApplicationContext(), Prefs.DOWNLOAD_MODE, Prefs.DOWNLOAD_MODE_SIMPLE);
						        LOG.d("simple");
					        }
					        arg0.dismiss();
				        }
			        });
			dialog.show();
			return false;
		}
	};

	public void onCreateActivity(Activity context, int contentView) {
		onActivateStarTabs(context, contentView);
		onActivateMenuActivity(context);

		queryManager = app.getIntegrationsQueryManager();

		navigationAdapter = new NavigationAdapter(this, items, app);
		navigationAdapter.setNotifyOnChange(true);

		listView = (ListView) findViewById(R.id.onlineListView);
		listView.setOnItemClickListener(onItemClickListener);
		listView.setOnItemLongClickListener(onItemLongClickListener);
	}

	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
		final FModel item = (FModel) adapterView.getItemAtPosition(pos);
		if (Pref.getInt(this, Pref.PLAYLIST_MODE) == Pref.PLAYLIST_MODE_AUTOMATIC) {
			List<FModel> navItesm = navigationAdapter.getItems();
			List<FModel> cleanList = SongUtil.getListWithouFolders(navItesm);
			app.getPlayListManager().setList(cleanList);
			int indexOf = cleanList.indexOf(item);
			FServiceHelper.getInstance().playAtPos(getApplicationContext(), indexOf);
		} else {
			FServiceHelper.getInstance().play(getApplicationContext(), item);
		}

	}

	public boolean onItemLongClick(final AdapterView<?> adapter, View view, final int pos, long arg3) {
		final FModel item = (FModel) adapter.getItemAtPosition(pos);
		actionDialog(item);
		return true;
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		public void onItemClick(android.widget.AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			TabActivity.this.onItemClick(arg0, arg1, arg2, arg3);
		};
	};

	OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			return TabActivity.this.onItemLongClick(arg0, arg1, arg2, arg3);
		}
	};

	protected void actionDialog(final FModel item) {
		final List<FModel> items = navigationAdapter.getItems();

		new RunnableDialog(this, getString(R.string.Action))//
		        .Action(getString(R.string.Set_As_Playlist), new Runnable() {
			        @Override
			        public void run() {
				        SongUtil.removeFolders(items);
				        app.getPlayListManager().setList(items);
				        app.playOnAppend();
				        showPlayer();
			        }
		        }, SongUtil.isFileInList(items))//

		        .Action(getString(R.string.Add_To_Playlist), new Runnable() {
			        @Override
			        public void run() {
				        PlaylistManager manager = app.getPlayListManager();
				        if (item.isFile()) {
					        manager.add(item);
				        }
				        app.playOnAppend();
			        }
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Add_All_To_Playlist), new Runnable() {
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
				        startActivity(new Intent(TabActivity.this, DownloadActitivy.class));
			        }
		        }, item != null && item.isFile())//

		        .Action(getString(R.string.Download_All), new Runnable() {
			        @Override
			        public void run() {
				        for (FModel current : items) {
					        app.addToDownload(current);
				        }
				        startActivity(new Intent(TabActivity.this, DownloadActitivy.class));

			        }
		        }, SongUtil.isFileInList(items)).show();

	}

}
