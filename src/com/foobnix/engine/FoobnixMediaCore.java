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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;

import com.foobnix.broadcast.BroadCastManager;
import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.engine.media.EnginesManager;
import com.foobnix.engine.media.MediaObserver;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.FModelBuilder;
import com.foobnix.service.AlarmSleepService;
import com.foobnix.service.FoobnixNotification;
import com.foobnix.service.LastFmService;
import com.foobnix.util.C;
import com.foobnix.util.Conf;
import com.foobnix.util.FolderUtil;
import com.foobnix.util.LOG;
import com.foobnix.util.SongUtil;
import com.foobnix.util.TimeUtil;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

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

		systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

		// TODO : fix update song
		/*
		 * try { if (song.getType() == TYPE.ONLINE && !app.isOnline()) {
		 * Toast.makeText(context,
		 * R.string.Network_not_available_cant_play_online_song,
		 * Toast.LENGTH_LONG).show(); return; } //
		 * VKontakteApi.updateFModelPath(song, context); } catch
		 * (VKSongNotFoundException e) { Toast.makeText(context,
		 * R.string.Song_not_found_in_the_Internet_cantt_play,
		 * Toast.LENGTH_LONG).show(); return; } catch (VKAuthorizationException
		 * e) { Toast.makeText(context,
		 * R.string.Please_Refresh_Vkontakte_Settings,
		 * Toast.LENGTH_LONG).show(); return; }
		 */

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
			engineManager.playModel(song);
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

		if (async == null || async.getStatus() == Status.FINISHED) {
			if (systemService.getActiveNetworkInfo() != null
			        && systemService.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
				async = new BgAsync();
				async.execute();
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

	private BgAsync async;
	private ConnectivityManager systemService;

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
	}

	public void seekTo(Integer msec) {
		engineManager.seekTo(msec);

	}

	class BgAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (!C.get().isBackground) {
				return null;
			}
			FModel song = app.getNowPlayingSong();
			String user = "l_user_";

			if (StringUtils.isNotEmpty(C.get().lastFmUser)) {
				user = C.get().lastFmUser;
			}
			LOG.d("Disc cover for", song.getArtist(), song.getTitle());
			Track track = Track.getInfo(song.getArtist(), song.getTitle(), Conf.LAST_FM_API_KEY);
			if (track != null) {
				String albumStr = track.getAlbumMbid();
				Album album = Album.getInfo(song.getArtist(), albumStr, Conf.LAST_FM_API_KEY);
				if (album != null) {
					Date releaseDate = album.getReleaseDate();
					if (releaseDate != null) {
						Calendar c = Calendar.getInstance();
						c.setTime(releaseDate);
						app.getNowPlayingSong().setAlbum("" + c.get(Calendar.YEAR) + " " + album.getName());
					} else {
						app.getNowPlayingSong().setAlbum(album.getName());
					}

					String url = album.getImageURL(ImageSize.MEGA);
					if (StringUtils.isNotEmpty(url)) {
						broadCastManager.sendBgImage(url);
						return null;
					}
				}
			}

			Artist info = Artist.getInfo(song.getArtist(), Locale.getDefault(), user, Conf.LAST_FM_API_KEY);
			if (info != null) {
				String imageURL = info.getImageURL(ImageSize.MEGA);
				broadCastManager.sendBgImage(imageURL);

			}
			return null;
		}
	};
}
