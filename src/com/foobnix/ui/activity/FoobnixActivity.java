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

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.foobnix.R;
import com.foobnix.broadcast.FoobnixUIUpdater;
import com.foobnix.broadcast.SongLineUpdater;
import com.foobnix.broadcast.action.NewSongBroadcastAction;
import com.foobnix.broadcast.action.UIBrodcastAction;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.C;
import com.foobnix.util.Conf;
import com.foobnix.util.LOG;

public class FoobnixActivity extends FoobnixMenuActivity {
	private FoobnixUIUpdater foobnixUIUpdater;
	private SongLineUpdater songLineUpdater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		playlistView = (ListView) findViewById(R.id.listview_playlist);
		playlistView.setAdapter(playListAdapter);

		SeekBar seekBar = ((SeekBar) findViewById(R.id.playerSeekBar));
		seekBar.setOnSeekBarChangeListener(seekLisner);

		playlistView.setOnItemClickListener(onItemClick);
		playlistView.setOnItemLongClickListener(onDialog);

		Button playPause = (Button) findViewById(R.id.button_play_pause);
		playPause.setOnClickListener(onPlayPause);

		Button next = (Button) findViewById(R.id.button_next);
		next.setOnClickListener(onNext);

		Button prev = (Button) findViewById(R.id.button_prev);
		prev.setOnClickListener(onPrev);

		foobnixUIUpdater = new FoobnixUIUpdater(this, app);
		songLineUpdater = new SongLineUpdater(playListAdapter, playlistView);

		if (app.getNowPlayingSong().equals(FModel.Empty())) {
			app.getNotification().displayNotifcation(getActivityTitle(), C.get().sleepMins);
		}

		if (StringUtils.isEmpty(C.get().vkontakteToken) && app.isOnline()) {
			startActivity(new Intent(this, VkCheckActivity.class));
		}

		LOG.d("ON FoobnixActivity Create");
	}

	@Override
	protected void onStart() {
		super.onStart();
		LOG.d("On Start");


		final FModel stat = app.getNowPlayingSong();

		playlistView.post(new Runnable() {
			@Override
			public void run() {
				playListAdapter.updateCurrentView(stat.getPosition(), stat.getTime());

			}
		});
		LOG.d("UPDATE", stat.getPosition());

		FServiceHelper.getInstance().activateShortTimer(FoobnixActivity.this, true);
		playlistView.setSelection(stat.getPosition() - 2);

		registerReceiver(foobnixUIUpdater, new UIBrodcastAction().getIntentFilter());
		registerReceiver(songLineUpdater, new NewSongBroadcastAction().getIntentFilter());
	}

	@Override
	protected void onStop() {
		super.onStop();
		LOG.d("ON FoobnixActivity STOP");
		unregisterReceiver(foobnixUIUpdater);
		unregisterReceiver(songLineUpdater);
		FServiceHelper.getInstance().activateShortTimer(FoobnixActivity.this, false);
	}

	View.OnClickListener onPrev = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FServiceHelper.getInstance().prev(FoobnixActivity.this);
		}
	};

	View.OnClickListener onNext = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FServiceHelper.getInstance().next(getApplicationContext());
		}
	};

	View.OnClickListener onPlayPause = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FServiceHelper.getInstance().playPause(getApplicationContext());
		}
	};

	OnItemClickListener onItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
			FModel FModel = (FModel) adapterView.getItemAtPosition(pos);
			FServiceHelper.getInstance().play(getApplicationContext(), FModel);
		}
	};

	OnSeekBarChangeListener seekLisner = new OnSeekBarChangeListener() {
		private int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			this.progress = progress;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			FServiceHelper.getInstance().seekTo(FoobnixActivity.this, progress);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			FServiceHelper.getInstance().seekTo(FoobnixActivity.this, progress);
		}

	};

	OnItemLongClickListener onDialog = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> adapter, View view, final int pos, long arg3) {
			final FModel item = (FModel) adapter.getItemAtPosition(pos);
			actionDialog(item);
			return true;
		}
	};

	@Override
	protected void actionDialog(final FModel item) {
		new RunnableDialog(FoobnixActivity.this, getString(R.string.Player_List_Action))//

		        .Action(getString(R.string.Clean_Playlist), new Runnable() {

			        @Override
			        public void run() {
				        cleanPlayList();
			        }
		        })//

		        .Action(getString(R.string.Play), new Runnable() {
			        @Override
			        public void run() {
				        FServiceHelper.getInstance().play(getApplicationContext(), item);
			        }
		        }, item != null)//

		        .Action(getString(R.string.Delete), new Runnable() {
			        @Override
			        public void run() {
				        playListAdapter.remove(item);
				        playListManager.remove(item);
			        }
		        }, item != null)//

		        .Action(getString(R.string.Download), new Runnable() {

			        @Override
			        public void run() {
				        app.addToDownload(item);
			        }
		        }, item != null && item.getType() == TYPE.ONLINE)//

		        .Action(getString(R.string.Download_All), new Runnable() {
			        @Override
			        public void run() {
				        List<FModel> items = playListAdapter.getAllFModels();
				        for (FModel current : items) {
					        app.addToDownload(current);
				        }
			        }
		        })//

		        .show();
	}

	@Override
	public String getActivityTitle() {
		return String.format("Foobnix Player %s", Conf.getFoobnixVersion(FoobnixActivity.this));
	}

	public Class<?> activityClazz() {
		return FoobnixActivity.class;
	}

}
