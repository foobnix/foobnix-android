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
package com.foobnix.engine.media;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.foobnix.util.LOG;

public class MediaPlayerEngine implements MediaEngine {
    private MediaPlayer mediaPlayer;
    private int buffering;
	private boolean isError;

    public MediaPlayerEngine(final MediaObserver mediaObserver) {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                buffering = percent;
            }
        });

        mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
				mediaObserver.onStart();
				isError = false;

            }
        });

        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
				LOG.d("On Complete");
                mediaObserver.onComlete();
            }
        });
        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
				LOG.d("On Error", buffering);
				if (buffering > 5) {

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}

					if (!mediaPlayer.isPlaying()) {
						mediaObserver.onError();
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
			play();
		}
	}

    @Override
    public void play(String path) throws IllegalArgumentException, IllegalStateException, IOException {
        LOG.d("Media Player paying", path);
		buffering = 0;
		isError = false;
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.setDataSource(path);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
        // mediaPlayer.prepare();
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();

    }

    @Override
    public void play() {
        mediaPlayer.start();

    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public String[] supportedExts() {
		return new String[] { "mp3", "ogg" };
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();

    }

    @Override
    public int getBuffering() {
        return buffering;
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

	public void setError(boolean isError) {
	    this.isError = isError;
    }

	public boolean isError() {
	    return isError;
    }

}
