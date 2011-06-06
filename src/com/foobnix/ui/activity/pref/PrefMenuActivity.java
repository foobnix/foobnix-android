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
package com.foobnix.ui.activity.pref;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.ui.activity.DMActitivy;
import com.foobnix.ui.activity.FoobnixActivity;
import com.foobnix.ui.activity.InfoActivity;
import com.foobnix.ui.activity.MediaActivity;
import com.foobnix.util.C;

public class PrefMenuActivity extends PreferenceActivity {

	protected FoobnixApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (FoobnixApplication) getApplication();
		app.getAlarmSleepService().onLastActivation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.getAlarmSleepService().onLastActivation();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		C.get().save(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.player_menu, menu);
		return true;
	}

	public void onMoreAction() {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		finish();

		switch (item.getItemId()) {
		case R.id.playerMore:
			onMoreAction();

		case R.id.playerSettingsMenu:
			startActivity(new Intent(this, PlayerPreferences.class));
			return true;

		case R.id.playerDowload:
			startActivity(new Intent(this, DMActitivy.class));
			return true;

		case R.id.playerMedia:
			startActivity(new Intent(this, MediaActivity.class));
			return true;

		case R.id.playerInfo:
			startActivity(new Intent(this, InfoActivity.class));
			return true;

		case R.id.playerPlayer:
			startActivity(new Intent(this, FoobnixActivity.class));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
