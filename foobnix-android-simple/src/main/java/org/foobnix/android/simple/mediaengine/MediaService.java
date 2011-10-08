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
        mediaCore = new MediaPlayerCore(getApplicationContext());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent == null || intent.getAction() == null) {
            return;
        }

        Controls mode = Controls.valueOf(intent.getAction());

        switch (mode) {
        case PLAY_PAHT:
            String path = (String) intent.getExtras().getString(EXTRA_PATH);
            mediaCore.play(path);
            break;
        }
    }
}
