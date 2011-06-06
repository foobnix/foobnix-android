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
package com.foobnix.engine;

import org.apache.commons.lang.StringUtils;

import android.media.MediaPlayer;
import android.util.Log;

import com.foobnix.model.FModel;
import com.foobnix.util.LOG;

public class MediaPlayerImpl extends MediaPlayer {
	private int buffering;
	private boolean isError;


	public MediaPlayerImpl(final FoobnixMediaCore core) {
		setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				buffering = percent;
			}
		});

		setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				isError = false;
				start();

			}
		});

		setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (!isError) {
					core.playNext();
				}
			}
		});
		setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				LOG.d("Error playing current song");
				isError = true;
				buffering = -1;
				return false;
			}
		});
	}

	public void play(FModel model) {
		buffering = 0;
		if (model == null || StringUtils.isEmpty(model.getPath())) {
			LOG.d("FModel is not set!!!");
			return;
		}

		LOG.d("Playing", model.getText(), model.getPath());
		try {
			stop();
			reset();
			setDataSource(model.getPath());
			prepare();
		} catch (Exception e) {
			Log.e("MediaEngine", "Music playing", e);
		}
	}

	public void setBuffering(int buffering) {
		this.buffering = buffering;
	}

	public int getBuffering() {
		return buffering;
	}


}
