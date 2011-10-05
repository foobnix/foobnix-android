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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.util.C;
import com.foobnix.util.LOG;
import com.foobnix.util.pref.Pref;

public class PrefMenuActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	protected FoobnixApplication app;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		LOG.d("On Pref changes", key);
		Preference pref = findPreference(key);

		if (pref instanceof EditTextPreference) {
			EditTextPreference listPref = (EditTextPreference) pref;
			Pref.putStr(this, key, listPref.getText());
		}

		if (pref instanceof CheckBoxPreference) {
			CheckBoxPreference listPref = (CheckBoxPreference) pref;
			Pref.putBool(this, key, listPref.isChecked());
		}

	}

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
	protected void onPause() {
		super.onPause();
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

}
