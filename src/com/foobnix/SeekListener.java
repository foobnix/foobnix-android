package com.foobnix;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class SeekListener implements OnSeekBarChangeListener {
	private int progress;
	private PlayerActivity fcontext;

	public SeekListener(PlayerActivity fcontext) {
		this.fcontext = fcontext;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		this.progress = progress;

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		fcontext.getMediaEngine().seekTo(progress);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		fcontext.getMediaEngine().seekTo(progress);
	}
}
