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

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.foobnix.engine.FoobnixApplication;
import com.foobnix.util.C;

public class SleepTimerPreference {
	private ListPreference sleep;
	public ListPreference Builder(PreferenceActivity activity, final FoobnixApplication app) {

		sleep = new ListPreference(activity);
		sleep.setTitle("Stop Timer");
		sleep.setEntries(new String[] { "Disable", "15 mins", "30 mins", "45 mins", "60 mins", "75 mins",
		        "90 mins",
		        "105 mins", "120 mins" });
		sleep.setEntryValues(new String[] { "0", "15", "30", "45", "60", "75", "90", "105", "120" });
		
		updateSummary();

		sleep.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {				
				C.get().sleepMins[0] = Integer.parseInt((String) newValue);
				updateSummary();
				app.getAlarmSleepService().onLastActivation();
				app.getNotificationManager().displayNotifcation(C.get().sleepMins);
				return false;
			}
		});

		return sleep;
	}

	private void updateSummary() {
		sleep.setDefaultValue("" + C.get().sleepMins);

		if (C.get().sleepMins[0] <= 0) {
			sleep.setSummary("Disabled");
		} else {
			sleep.setSummary(String.format("Stop from last activity in %s mins", C.get().sleepMins[0]));
		}
	}

}
