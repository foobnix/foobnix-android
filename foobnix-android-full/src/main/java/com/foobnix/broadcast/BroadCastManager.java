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
package com.foobnix.broadcast;

import android.content.Context;
import android.content.Intent;

import com.foobnix.broadcast.action.NewSongBroadcastAction;
import com.foobnix.broadcast.action.UIBrodcastAction;
import com.foobnix.broadcast.model.NewSongBroadcast;
import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.model.FModel;
import com.foobnix.util.IntentHelper;

public class BroadCastManager {

	private Context context;

	public BroadCastManager(Context context) {
		this.context = context;

	}

	public void sendBgImage(String url) {
		if (url == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(BgImageBroadcast.class.getName());
		intent.putExtra(BgImageBroadcast.KEY, url);
		context.sendBroadcast(intent);
	}

	public void sendNowPlaying(UIBroadcast stat) {
		if (stat == null) {
			return;
		}
		Intent i = IntentHelper.createBroadcastIntent(new UIBrodcastAction(), stat);
		context.sendBroadcast(i);
	}

	public void sendNewFModel(FModel FModel) {
		if (FModel == null) {
			return;
		}
		NewSongBroadcast stat = new NewSongBroadcast(FModel);
		Intent i = IntentHelper.createBroadcastIntent(new NewSongBroadcastAction(), stat);
		context.sendBroadcast(i);
	}

}
