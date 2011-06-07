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
package com.foobnix.ui.activity.pref.base;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.foobnix.R;
import com.foobnix.util.C;

public class RandomModePreference {
	private CheckBoxPreference random;

	public CheckBoxPreference Builder(PreferenceActivity activity) {
		random = new CheckBoxPreference(activity);

		random.setOnPreferenceClickListener(onRandom);
		random.setChecked(C.get().isRandom);

		updateRandomTitle();
		return random;

	}

	OnPreferenceClickListener onRandom = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			C.get().isRandom = !C.get().isRandom;
			updateRandomTitle();
			return false;
		}

	};

	private void updateRandomTitle() {
		if (C.get().isRandom) {
			random.setTitle(R.string.Random_On);
		} else {
			random.setTitle(R.string.Random_Off);
		}
	}

}
