package com.foobnix;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.SeekBar;

public class ProgressUpdater extends Thread {
	private final PlayerActivity fcontext;

	public ProgressUpdater(PlayerActivity fcontext) {
		this.fcontext = fcontext;
	}

	@Override
	public void run() {
		SeekBar seekBar = fcontext.getSeekBar();
		MediaPlayer player = fcontext.getMediaEngine().getPlayer();

		while (true) {
			if (seekBar != null && player != null && player.isPlaying()) {
				seekBar.setMax(player.getDuration());
				seekBar.setProgress(player.getCurrentPosition());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.e("", "", e);
			}
		}
	}
}

