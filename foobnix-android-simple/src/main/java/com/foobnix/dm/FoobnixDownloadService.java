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
package com.foobnix.dm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.foobnix.commons.log.LOG;
import com.foobnix.mediaengine.MediaModel;


public class FoobnixDownloadService extends Service {
	public enum DM_ACTION {
		ADD
	}

	private DownloadTask dTask;
    private List<MediaModel> dmList = Collections.synchronizedList(new ArrayList<MediaModel>());

	@Override
	public void onCreate() {
		super.onCreate();
		dTask = new DownloadTask();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        dmList.clear();
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
		switch (DM_ACTION.valueOf(action)) {
		case ADD:
            MediaModel item = (MediaModel) intent.getExtras().getSerializable(DM_ACTION.ADD.name());

			if (item.getText().equals("..")) {
				return;
			}

            dmList.add(item);

			LOG.d("STATUS", dTask.getStatus());

			if (dTask.getStatus() == AsyncTask.Status.RUNNING) {
				LOG.d("DT runnig");
				return;
			}

			if (dTask.getStatus() == AsyncTask.Status.FINISHED) {
				dTask = null;
				dTask = new DownloadTask();
				LOG.d("DT new task create");
			}

			if (dTask.getStatus() == AsyncTask.Status.PENDING) {
				dTask.execute();
				LOG.d("DT pending, execute");
			}

			break;
		}
	}

	class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			proccess();
			return null;
		}
	};

	public void proccess() {
        Iterator<MediaModel> iterator = dmList.iterator();
		while (iterator.hasNext()) {
            /*
             * MediaModel item = iterator.next(); if (item.getStatus() ==
             * MediaModel.DOWNLOAD_STATUS.NEW) { try {
             * 
             * DownloadManager.downloadFModel(app, item); } catch
             * (VkErrorException e) { item.setStatus(DOWNLOAD_STATUS.FAIL);
             * return; } catch (VKSongNotFoundException e) {
             * item.setStatus(DOWNLOAD_STATUS.FAIL); }
             * 
             * iterator = app.getDowloadList().iterator(); }
             */
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}