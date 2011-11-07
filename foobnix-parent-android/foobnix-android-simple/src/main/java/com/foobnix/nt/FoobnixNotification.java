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
package com.foobnix.nt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.foobnix.R;
import com.foobnix.activity.PlaylistActivity;

public class FoobnixNotification {

    private Context context;
    private NotificationManager notificationManager;

    public FoobnixNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void displayNotifcation(String title, String msg, MODE mode) {
        if (mode == MODE.HIDE) {
            notificationManager.cancelAll();
            return;
        }

        int image = R.drawable.foobnix_pause;
        // if (isPlaying) {
            image = R.drawable.foobnix;
        // }

        Notification notification = new Notification(image, msg, System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, PlaylistActivity.class), 0);


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
}
