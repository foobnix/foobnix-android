package org.foobnix.android.simple.mediaengine;

import android.content.Intent;
import android.os.IBinder;

public class MediaService extends MediaServiceControls {
    private MediaPlayerCore mediaCore;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(getApplicationContext());
        mediaCore = new MediaPlayerCore(getApplicationContext());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent == null || intent.getAction() == null) {
            return;
        }

        MediaAction mode = MediaAction.valueOf(intent.getAction());

        switch (mode) {
        case PLAY_PAHT:
            String path = (String) intent.getExtras().getString(EXTRA);
            mediaCore.play(path);
            break;
        case SET_PLAYLIST:
            Models models = (Models) intent.getExtras().getSerializable(EXTRA);
            mediaCore.getPlaylistCtr().setPlaylist(models.getItems());
            break;
        case PAUSE:
            mediaCore.pause();
            break;
        case NEXT:
            mediaCore.next();
            break;
        case PREV:
            mediaCore.prev();
            break;
        case PLAY_PAUSE:
            mediaCore.playPause();
            break;

        }
    }
}
