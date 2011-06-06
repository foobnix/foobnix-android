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
package com.foobnix.service;

import java.util.Calendar;

import android.content.Context;

import com.foobnix.engine.FServiceHelper;
import com.foobnix.util.C;
import com.foobnix.util.LOG;

public class AlarmSleepService {
	private final int[] sleepRemainMS = { 0, 0 };
	private long sleepDate;
	private Context context;
	private FoobnixNotification notification;

	public AlarmSleepService(Context context, FoobnixNotification notification) {
		this.context = context;
		this.notification = notification;
	}

	public void checkSleepAlarm(boolean isPlaing) {
		// alarmCheck(isPlaing);
		sleepCheck(isPlaing);
	}

	public void onLastActivation() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, C.get().sleepMins[0]);
		sleepDate = calendar.getTimeInMillis();
		checkSleepAlarm(true);
		notification.displayNotifcation(sleepRemainMS);
	}

	private void alarmCheck(boolean isPlaying) {
		if (C.get().alarmHM[0] <= -1 || C.get().alarmHM[1] <= -1) {
			return;
		}
		if (isPlaying) {
			return;
		}

		Calendar current = Calendar.getInstance();

		int h = current.get(Calendar.HOUR_OF_DAY);
		int m = current.get(Calendar.MINUTE);

		// alarm at time
		if (h == C.get().alarmHM[0] && m == C.get().alarmHM[1]) {
			FServiceHelper.getInstance().playFirst(context);
			LOG.d("Send alarm");
		}

	}

	private void sleepCheck(boolean isPlaying) {
		if (!isPlaying) {
			return;
		}

		if (C.get().sleepMins[0] <= 0) {
			return;
		}

		long dateTime = System.currentTimeMillis();
		Calendar current = Calendar.getInstance();

		if (dateTime > sleepDate) {
			LOG.d("Sleep send pause");
			FServiceHelper.getInstance().pause(context);
		} else {
			current.setTimeInMillis(sleepDate - dateTime);
			sleepRemainMS[0] = current.get(Calendar.MINUTE);
			sleepRemainMS[1] = current.get(Calendar.SECOND);

			LOG.d(String.format("Sleep in %s:%s", current.get(Calendar.MINUTE), current.get(Calendar.SECOND)));
			notification.displayNotifcation(sleepRemainMS);

			if (sleepRemainMS[0] < 0) {
				sleepRemainMS[0] = 0;
			}
		}
	}

	public int[] getSleepRemainMS() {
		return sleepRemainMS;
	}

}
