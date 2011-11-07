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

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.foobnix.R;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Prefs;

public class SkipSongErorrPreferences {
	private CheckBoxPreference enable;

	public CheckBoxPreference Builder(PreferenceActivity activity) {
		enable = new CheckBoxPreference(activity);
		enable.setKey(Prefs.IS_SKIP_ERRORS);
		enable.setOnPreferenceClickListener(onClick);
		enable.setChecked(Pref.getBool(activity, Pref.IS_SKIP_ERRORS, false));
		enable.setTitle(R.string.Skip_Song_With_Error);
		return enable;

	}

	OnPreferenceClickListener onClick = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			Pref.putBool(preference.getContext(), Pref.IS_SKIP_ERRORS, enable.isChecked());
			return false;
		}

	};

}
