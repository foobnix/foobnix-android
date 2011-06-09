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

import java.io.File;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.broadcast.BroadCastManager;
import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.engine.media.EnginesManager;
import com.foobnix.engine.media.MediaObserver;
import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.exception.VKSongNotFoundException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.FModelBuilder;
import com.foobnix.service.AlarmSleepService;
import com.foobnix.service.FoobnixNotification;
import com.foobnix.service.LastFmService;
import com.foobnix.service.VKService;
import com.foobnix.util.C;
import com.foobnix.util.FolderUtil;
import com.foobnix.util.LOG;
import com.foobnix.util.SongUtil;
import com.foobnix.util.TimeUtil;

public class FoobnixMediaCore {

	private EnginesManager engineManager;
	private PlayListController playListController;
	private BroadCastManager broadCastManager;
	private LastFmService lastFmService;
	private Context context;
	private FoobnixApplication app;
	private FoobnixNotification notification;
	private WifiLocker wifiLocker;
	private Handler handler;
	private AlarmSleepService alarmSleepService;
	private boolean isActivate = true;

	public FoobnixMediaCore(Context context) {
		this.context = context;
		this.app = ((FoobnixApplication) context);
		playListController = app.getPlayListManager().getPlayListController();
		lastFmService = app.getLastFmService();

		engineManager = new EnginesManager(mediaObserver);

		notification = app.getNotification();
		broadCastManager = new BroadCastManager(context);
		wifiLocker = new WifiLocker(context);
		alarmSleepService = app.getAlarmSleepService();
		handler = new Handler();
	}

	MediaObserver mediaObserver = new MediaObserver() {

		@Override
		public void onError() {
		}

		@Override
		public void onComlete() {
			playNext();
		}
	};

	public void playNext() {
		if (C.get().isRandom) {
			playRandom();
		} else {
			FModel nextFModel = playListController.getNextFModel();
			playFModel(nextFModel);
		}
	}

	public void playFirst() {
		playFModel(playListController.getItem());
	}

	public void playRandom() {
		FModel nextFModel = playListController.getRandomFModel();
		playFModel(nextFModel);
	}

	public void onLastActivateWithMessage() {
		alarmSleepService.onLastActivation();
	}

	public void playAtPos(int pos) {
		FModel model = playListController.getAtPos(pos);
		if (model != null) {
			playFModel(model);
		}
	}

	public void playFModel(FModel model) {
		if (model == null) {
			return;
		}
		LOG.d(model.getPath());


		model.setScrobbled(false);
		app.setNowPlayingSong(model);

		UIBroadcast stat = new UIBroadcast(model, 0, 0, false, 0, app.getPlayListManager().getAll().size());
		broadCastManager.sendNowPlaying(stat);

		try {
			if (model.getType() == TYPE.ONLINE && !app.isOnline()) {
				Toast.makeText(context, R.string.Network_not_available_cant_play_online_song, Toast.LENGTH_LONG).show();
				return;
			}
			VKService.updateFModelPath(model, context);
		} catch (VKSongNotFoundException e) {
			Toast.makeText(context, R.string.Song_not_found_in_the_Internet_cantt_play, Toast.LENGTH_LONG).show();
			return;
		} catch (VKAuthorizationException e) {
			Toast.makeText(context, R.string.Please_Refresh_Vkontakte_Settings, Toast.LENGTH_LONG).show();
			return;
		}

		if (model.getType() != TYPE.ONLINE) {
			model.setTime(TimeUtil.durationToString(engineManager.getDuration()));
		}
		if (model.getType() == TYPE.ONLINE) {
			if (model.getPath().startsWith("http")) {
				int size = SongUtil.getRemoteSize(model.getPath());
				model.setSize(String.format("%.1fM", SongUtil.getMB(size)));
			} else {
				// engineManager.setBuffering(100);
				model.setSize(FolderUtil.getSizeMb(new File(model.getPath())));
			}
		}

		try {
			engineManager.playModel(model);
		} catch (Exception e) {
			LOG.e("error playing", e);
		}

		if (isActivate) {
			handler.post(shortTask);
		}
		handler.postDelayed(longTask, 15000);

		notification.displayNotifcation(model.getText());

		broadCastManager.sendNewFModel(model);


	}

	public void playPrev() {
		FModel FModel = playListController.getPrevFModel();
		playFModel(FModel);
	}

	private void shortTimer() {
		FModel model = app.getNowPlayingSong();

		if (!FModelBuilder.Empty().equals(model)) {
			app.setPlaying(engineManager.isPlaying());
			
			UIBroadcast stat = new UIBroadcast(model, engineManager.getCurrentPosition(), engineManager.getDuration(),
			        engineManager.isPlaying(), engineManager.getBuffering(), app.getPlayListManager().getAll().size());
			broadCastManager.sendNowPlaying(stat);


		}
	}

	public void longTimer() {
		FModel model = app.getNowPlayingSong();

		alarmSleepService.checkSleepAlarm(engineManager.isPlaying());

		if (app.getNowPlayingSong().getType() == TYPE.ONLINE || !app.isDownloadFinished()) {
			wifiLocker.acqire();
		} else {
			wifiLocker.release();
		}

		lastFmService.updateNowPlaying(model);

		if (!model.isScrobbled() && engineManager.getCurrentPosition() > engineManager.getDuration() * 0.85) {
			lastFmService.scrobble(model);
			model.setScrobbled(true);
		}

	}

	private Runnable shortTask = new Runnable() {
		public void run() {
			LOG.d("run ShortTask");
			shortTimer();
			handler.removeCallbacks(shortTask);
			handler.postDelayed(shortTask, 1000);
		}
	};

	private Runnable longTask = new Runnable() {
		public void run() {
			LOG.d("run Long Task");
			longTimer();
			handler.removeCallbacks(longTask);
			handler.postDelayed(longTask, 15000);
		}
	};

	public void pause() {
		handler.removeCallbacks(shortTask);
		handler.removeCallbacks(longTask);
		engineManager.pause();
		shortTimer();

	}

	public void start() {
		// start();
		handler.post(shortTask);
		handler.postDelayed(longTask, 15000);

	}

	public void stop() {
		pause();
		engineManager.stop();
	}

	/**
	 * @param isActivate
	 */
	public void activateShortTimer(boolean flag) {
		this.isActivate = flag;
		if (flag) {
			LOG.d("Activate timer");
			handler.post(shortTask);
		} else {
			LOG.d("Desactivate timer");
			handler.removeCallbacks(shortTask);
		}

	}

	public void playPause() {
		engineManager.playPause();

	}

	public void seekTo(Integer msec) {
		engineManager.seekTo(msec);

	}

}
