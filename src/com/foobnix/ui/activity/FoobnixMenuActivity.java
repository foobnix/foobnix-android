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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.engine.PlayListManager;
import com.foobnix.model.FModel;
import com.foobnix.ui.activity.pref.PlayerPreferences;
import com.foobnix.ui.adapter.PlayListAdapter;
import com.foobnix.util.C;
import com.foobnix.util.LOG;

public abstract class FoobnixMenuActivity extends FoobnixCommonActivity {
	protected PlayListAdapter playListAdapter;
	protected PlayListManager playListManager;
	protected ListView playlistView;
	protected FoobnixApplication app;

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.getAlarmSleepService().onLastActivation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.player_menu, menu);
		return true;
	}

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
			finish();
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
