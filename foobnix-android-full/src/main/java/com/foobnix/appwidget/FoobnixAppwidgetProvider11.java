package com.foobnix.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.foobnix.R;
import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.commons.LOG;
import com.foobnix.engine.FServiceHelper;

public class FoobnixAppwidgetProvider11 extends AppWidgetProvider {
    public static String ACTION_WIDGET_PLAY_PAUSE = "ACTION_WIDGET_PLAY_PAUSE";
    public static String ACTION_WIDGET_NEXT = "ACTION_WIDGET_NEXT";
    public static String ACTION_WIDGET_MEDIA_MODEL = "ACTION_WIDGET_MEDIA_MODEL";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_1_1);

        // next
        Intent intent = new Intent(context, FoobnixAppwidgetProvider11.class);
        intent.setAction(ACTION_WIDGET_PLAY_PAUSE);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_button_play_pause, playPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        
        if (action.equals(ACTION_WIDGET_MEDIA_MODEL)) {
            UIBroadcast fmodel = (UIBroadcast) intent.getSerializableExtra(UIBroadcast.class.getName());

            if (fmodel != null) {
                LOG.d("Recive fmodel", fmodel.isPlaying(), fmodel);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_1_1);

                if (fmodel.isPlaying()) {
                    remoteViews.setImageViewResource(R.id.app_button_play_pause, android.R.drawable.ic_media_pause);
                } else {
                    remoteViews.setImageViewResource(R.id.app_button_play_pause, android.R.drawable.ic_media_play);
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
                        FoobnixAppwidgetProvider11.class.getName());
                appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);
            }
        } else if (action.equals(ACTION_WIDGET_PLAY_PAUSE)) {
            FServiceHelper.getInstance().playPause(context);
        }

    }
}
