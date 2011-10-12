package com.foobnix.mediaengine;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MediaService extends MediaServiceControls {
    private MediaPlayerCore mediaCore;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
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
            MediaModels models = (MediaModels) intent.getExtras().getSerializable(EXTRA);
            mediaCore.getPlaylistCtr().setPlaylist(models.getItems());
            break;
        case SEEK_TO:
            int seekTo = intent.getExtras().getInt(EXTRA);
            mediaCore.seekTo(seekTo);
            break;

        case PLAY_AT_POS:
            int pos = intent.getExtras().getInt(EXTRA);
            mediaCore.playAtPot(pos);
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

    public MediaPlayerCore getMediaCore() {
        return mediaCore;
    }
}


