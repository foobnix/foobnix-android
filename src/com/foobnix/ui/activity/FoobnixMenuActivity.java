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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.engine.PlayListManager;
import com.foobnix.model.FModel;
import com.foobnix.ui.activity.info.AboutArtistActivity;
import com.foobnix.ui.activity.pref.PlayerPreferences;
import com.foobnix.ui.adapter.PlayListAdapter;
import com.foobnix.util.C;
import com.foobnix.util.LOG;

public abstract class FoobnixMenuActivity extends FoobnixCommonActivity {
	protected PlayListAdapter playListAdapter;
	protected PlayListManager playListManager;
	protected ListView playlistView;
	protected FoobnixApplication app;
	protected View menuLyaout;

	private void onMoreAction() {
		actionDialog(null);
	}

	protected abstract void actionDialog(final FModel item);

	public abstract Class<?> activityClazz();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (FoobnixApplication) getApplication();
		playListManager = app.getPlayListManager();
		playListAdapter = new PlayListAdapter(this, R.id.listview_playlist, playListManager.getAll());
		app.getAlarmSleepService().onLastActivation();

		LOG.d("On create Menu Activity");

	}

	public void hideShowMenu() {
		if (app.isShowMenu()) {
			menuLyaout.setVisibility(View.VISIBLE);
		} else {
			menuLyaout.setVisibility(View.GONE);
		}
		app.setShowMenu(!app.isShowMenu());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		hideShowMenu();
		return false;
	}

	public void onAcitvateMenuImages(Context contex) {
		new ButtonImageBindActivity(contex, R.id.imageAdd, MediaActivity.class);
		new ButtonImageBindActivity(contex, R.id.imageAddNav, MediaActivity.class);
		new ButtonImageBindActivity(contex, R.id.imagePlayer, FoobnixActivity.class);
		new ButtonImageBindActivity(contex, R.id.imageDownload, DMActitivy.class);
		new ButtonImageBindActivity(contex, R.id.imageInfo, AboutArtistActivity.class);
		new ButtonImageBindActivity(contex, R.id.imageSettins, PlayerPreferences.class, false);
		new ButtonImageBindActivity(contex, R.id.imageMore, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMoreAction();
			}
		});

		menuLyaout = (View) findViewById(R.id.buttons_override);

	}

	class ButtonImageBindActivity {
		public ButtonImageBindActivity(final Context context, int buttonId, final Class activityClazz,
		        final boolean isFinish) {
			View view = (View) findViewById(buttonId);
			if (view == null) {
				return;
			}
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isFinish) {
						LOG.d("Finish this activity");
						finish();
					}
					startActivity(new Intent(context.getApplicationContext(), activityClazz));
				}
			});
			view.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (MotionEvent.ACTION_DOWN == event.getAction()) {
						v.setBackgroundColor(Color.GRAY);
						LOG.d("on image down");
					} else if (MotionEvent.ACTION_UP == event.getAction()) {
						v.setBackgroundColor(Color.TRANSPARENT);
					}
					return false;
				}

			});
		}

		public ButtonImageBindActivity(final Context context, int buttonId, final Class activityClazz) {
			this(context, buttonId, activityClazz, true);
		}

		public ButtonImageBindActivity(final Context context, int buttonId, final View.OnClickListener onClickLisner) {
			View view = (View) findViewById(buttonId);

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onClickLisner.onClick(v);
				}
			});

			view.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (MotionEvent.ACTION_DOWN == event.getAction()) {
						v.setBackgroundColor(Color.GRAY);
						LOG.d("on image down");
					} else if (MotionEvent.ACTION_UP == event.getAction()) {
						v.setBackgroundColor(Color.TRANSPARENT);
					}
					return false;
				}

			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.getAlarmSleepService().onLastActivation();
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * super.onCreateOptionsMenu(menu); MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.player_menu, menu); return
	 * true; }
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		C.get().save(this);
	}

	protected void showPlayer() {
		finish();
		startActivity(new Intent(this, FoobnixActivity.class));
	}

	public void cleanPlayList() {
		playListAdapter.clear();
		playListManager.clear();
		if (playlistView != null) {
			playlistView.setSelection(0);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.playerMore:
			onMoreAction();
			return true;
		case R.id.playerSettingsMenu:
			startActivity(new Intent(this, PlayerPreferences.class));
			return true;
		case R.id.playerDowload:
			finish();
			startActivity(new Intent(this, DMActitivy.class));
			return true;
		case R.id.playerMedia:
			finish();
			startActivity(new Intent(this, MediaActivity.class));
			return true;
		case R.id.playerInfo:
			finish();
			startActivity(new Intent(this, InfoActivity.class));
			return true;
		case R.id.playerPlayer:
			openActivity(FoobnixActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openActivity(Class<?> clazz) {
		LOG.d(activityClazz(), clazz);
		if (activityClazz() != clazz) {
			finish();
			startActivity(new Intent(this, clazz));
		}
	}

	public PlayListAdapter getPlayListAdapter() {
		return playListAdapter;
	}

	public ListView getPlaylistView() {
		return playlistView;
	}

}
