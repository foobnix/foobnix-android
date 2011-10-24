package com.foobnix.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.foobnix.R;
import com.foobnix.activity.PlaylistActivity;
import com.foobnix.commons.LOG;
import com.foobnix.commons.TimeUtil;
import com.foobnix.mediaengine.MediaEngineState;
import com.foobnix.mediaengine.MediaService;

public class FoobnixAppwidgetProvider41 extends AppWidgetProvider {
    public static String ACTION_WIDGET_PLAY_PAUSE = "ACTION_WIDGET_PLAY_PAUSE";
    public static String ACTION_WIDGET_NEXT = "ACTION_WIDGET_NEXT";
    public static String ACTION_WIDGET_MEDIA_MODEL = "ACTION_WIDGET_MEDIA_MODEL";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_4_1);

        // next
        Intent intent = new Intent(context, FoobnixAppwidgetProvider41.class);
        intent.setAction(ACTION_WIDGET_NEXT);

        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_button_next, nextPendingIntent);

        // open foobnix
        intent = new Intent(context, PlaylistActivity.class);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.image_foobnix, prevPendingIntent);

        // play_pause
        intent = new Intent(context, FoobnixAppwidgetProvider41.class);
        intent.setAction(ACTION_WIDGET_PLAY_PAUSE);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_button_play_pause, playPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(ACTION_WIDGET_NEXT)) {
            MediaService.next();
        } else if (action.equals(ACTION_WIDGET_PLAY_PAUSE)) {
            MediaService.playPause();
        } else if (action.equals(ACTION_WIDGET_MEDIA_MODEL)) {
            // MediaEngineState fmodel = (UIBroadcast)
            // intent.getSerializableExtra(UIBroadcast.class.getName());
            MediaEngineState fmodel = null;
            if (fmodel != null) {
                LOG.d("Recive fmodel", fmodel.isPlaying(), fmodel);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_4_1);
                // remoteViews.setTextViewText(R.id.app_text_line,
                // InfoTextHelper.infoLineStatus(fmodel));
                remoteViews.setTextViewText(R.id.app_text_time, TimeUtil.durationToString(fmodel.getDuration()));

                if (fmodel.isPlaying()) {
                    remoteViews.setImageViewResource(R.id.app_button_play_pause, android.R.drawable.ic_media_pause);
                } else {
                    remoteViews.setImageViewResource(R.id.app_button_play_pause, android.R.drawable.ic_media_play);
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
                        FoobnixAppwidgetProvider41.class.getName());
                appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);
            }
        }

    }
}
