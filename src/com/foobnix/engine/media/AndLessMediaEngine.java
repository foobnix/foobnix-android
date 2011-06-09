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

import net.avs234.AndLessSrv;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import com.foobnix.util.LOG;

public class AndLessMediaEngine implements MediaEngine {
	private int ctx = 0;
	private int mode = AndLessSrv.MODE_CALLBACK;
	private AsyncPlay play;
	private String path;
	private int pos;
	PlayingThread thread;
	private Handler handler = new Handler();
	private final MediaObserver mediaObserver;

	public AndLessMediaEngine(MediaObserver mediaObserver) {
		this.mediaObserver = mediaObserver;
		AndLessSrv.libInit(Integer.parseInt(Build.VERSION.SDK));
		ctx = AndLessSrv.audioInit(ctx, mode);
	}

	@Override
	public void play(String path) {
		play(path, 0);
	}

	public void play(String path, int pos) {
		this.path = path;
		this.pos = pos;
		LOG.d("Andless Player paying", path, pos);

		stop();

		thread = new PlayingThread();
		thread.execute();
		handler.removeCallbacks(onCompleteCheck);
		handler.postDelayed(onCompleteCheck, 1000);

	}

	Runnable onCompleteCheck = new Runnable() {

		@Override
		public void run() {
			LOG.d("Flac pos: ", getCurrentPosition(), getDuration());
			if (getCurrentPosition() - 1 >= getDuration()) {
				mediaObserver.onComlete();
			}
		
			handler.postDelayed(onCompleteCheck, 1000);
		}
	};

	class PlayingThread extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			ctx = AndLessSrv.audioInit(ctx, mode);

			if (path.toLowerCase().endsWith("flac")) {
				AndLessSrv.flacPlay(ctx, path, pos);
			} else if (path.toLowerCase().endsWith("ape")) {
				AndLessSrv.apePlay(ctx, path, pos);
			}
			return null;
		}
	};

	@Override
	public void stop() {
		AndLessSrv.audioStop(ctx);
		if (thread != null) {
			thread.cancel(true);
			thread = null;
		}
		handler.removeCallbacks(onCompleteCheck);
	}

	@Override
	public void pause() {
		AndLessSrv.audioPause(ctx);
	}

	@Override
	public void play() {
		AndLessSrv.audioResume(ctx);
	}

	@Override
	public boolean isPlaying() {
		return true;
	}

	@Override
	public String[] supportedExts() {
		return new String[] { "flac", "ape" };
	}

	class AsyncPlay extends AsyncTask<Void, Void, Void> {
		private final String path;
		private final int pos;

		AsyncPlay(String path, int pos) {
			this.path = path;
			this.pos = pos;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (path.toLowerCase().endsWith("flac")) {
				AndLessSrv.flacPlay(ctx, path, pos);
			} else if (path.toLowerCase().endsWith("ape")) {
				AndLessSrv.apePlay(ctx, path, pos);
			}
			return null;
		}
	}

	@Override
	public int getDuration() {
		return AndLessSrv.audioGetDuration(ctx) * 1000;
	}

	@Override
	public int getCurrentPosition() {
		return AndLessSrv.audioGetCurPosition(ctx) * 1000;
	}

	@Override
	public int getBuffering() {
		return 100;
	}

	@Override
	public void seekTo(int pos) {
		pos = pos / 1000;
		// play(path, pos);

	}

}
