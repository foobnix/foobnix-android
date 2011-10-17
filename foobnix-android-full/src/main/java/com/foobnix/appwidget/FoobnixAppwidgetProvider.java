package com.foobnix.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.commons.LOG;
import com.foobnix.engine.FServiceHelper;

public class FoobnixAppwidgetProvider extends AppWidgetProvider {
    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        LOG.d("appWidgetIds", appWidgetIds.length);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        Intent nextIntent = new Intent(context, FoobnixAppwidgetProvider.class);
        nextIntent.setAction(ACTION_WIDGET_RECEIVER);
        nextIntent.putExtra("msg", "next");

        Intent prevIntent = new Intent(context, FoobnixAppwidgetProvider.class);
        prevIntent.setAction(ACTION_WIDGET_RECEIVER);
        prevIntent.putExtra("msg", "prev");

        Intent playIntent = new Intent(context, FoobnixAppwidgetProvider.class);
        playIntent.setAction(ACTION_WIDGET_RECEIVER);
        playIntent.putExtra("msg", "play_pause");

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent,
                PendingIntent.FLAG_NO_CREATE);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent,
                PendingIntent.FLAG_NO_CREATE);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent,
                PendingIntent.FLAG_NO_CREATE);

        remoteViews.setOnClickPendingIntent(R.id.button_next, nextPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        remoteViews.setOnClickPendingIntent(R.id.button_prev, prevPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        remoteViews.setOnClickPendingIntent(R.id.button_play_pause, playPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(ACTION_WIDGET_RECEIVER)) {
            String msg = intent.getStringExtra("msg");
            Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

            if ("next".equals(msg)) {
                FServiceHelper.getInstance().next(context);
            } else if ("prev".equals(msg)) {
                FServiceHelper.getInstance().prev(context);
            } else if ("play_pause".equals(msg)) {
                FServiceHelper.getInstance().playPause(context);
            }
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

}
