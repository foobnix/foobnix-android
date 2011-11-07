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

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.TimePicker;

import com.foobnix.engine.FoobnixApplication;
import com.foobnix.util.C;
import com.foobnix.util.TimeUtil;

public class AlarmPreference {

	private PreferenceScreen alarm;
	private FoobnixApplication app;

	public PreferenceScreen Builder(PreferenceActivity activity, final FoobnixApplication app) {
		this.app = app;
		alarm = activity.getPreferenceManager().createPreferenceScreen(activity);

		TimePickerDialog.OnTimeSetListener lister = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				C.get().alarmHM[0] = hourOfDay;
				C.get().alarmHM[1] = minute;
				updateSummary();
			}
		};
		int h = C.get().alarmHM[0] < 0 ? 0 : C.get().alarmHM[0];
		int m = C.get().alarmHM[1] < 0 ? 0 : C.get().alarmHM[1];
		final TimePickerDialog timeDialog = new TimePickerDialog(activity, lister, h, m, true);

		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("Enable Alarm?").setCancelable(false)
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
				        dialog.cancel();
				        timeDialog.show();
			        }
		        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
				        dialog.cancel();
				        C.get().alarmHM[0] = -1;
				        C.get().alarmHM[1] = -1;
				        updateSummary();
			        }
		        }).create();

		alarm.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				builder.show();
				return false;
			}
		});

		alarm.setTitle("Alarm");

		alarm.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				builder.create().show();
				return false;
			}
		});

		updateSummary();
		return alarm;
	}

	private void updateSummary() {
		if (C.get().alarmHM[0] < 0 || C.get().alarmHM[1] < 0) {
			alarm.setSummary("Disabled");
		} else {
			alarm.setSummary(TimeUtil.getTimeFormated(C.get().alarmHM));
		}
		app.getAlarmSleepService().onLastActivation();
		app.getNotificationManager().displayNotifcation();
	}

}
