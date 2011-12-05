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
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.broadcast.BroadCastManager;
import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.commons.string.StringUtils;
import com.foobnix.engine.media.MediaObserver;
import com.foobnix.engine.media.MediaPlayerEngine;
import com.foobnix.exception.VkErrorException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.FModelBuilder;
import com.foobnix.model.VkAudio;
import com.foobnix.service.AlarmSleepService;
import com.foobnix.service.FoobnixNotification;
import com.foobnix.service.LastFmService;
import com.foobnix.ui.activity.pref.VkontakteAccountPreferences;
import com.foobnix.ui.activity.stars.PlaylistActivity;
import com.foobnix.util.C;
import com.foobnix.util.FolderUtil;
import com.foobnix.util.LOG;
import com.foobnix.util.Res;
import com.foobnix.util.SongUtil;
import com.foobnix.util.TimeUtil;
import com.foobnix.util.pref.Pref;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

public class FoobnixMediaCore {

	private MediaPlayerEngine engineManager;
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

		engineManager = new MediaPlayerEngine(mediaObserver);

		notification = app.getNotification();
		broadCastManager = new BroadCastManager(context);
		wifiLocker = new WifiLocker(context);
		alarmSleepService = app.getAlarmSleepService();
		handler = new Handler();

		systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		mobileInfo = systemService.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	}

	MediaObserver mediaObserver = new MediaObserver() {
		@Override
		public void onError() {
			if (Pref.getBool(context, Pref.IS_SKIP_ERRORS, false)) {
				playNext();
			}
		}

		@Override
		public void onStart() {
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

	public void play() {
		engineManager.play();
		app.setPlaying(true);
		handler.post(shortTask);
		handler.postDelayed(longTask, 15000);
		notification.displayNotifcation(true);

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

	public void playFModel(final FModel song) {

		if (song == null) {
			return;
		}
		pause();
		LOG.d(song.getPath());

		song.setScrobbled(false);
		app.setNowPlayingSong(song);

		UIBroadcast stat = new UIBroadcast(song, 0, 0, false, 0, app.getPlayListManager().getAll().size());
		broadCastManager.sendNowPlaying(stat);

		if (song.getType() == TYPE.ONLINE && !app.isOnline()) {
			Toast.makeText(context, R.string.Network_not_available_cant_play_online_song, Toast.LENGTH_LONG).show();
			return;
		} //

		if (StringUtils.isEmpty(song.getPath())) {

			LOG.d("Search most relevant song");
			try {
				VkAudio mostRelevantSong = app.getIntegrationsQueryManager().getVkAdapter()
				        .getMostRelevantSong(song.getText());
				if (mostRelevantSong != null) {
					song.setPath(mostRelevantSong.getUrl());
				} else {
					if (Pref.getBool(context, Pref.IS_SKIP_ERRORS, false)) {
						playNext();
					}
					Toast.makeText(context, R.string.Song_not_found_in_the_Internet_cantt_play, Toast.LENGTH_SHORT)
					        .show();
					return;
				}
			} catch (VkErrorException e) {
				Intent intent = new Intent(context, VkontakteAccountPreferences.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return;
			}

		}

		if (song.getType() != TYPE.ONLINE) {
			song.setTime(TimeUtil.durationToString(engineManager.getDuration()));
		}
		if (song.getType() == TYPE.ONLINE) {
			if (song.getPath().startsWith("http")) {
				int size = SongUtil.getRemoteSize(song.getPath());
				song.setSize(String.format("%.1fM", SongUtil.getMB(size)));
			} else {
				// engineManager.setBuffering(100);
				song.setSize(FolderUtil.getSizeMb(new File(song.getPath())));
			}
		}

		try {
			engineManager.play(song.getPath());
		} catch (Exception e) {
			LOG.e("error playing", e);
		}

		if (isActivate) {
			handler.post(shortTask);
		}
		handler.postDelayed(longTask, 15000);

		broadCastManager.sendNewFModel(song);
		app.setPlaying(true);
		notification.setPlaying(true);
		notification.displayNotifcation(song.getText());

		context.sendBroadcast(new Intent(PlaylistActivity.class.getName()));

		if (bgAsyncTask == null || bgAsyncTask.getStatus() == Status.FINISHED) {
			if (systemService.getActiveNetworkInfo() != null
			        && systemService.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
				bgAsyncTask = new BgAsyncTask();
				bgAsyncTask.execute();
			}
		}

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
			if (engineManager.isError()) {
				stat.setBuffering(-1);
			}

			broadCastManager.sendNowPlaying(stat);

		}
	}

	public void longTimer() {
		FModel model = app.getNowPlayingSong();

		alarmSleepService.checkSleepAlarm(engineManager.isPlaying());

		LOG.d(app.getNowPlayingSong().getType());
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

	private BgAsyncTask bgAsyncTask;
	private ConnectivityManager systemService;
	private NetworkInfo mobileInfo;

	public void pause() {
		handler.removeCallbacks(shortTask);
		handler.removeCallbacks(longTask);
		engineManager.pause();
		app.setPlaying(false);
		shortTimer();
		notification.displayNotifcation(false);

	}

	public void justStop() {
		engineManager.stop();
	}

	public void stop() {
		pause();
		engineManager.stop();
		notification.displayNotifcation(false);
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
		if (app.getNowPlayingSong().equals(FModelBuilder.Empty())) {
			playFirst();
		} else {
			engineManager.playPause();
			notification.displayNotifcation(!app.isPlaying());
			app.setPlaying(!app.isPlaying());
		}

        UIBroadcast stat = new UIBroadcast(app.getNowPlayingSong(), engineManager.getCurrentPosition(),
                engineManager.getDuration(), app.isPlaying(), engineManager.getBuffering(), app.getPlayListManager()
                        .getAll().size());

        broadCastManager.sendNowPlaying(stat);

	}

	public void seekTo(Integer msec) {
		engineManager.seekTo(msec);

	}

	class BgAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				findDiscImageTask();
			} catch (Exception e) {
				LOG.e("get disc image error", e);
			}

			return null;
		}
	};

	private boolean findDiscImageTask() {
		if (!Pref.getBool(context, Pref.IS_BACKGROUND, true)) {
			broadCastManager.sendBgImage("off");
			app.getCache().setDiscCover(null);
			return false;
		}

		FModel song = app.getNowPlayingSong();
		String user = "foobnix";

		if (StringUtils.isNotEmpty(Pref.getStr(context, Pref.LASTFM_USER))) {
			user = Pref.getStr(context, Pref.LASTFM_USER);
		}

		LOG.d("Disc cover for", song.getArtist(), song.getTitle());
		Track track = Track.getInfo(song.getArtist(), song.getTitle(), Res.get(context, R.string.LAST_FM_API_KEY));
		if (track != null) {
			String albumStr = track.getAlbumMbid();
			Album album = Album.getInfo(song.getArtist(), albumStr, Res.get(context, R.string.LAST_FM_API_KEY));
			if (album != null) {
				String url = album.getImageURL(ImageSize.MEGA);
				if (StringUtils.isNotEmpty(url)) {
					broadCastManager.sendBgImage(url);
					return true;
				}
			}
		}

		Artist info = Artist.getInfo(song.getArtist(), Locale.getDefault(), user,
		        Res.get(context, R.string.LAST_FM_API_KEY));
		if (info != null) {
			String imageURL = info.getImageURL(ImageSize.MEGA);
			broadCastManager.sendBgImage(imageURL);
			return true;
		}
		return false;
	}
}
