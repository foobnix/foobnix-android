package org.foobnix.android.simple.mediaengine;

import java.io.IOException;

import android.content.Context;

import com.foobnix.commons.LOG;

public class MediaPlayerCore extends MediaPlayerFeatures {
    public MediaPlayerCore(Context context) {
        super(context);
    }

    public void play(String path) {
        try {
            playPath(path);
        } catch (IllegalArgumentException e) {
            LOG.e(e);
        } catch (IllegalStateException e) {
            LOG.e(e);
        } catch (IOException e) {
            LOG.e(e);
        }

    }
}
