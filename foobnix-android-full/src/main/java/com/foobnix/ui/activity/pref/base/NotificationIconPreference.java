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

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.service.MODE;
import com.foobnix.util.C;

public class NotificationIconPreference {
	public ListPreference Builder(PreferenceActivity activity, final FoobnixApplication app) {
		final ListPreference trayIcon = new ListPreference(activity);
		trayIcon.setTitle(R.string.Notification_Icon);

		trayIcon.setEntries(MODE.AUTO_CANCEL.textValues());
		trayIcon.setEntryValues(MODE.AUTO_CANCEL.nameValues());

		trayIcon.setDefaultValue(C.get().notificationMode.name());
		trayIcon.setSummary(C.get().notificationMode.getText());

		trayIcon.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object value) {
				C.get().notificationMode = MODE.valueOf((String) value);
				trayIcon.setDefaultValue(C.get().notificationMode.name());
				trayIcon.setSummary(C.get().notificationMode.getText());
				app.getNotificationManager().updateIconMessage();
				return false;
			}
		});
		return trayIcon;
	}

}
