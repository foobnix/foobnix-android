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

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.foobnix.engine.FoobnixApplication;
import com.foobnix.engine.FoobnixMediaCore;
import com.foobnix.model.FModel;
import com.foobnix.util.LOG;

public class FoobnixService extends Service {
	private FoobnixApplication app;

	public enum SERVICE_ACTION {
		PLAY, PLAY_STATE, PAUSE, PLAY_PAUSE, START, SEEK, NEXT, PREV, RANDOM, KEY, NULL, WAIT, PLAY_FIRST, STOP, IS_ACTIVATE_SHORT_TIMER, PLAY_POSITION;
	}

	private FoobnixMediaCore mediaCore;
	private Handler handler = new Handler();

	@Override
	public void onCreate() {
		app = ((FoobnixApplication) getApplication());
		mediaCore = new FoobnixMediaCore(app);

		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
		registerReceiver(headsetReciever, filter);

		TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tmgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaCore.justStop();
		unregisterReceiver(headsetReciever);
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
			mediaCore.play();
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
			mediaCore.seekTo(msec);
			break;

		case PLAY:
			FModel model = (FModel) intent.getExtras().getSerializable(SERVICE_ACTION.KEY.toString());
			mediaCore.playFModel(model);
			break;

		case PLAY_POSITION:
			int pos = intent.getExtras().getInt(SERVICE_ACTION.KEY.toString());
			mediaCore.playAtPos(pos);
			break;

		case IS_ACTIVATE_SHORT_TIMER:
			boolean isActivate = intent.getExtras().getBoolean(SERVICE_ACTION.IS_ACTIVATE_SHORT_TIMER.name());
			mediaCore.activateShortTimer(isActivate);

			break;

		case STOP:
			mediaCore.stop();
			break;

		case PLAY_STATE:
			mediaCore.play();
			break;

		case PLAY_FIRST:
			mediaCore.playFirst();
			break;

		default:
			Toast.makeText(FoobnixService.this, "Action not defined: " + action, Toast.LENGTH_SHORT).show();
		}
	}

	private PhoneStateListener phoneStateListener = new PhoneStateListener() {
		private boolean needResume;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK) {
				needResume = app.isPlaying();
				mediaCore.pause();
			} else if (state == TelephonyManager.CALL_STATE_IDLE) {
				if (needResume) {
					mediaCore.play();
				}
			}
		}
	};

	private BroadcastReceiver headsetReciever = new BroadcastReceiver() {
		private boolean needResume = false;
		private AtomicBoolean lock = new AtomicBoolean(false);

		@Override
		public void onReceive(Context context, Intent intent) {
			if (isIntentHeadsetRemoved(intent)) {
				if (lock.get()) {
					LOG.d("Skip second time");
					return;
				}
				needResume = app.isPlaying();
				LOG.d("Headphone pause, isplaygin", needResume);
				mediaCore.pause();
				lock.set(true);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						lock.set(false);
					}
				}, 1000);
			} else if (isIntentHeadsetInserted(intent)) {
				LOG.d("Headphone resume", needResume);
				if (needResume) {
					mediaCore.play();
				}
			}
		}

		private boolean isIntentHeadsetInserted(Intent intent) {
			return (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG) && intent.getIntExtra("state", 0) != 0);
		}

		private boolean isIntentHeadsetRemoved(Intent intent) {
			return ((intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG) && intent.getIntExtra("state", 0) == 0) || intent
			        .getAction().equalsIgnoreCase(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
		}
	};

	private final IBinder binder = new LocalBinder();
	private IntentFilter filter;

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
