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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.engine.PlayListManager;
import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.FModelBuilder;
import com.foobnix.service.OnlineManager;
import com.foobnix.ui.adapter.FolderAdapter;
import com.foobnix.ui.widget.ImageBackgroundDecorator;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.C;
import com.foobnix.util.LOG;
import com.foobnix.util.Pref;
import com.foobnix.util.PrefKeys;
import com.foobnix.util.SongUtil;

public class OnlineActivity extends MediaParentActivity {
	public enum SEARCH_BY {
		TRACKS_BY_ARTIST(R.string.Tracks, true), //
		ALBUMS_BY_ARTIST(R.string.Albums, true), //
		SIMILAR_ARTIST_BY_ARTIST(R.string.Similar, true), //
		TAGS_BY_TAG(R.string.Tags, true), //
		ALL_AUDIO(R.string.Audio, true), //
		// unvisible items
		TRACKS_BY_ALBUM(-1, false), //
		TRACKS_BY_TAG(-1, false); //

		private final int textId;
		private final boolean display;

		private SEARCH_BY(int textId, boolean display) {
			this.textId = textId;
			this.display = display;
		}

		public String getText(Context context) {
			if (textId == -1) {
				return "";
			}

			return context.getString(textId);
		}

		public boolean isDisplay() {
			return display;
		}
	};

	private EditText editText;
	private FolderAdapter navAdapter;
	private OnlineManager onlineManager;
	private List<FModel> items = new ArrayList<FModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.online);

		navAdapter = new FolderAdapter(this, items);
		navAdapter.setNotifyOnChange(true);

		onlineManager = new OnlineManager(this);

		list = (ListView) findViewById(R.id.onlineListView);
		list.setAdapter(navAdapter);

		list.setOnItemLongClickListener(onDialog);
		list.setOnItemClickListener(onClick);

		FoobnixApplication app = (FoobnixApplication) getApplication();

		textSpinner = (TextView) findViewById(R.id.textSpinner);
		textSpinner.setText(SEARCH_BY.TRACKS_BY_ARTIST.getText(this));

		textSpinner.setOnClickListener(onSearchChoose);

		editText = (EditText) findViewById(R.id.onlineEditText);

		View search = (View) findViewById(R.id.onlineSearch);
		search.setOnClickListener(onSearch);
		search.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				paste();
				return true;
			}
		});
		ImageBackgroundDecorator.backgroundOnTouch(search);

		if (StringUtils.isEmpty(C.get().vkontakteToken) && app.isOnline()) {
			startActivity(new Intent(this, VkCheckActivity.class));
		}

		navAdapter.update(app.getOnlineItems());

		onAcitvateMenuImages(this);
		Pref.put(this, PrefKeys.ACTIVE_MEDIA_ACTIVITY, OnlineActivity.class.getName());
	}

	View.OnClickListener onSearchChoose = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(OnlineActivity.this);
			builder.setTitle("Search By");
			builder.setItems(getAllSearchByValues(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					textSpinner.setText(getAllSearchByValues()[item]);
					dialog.cancel();
				}
			});
			builder.show();

		}
	};

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
			items.add(FModelBuilder.CreateFromText(getString(R.string.Your_search_did_not_match_any_results))
			        .addArtist(""));
		}

	}

	View.OnClickListener onSearch = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			if (!app.isOnline()) {
				ToastLong(getString(R.string.Network_not_available));
				return;
			}

			String ask = editText.getText().toString();
			try {
				if (StringUtils.isNotEmpty(ask)) {
					ask = capitilizeWords(ask.trim());
					// SEARCH_BY searchBy = getByText((String)
					// spinner.getSelectedItem());
					SEARCH_BY searchBy = getByText((String) textSpinner.getText());
					updateByTypes(FModelBuilder.Search(ask, searchBy));
				}

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			} catch (Exception e) {
				LOG.e("Search error", e);
				ToastShort("Search Error, maybe Network error");
			}
		}
	};

	private String capitilizeWords(String text) {
		String[] split = text.split(" ");
		StringBuilder result = new StringBuilder();
		for (String str : split) {
			String word = str.trim();
			if(StringUtils.isNotEmpty(word)){
				String first = (""+word.charAt(0)).toUpperCase();
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
		public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
			final FModel item = (FModel) adapter.getItemAtPosition(pos);

			if (item.getType() != TYPE.ONLINE) {
				// Toast.makeText(OnlineActivity.this, item.getText(),
				// Toast.LENGTH_SHORT).show();
				// FServiceHelper.getInstance().play(getApplicationContext(),
				// item);
				return;
			}

			if (item.isFile()) {
				// PlayListManager manager = app.getPlayListManager();
				// manager.add(item);
				// Toast.makeText(OnlineActivity.this, getString(R.string.Added)
				// + ": " + item.getText(),
				// Toast.LENGTH_SHORT).show();
				FServiceHelper.getInstance().play(getApplicationContext(), item);
			} else {
				list.setSelection(0);
				updateByTypes(item);
			}
		}
	};

	private void updateByTypes(FModel item) {
		List<FModel> items = new ArrayList<FModel>();

		LOG.d("SEACH BY", item.getSearchBy());

		switch (item.getSearchBy()) {
		case TRACKS_BY_ARTIST:
			items = onlineManager.findTracksByArtist(item.getArtist());
			break;
		case ALBUMS_BY_ARTIST:
			items = onlineManager.findAlbumsByArtist(item.getArtist());
			break;
		case SIMILAR_ARTIST_BY_ARTIST:
			items = onlineManager.findSimilarArtistByArtist(item.getArtist());
			break;
		case TAGS_BY_TAG:
			items = onlineManager.findTagsByTag(item.getTag());
			break;
		case ALL_AUDIO:
			try {
				items = onlineManager.findTracksByVK(item.getText());
			} catch (VKAuthorizationException e) {
				startActivity(new Intent(OnlineActivity.this, VkCheckActivity.class));
			}
			break;

		// on click items
		case TRACKS_BY_ALBUM:
			LOG.d("Seach by Album");
			items = onlineManager.findTracksByArtistAlbum(item.getArtist(), item.getAlbum());
			break;
		case TRACKS_BY_TAG:
			items = onlineManager.findTracksByTag(item.getTag());
			break;

		default:
			LOG.d("TYPE not found", item.getType());
			break;
		}
		SongUtil.updateType(items, TYPE.ONLINE);
		SongUtil.updatePositions(items);
		checkForEmpy(items);
		updateList(items);

	}

	@Override
	protected void actionDialog(final FModel item) {
		new RunnableDialog(OnlineActivity.this, getString(R.string.Online_Action))//

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
	private TextView textSpinner;

	private String[] getAllSearchByValues() {
		List<String> result = new ArrayList<String>();
		for (SEARCH_BY ITEM : SEARCH_BY.values()) {
			if (ITEM.isDisplay()) {
				result.add(ITEM.getText(OnlineActivity.this));
			}
		}
		return result.toArray(new String[] {});
	}

	private SEARCH_BY getByText(String text) {
		for (SEARCH_BY ITEM : SEARCH_BY.values()) {
			if (ITEM.getText(OnlineActivity.this).equalsIgnoreCase(text)) {
				return ITEM;
			}
		}
		return SEARCH_BY.ALL_AUDIO;
	}

	private void updateList(List<FModel> items) {
		if (items == null) {
			return;
		}
		navAdapter.update(items);
		app.setOnlineItems(items);
	}

	@Override
	public String getActivityTitle() {
		return "Online Music Search";
	}

	@Override
	public Class<?> activityClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
