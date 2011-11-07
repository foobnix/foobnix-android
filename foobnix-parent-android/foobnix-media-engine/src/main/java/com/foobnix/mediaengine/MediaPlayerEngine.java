package com.foobnix.mediaengine;

import java.io.FileDescriptor;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.foobnix.commons.LOG;

public class MediaPlayerEngine extends MediaPlayer {
    private int buffering;
    private boolean isError;

    public MediaPlayerEngine(final MediaObserver mediaObserver) {

        setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                buffering = percent;
            }
        });

        setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                start();
                if (mediaObserver != null) {
                    mediaObserver.onStart();
                }
                isError = false;

            }
        });

        setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LOG.d("On Error", buffering);
                if (buffering > 5) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }

                    if (!isPlaying()) {
                        if (mediaObserver != null) {
                            mediaObserver.onError();
                        }
                        isError = true;
                    }
                }
                buffering = -1;

                return true;
            }
        });
    }

    public void playPause() {
        if (isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    public void playPath(String path) throws IllegalArgumentException, IllegalStateException, IOException {
        LOG.d("Media Player paying", path);
        buffering = 0;
        isError = false;
        stop();
        reset();
        setDataSource(path);
        setAudioStreamType(AudioManager.STREAM_MUSIC);
        prepareAsync();
        // mediaPlayer.prepare();
    }

    public void playDataSource(FileDescriptor fd) throws IllegalArgumentException, IllegalStateException, IOException {
        buffering = 0;
        isError = false;
        stop();
        reset();
        setDataSource(fd);
        setAudioStreamType(AudioManager.STREAM_MUSIC);
        prepareAsync();

    }


    public void setError(boolean isError) {
        this.isError = isError;
    }

    public boolean isError() {
        return isError;
    }

}