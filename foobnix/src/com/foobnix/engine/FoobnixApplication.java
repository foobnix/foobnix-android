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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;

import com.foobnix.model.FModel;
import com.foobnix.model.FModel.DOWNLOAD_STATUS;
import com.foobnix.model.FModelBuilder;
import com.foobnix.provider.IntegrationsQueryManager;
import com.foobnix.service.FoobnixNotification;
import com.foobnix.service.LastFmService;
import com.foobnix.util.C;
import com.foobnix.util.LOG;
import com.foobnix.util.pref.Pref;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public class FoobnixApplication extends Application {

	private List<FModel> onlineItems = new ArrayList<FModel>();
	private final List<FModel> downloadItems = Collections.synchronizedList(new ArrayList<FModel>());
	private PlaylistManager playListManager;
	private FModel nowPlayingSong = FModelBuilder.Empty();
	private LastFmService lastFmService;
	private FoobnixNotification notification;
	private boolean isPlaying = false;
	private boolean isShowMenu = true;
	private ApplicationCache cache = new ApplicationCache();

	private IntegrationsQueryManager integrationsQueryManager;

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		super.onCreate();
		C.get().load(this);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.enableDefaults();
		}

		notification = new FoobnixNotification(this);
		playListManager = new PlaylistManager(this);
		lastFmService = new LastFmService(this);

		integrationsQueryManager = new IntegrationsQueryManager(getApplicationContext());
		integrationsQueryManager.getVkAdapter().setToken(Pref.getStr(this, Pref.VKONTAKTE_TOKEN, Pref.NULL_TOKEN));

		if (isOnline()) {
			lastFmService.isConnectedAndEnable();

		}

		Caller.getInstance().setCache(new MemoryCache());

	}

	public void cleanDMList() {
		Iterator<FModel> iterator = getDowloadList().iterator();
		while (iterator.hasNext()) {
			FModel item = iterator.next();
			if (item.getStatus() != FModel.DOWNLOAD_STATUS.ACTIVE) {
				iterator.remove();
			}
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		LOG.d("Network", netInfo);
		if (netInfo == null) {
			return false;
		}
		LOG.d(netInfo, netInfo.isConnected());
		return netInfo.isConnected();
	}

	public boolean isDownloadFinished() {
		for (FModel model : downloadItems) {
			if (model.getStatus() == DOWNLOAD_STATUS.ACTIVE) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmptyPlaylist() {
		return playListManager.getAll().isEmpty();
	}

	public void playOnAppend() {
		LOG.d("is playgin", isPlaying);
		if (!isPlaying()) {
			FServiceHelper.getInstance().playFirst(this);
		}
	}

	public void setOnlineItems(List<FModel> onlineItems) {
		this.onlineItems = onlineItems;
	}

	public List<FModel> getOnlineItems() {
		return onlineItems;
	}

	public PlaylistManager getPlayListManager() {
		return playListManager;
	}

	public List<FModel> getDowloadList() {
		return downloadItems;
	}

	public void setNowPlayingSong(FModel nowPlayingFModel) {
		this.nowPlayingSong = nowPlayingFModel;
	}

	public FModel getNowPlayingSong() {
		return nowPlayingSong;
	}

	public LastFmService getLastFmService() {
		return lastFmService;
	}

	public void setNotificationManager(FoobnixNotification notificationManager) {
		this.notification = notificationManager;
	}

	public FoobnixNotification getNotificationManager() {
		return notification;
	}

	public void setNotification(FoobnixNotification notification) {
		this.notification = notification;
	}

	public FoobnixNotification getNotification() {
		return notification;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setShowMenu(boolean isShowMenu) {
		this.isShowMenu = isShowMenu;
	}

	public boolean isShowMenu() {
		return isShowMenu;
	}

	public void setCache(ApplicationCache cache) {
		this.cache = cache;
	}

	public ApplicationCache getCache() {
		return cache;
	}

	public IntegrationsQueryManager getIntegrationsQueryManager() {
		return integrationsQueryManager;
	}
}