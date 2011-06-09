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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.broadcast.action.UIBrodcastAction;
import com.foobnix.broadcast.model.UIBroadcast;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.util.IntentHelper;
import com.foobnix.util.LOG;
import com.foobnix.util.TimeUtil;

public class FoobnixUIUpdater extends BroadcastReceiver {

	private TextView currentTime;
	private TextView totalTime;
	private TextView infoLine;
	private SeekBar seekBar;
	private Button playPause;
	private Activity action;
	private FoobnixApplication app;

	public FoobnixUIUpdater(Activity action, FoobnixApplication app) {
		this.action = action;
		this.app = app;
		currentTime = (TextView) action.findViewById(R.id.playerActiveTimer);
		totalTime = (TextView) action.findViewById(R.id.playerTrackTime);
		seekBar = ((SeekBar) action.findViewById(R.id.playerSeekBar));
		infoLine = (TextView) action.findViewById(R.id.palyerInfoLine);
		playPause = (Button) action.findViewById(R.id.button_play_pause);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		UIBroadcast stat = (UIBroadcast) IntentHelper.getValue(intent, new UIBrodcastAction());
		if (stat == null) {
			return;
		}
		FModel model = stat.getModel();

		LOG.d("stat.getDuration()", stat.getDuration());

		currentTime.setText(TimeUtil.durationToString(stat.getPosition()));
		totalTime.setText(TimeUtil.durationToString(stat.getDuration()));

		seekBar.setMax(stat.getDuration());
		seekBar.setProgress(stat.getPosition());

		action.setTitle(String.format("%s/%s %s", model.getPosition() + 1, stat.getPlaylistLen(), model.getText()));
		infoLine.setText(infoLineStatus(stat));

		if (stat.isPlaying()) {
			playPause.setText("||");
		} else {
			playPause.setText(">");
		}
	}

	public String infoLineStatus(UIBroadcast stat) {
		FModel model = stat.getModel();
		if (model.getType() == TYPE.LOCAL) {
			return String.format("LOCAL | %s | %s | %s", model.getExt(), model.getSize(), model.getTitle());
		}

		if (model.getType() == TYPE.ONLINE) {
			String buffer = stat.getBuffering() + "%";
			if (stat.getBuffering() == -1) {
				buffer = "Erorr";
			}
			return String.format("ONLINE | Buffer: %s | %s | %s", buffer, model.getSize(), stat
			        .getModel().getTitle());
		}

		return stat.getModel().getText();

	}

}
