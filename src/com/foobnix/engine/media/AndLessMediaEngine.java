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

import com.foobnix.util.LOG;

public class AndLessMediaEngine implements MediaEngine {
	private int ctx = 0;
	private int mode = AndLessSrv.MODE_CALLBACK;
	private AsyncPlay play;
	private String path;

	public AndLessMediaEngine() {
		AndLessSrv.libInit(Integer.parseInt(Build.VERSION.SDK));
		ctx = AndLessSrv.audioInit(ctx, mode);
	}

	@Override
	public void play(String path) {
		LOG.d("Andless Player paying");

		this.path = path;
		AndLessSrv.audioStop(ctx);

		if (play == null) {
			play = new AsyncPlay(path);
			play.execute();
		} else {
			play.cancel(true);
			play = new AsyncPlay(path);
			play.execute();
		}

	}

	@Override
	public void stop() {
		AndLessSrv.audioStop(ctx);
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

		AsyncPlay(String path) {
			this.path = path;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (path.toLowerCase().endsWith("flac")) {
				AndLessSrv.flacPlay(ctx, path, 0);
			} else if (path.toLowerCase().endsWith("ape")) {
				AndLessSrv.apePlay(ctx, path, 0);
			}
			return null;
		}
	}

	@Override
	public int getDuration() {
		return AndLessSrv.audioGetDuration(ctx);
	}

	@Override
	public int getCurrentPosition() {
		return AndLessSrv.audioGetCurPosition(ctx);
	}

	@Override
	public int getBuffering() {
		return 100;
	}

	@Override
	public void seekTo(int pos) {
		if (path.toLowerCase().endsWith("flac")) {
			AndLessSrv.flacPlay(ctx, path, pos);
		} else if (path.toLowerCase().endsWith("ape")) {
			AndLessSrv.apePlay(ctx, path, pos);
		}

	}

}
