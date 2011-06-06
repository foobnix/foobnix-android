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
package com.foobnix.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.foobnix.engine.FoobnixMediaCore;
import com.foobnix.model.FModel;
import com.foobnix.util.LOG;

public class FoobnixService extends Service {
	public enum SERVICE_ACTION {
		PLAY, PLAY_STATE, PAUSE, PLAY_PAUSE, START, SEEK, NEXT, PREV, RANDOM, KEY, NULL, WAIT, PLAY_FIRST, STOP, IS_ACTIVATE_SHORT_TIMER;
	}

	private FoobnixMediaCore mediaCore;

	@Override
	public void onCreate() {
		mediaCore = new FoobnixMediaCore(getApplication());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent == null) {
			return;
		}

		String action = intent.getAction();

		if (action == null) {
			return;
		}

		SERVICE_ACTION mode = SERVICE_ACTION.valueOf(action);

		if (mode != SERVICE_ACTION.STOP) {
			mediaCore.onLastActivateWithMessage();
		}

		switch (mode) {
		case START:
			mediaCore.getPlayer().start();
			break;

		case PAUSE:
			mediaCore.pause();
			break;

		case WAIT:
			LOG.d("run waite THREAD");
			break;

		case PLAY_PAUSE:
			mediaCore.playPause();
			break;

		case NEXT:
			mediaCore.playNext();
			break;

		case RANDOM:
			mediaCore.playRandom();
			break;

		case PREV:
			mediaCore.playPrev();
			break;

		case SEEK:
			Integer msec = (Integer) intent.getExtras().getSerializable(SERVICE_ACTION.SEEK.toString());
			mediaCore.getPlayer().seekTo(msec);
			break;

		case PLAY:
			FModel model = (FModel) intent.getExtras().getSerializable(SERVICE_ACTION.KEY.toString());
			mediaCore.playFModel(model);
			break;
		case IS_ACTIVATE_SHORT_TIMER:
			boolean isActivate = intent.getExtras().getBoolean(SERVICE_ACTION.IS_ACTIVATE_SHORT_TIMER.name());
			mediaCore.activateShortTimer(isActivate);

			break;

		case STOP:
			mediaCore.stop();
			break;

		case PLAY_STATE:
			mediaCore.playFirst();
			break;

		case PLAY_FIRST:
			mediaCore.playFirst();
			break;

		default:
			Toast.makeText(FoobnixService.this, "Action not defined: " + action, Toast.LENGTH_SHORT).show();
		}
	}

	private final IBinder binder = new LocalBinder();

	public class LocalBinder extends Binder {
		public FoobnixService getService() {
			return FoobnixService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
}
