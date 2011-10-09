package org.foobnix.android.simple.mediaengine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

public abstract class MediaServiceControls extends Service {
    protected static String EXTRA = "EXTRA";
    private static Context context;

    public enum MediaAction {
        PLAY_PAHT, PAUSE, NEXT, PREV, SET_PLAYLIST, PLAY_PAUSE
	}

    // any action without extra
    public static void prev() {
        simpleServiceAction(MediaAction.PREV);
    }

    public static void next() {
        simpleServiceAction(MediaAction.NEXT);
    }

    public static void puase() {
        simpleServiceAction(MediaAction.PAUSE);
    }

    public static void playPause() {
        simpleServiceAction(MediaAction.PLAY_PAUSE);
    }

    public static void playPath(String path) {
		Intent service = new Intent(context, MediaService.class);
		service.setAction(MediaAction.PLAY_PAHT.name());
        service.putExtra(EXTRA, path);
		context.startService(service);
	}

    public static void setPlaylist(Models models) {
        Intent service = new Intent(context, MediaService.class);
        service.setAction(MediaAction.SET_PLAYLIST.name());
        service.putExtra(EXTRA, models);
        context.startService(service);
    }

    private static void simpleServiceAction(MediaAction mediaAction) {
        Intent service = new Intent(context, MediaService.class);
        service.setAction(mediaAction.name());
        context.startService(service);
    }

    public static void setContext(Context context) {
        MediaServiceControls.context = context;
    }

}
