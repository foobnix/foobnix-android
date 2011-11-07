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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.foobnix.R;
import com.foobnix.ui.activity.stars.PlaylistActivity;
import com.foobnix.util.C;
import com.foobnix.util.TimeUtil;

public class FoobnixNotification {

	private Context context;
	private NotificationManager notificationManager;
	private String msg;
	private int[] sleepMS;
	private boolean isPlaying;

	public FoobnixNotification(Context context) {
		this.context = context;

		String ns = Context.NOTIFICATION_SERVICE;
		notificationManager = (NotificationManager) context.getSystemService(ns);
	}

	public void displayNotifcation() {
		displayNotifcation(this.msg, this.sleepMS);
	}

	public void displayNotifcation(boolean playing) {
		this.isPlaying = playing;
		displayNotifcation(this.msg, this.sleepMS);
	}

	public void displayNotifcation(String msg) {
		displayNotifcation(msg, this.sleepMS);
	}

	public void displayNotifcation(int[] sleepMS) {
		displayNotifcation(this.msg, sleepMS);
	}

	public void displayNotifcation(String msg, int[] sleepMS) {
		displayNotifcation(msg, sleepMS, C.get().notificationMode);
	}

	public void displayNotifcation(MODE mode) {
		displayNotifcation(this.msg, this.sleepMS, mode);
	}

	public void displayNotifcation(String msg, int[] sleepMS, MODE mode) {
		this.msg = msg;
		this.sleepMS = sleepMS;



		if (mode == MODE.HIDE) {
			notificationManager.cancelAll();
			return;
		}

		int image = R.drawable.foobnix_pause;
		if (isPlaying) {
			image = R.drawable.foobnix;
		}
		
		Notification notification = new Notification(image, msg, System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, PlaylistActivity.class),
		        0);

		String title = "Foobnix";

		if (sleepMS != null && (sleepMS[0] > 0 && sleepMS[1] > 0)) {
			title += String.format(" | S %s", TimeUtil.getTimeFormated(sleepMS));
		}
		if (C.get().alarmHM[0] >= 0) {
			title += String.format(" | A %s", TimeUtil.getTimeFormated(C.get().alarmHM));
		}

		notification.setLatestEventInfo(context, title, msg, contentIntent);

		switch (mode) {
		case AUTO_CANCEL:
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			break;
		case INSISTENT:
			notification.flags |= Notification.FLAG_INSISTENT;
			break;
		case ONGOING_EVENT:
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			break;
		case NO_CLEAR:
			notification.flags |= Notification.FLAG_NO_CLEAR;
			break;

		default:
			break;
		}

		notificationManager.notify(667661, notification);
	}

	public MODE getMode(String str) {
		for (MODE ITEM : MODE.values()) {
			if (ITEM.name().equalsIgnoreCase(str)) {
				return ITEM;
			}
		}
		return MODE.AUTO_CANCEL;
	}

	public void cancelAll() {
		notificationManager.cancelAll();
	}

	public void updateTryIcon(MODE mode) {
		notificationManager.cancelAll();
		displayNotifcation(msg, sleepMS, mode);
	}

	public void updateIconMessage() {
		notificationManager.cancelAll();
		displayNotifcation();
	}

	public void setPlaying(boolean isPlaying) {
	    this.isPlaying = isPlaying;
    }

	public boolean isPlaying() {
	    return isPlaying;
    }

}
