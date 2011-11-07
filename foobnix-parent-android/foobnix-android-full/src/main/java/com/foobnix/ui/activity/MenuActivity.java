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
package com.foobnix.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.foobnix.R;
import com.foobnix.broadcast.BgImageBroadcast;
import com.foobnix.broadcast.FoobnixUIUpdater;
import com.foobnix.broadcast.action.UIBrodcastAction;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.ui.activity.pref.PlayerPreferences;
import com.foobnix.ui.activity.stars.AboutArtistActivity;
import com.foobnix.ui.activity.stars.PlaylistActivity;
import com.foobnix.util.C;
import com.foobnix.util.LOG;

public abstract class MenuActivity extends SubCreateActivity {
	private FoobnixUIUpdater foobnixUIUpdater;
	protected View menuLyaout;
	private BgImageBroadcast bgImageBroadcast;

	public void hideShowMenu() {
		app.setShowMenu(!app.isShowMenu());
		displayImageMenu();
	}

	private void displayImageMenu() {
		if (menuLyaout == null) {
			return;
		}
		if (app.isShowMenu()) {
			menuLyaout.setVisibility(View.VISIBLE);
		} else {
			menuLyaout.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		LOG.d("On menu Press");
		hideShowMenu();
		return false;
	}

	public void onActivateMenuActivity(Activity context) {

		ImageButton playPause = (ImageButton) findViewById(R.id.button_play_pause);
		playPause.setOnClickListener(onPlayPause);

		ImageButton next = (ImageButton) findViewById(R.id.button_next);
		next.setOnClickListener(onNext);

		ImageButton prev = (ImageButton) findViewById(R.id.button_prev);
		prev.setOnClickListener(onPrev);

		SeekBar seekBar = ((SeekBar) findViewById(R.id.playerSeekBar));
		seekBar.setOnSeekBarChangeListener(seekLisner);


		FServiceHelper.getInstance().activateShortTimer(this, true);


		new ButtonImageBindActivity(context, R.id.imageInfo, AboutArtistActivity.class, false);
		new ButtonImageBindActivity(context, R.id.imageSettins, PlayerPreferences.class, false);

		menuLyaout = (View) findViewById(R.id.buttons_override);

		foobnixUIUpdater = new FoobnixUIUpdater(context, app);
		bgImageBroadcast = new BgImageBroadcast(this);
		displayImageMenu();

	}

	OnSeekBarChangeListener seekLisner = new OnSeekBarChangeListener() {
		private int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			this.progress = progress;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			FServiceHelper.getInstance().seekTo(MenuActivity.this, progress);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			FServiceHelper.getInstance().seekTo(MenuActivity.this, progress);
		}

	};

	View.OnClickListener onPrev = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FServiceHelper.getInstance().prev(v.getContext());
		}
	};

	View.OnClickListener onNext = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FServiceHelper.getInstance().next(v.getContext());
		}
	};

	View.OnClickListener onPlayPause = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FServiceHelper.getInstance().playPause(v.getContext());
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(bgImageBroadcast, new IntentFilter(BgImageBroadcast.class.getName()));
		registerReceiver(foobnixUIUpdater, new UIBrodcastAction().getIntentFilter());
		if (bgImageBroadcast != null && app.getCache() != null) {
			bgImageBroadcast.drawBackgound(app.getCache().getDiscCover());
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (bgImageBroadcast != null) {
			unregisterReceiver(bgImageBroadcast);
			unregisterReceiver(foobnixUIUpdater);
		}
		LOG.d("MENU stop");
	}

	class ButtonImageBindActivity {
		public ButtonImageBindActivity(final Context context, int buttonId, final Class activityClazz,
		        final boolean isFinish) {

			if (context.getClass().equals(activityClazz)) {
				return;
			}

			View view = (View) findViewById(buttonId);
			if (view == null) {
				return;
			}
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isFinish) {
						LOG.d("Finish this activity");
						finish();
					}
					startActivity(new Intent(context.getApplicationContext(), activityClazz));
				}
			});
			view.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (MotionEvent.ACTION_DOWN == event.getAction()) {
						v.setBackgroundColor(Color.GRAY);
						LOG.d("on image down");
					} else if (MotionEvent.ACTION_UP == event.getAction()) {
						v.setBackgroundColor(Color.TRANSPARENT);
					}
					return false;
				}

			});
		}

		public ButtonImageBindActivity(final Context context, int buttonId, final Class activityClazz) {
			this(context, buttonId, activityClazz, true);
		}

		public ButtonImageBindActivity(final Context context, int buttonId, final View.OnClickListener onClickLisner) {
			View view = (View) findViewById(buttonId);

			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
					onClickLisner.onClick(v);
				}
			});

			view.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (MotionEvent.ACTION_DOWN == event.getAction()) {
						v.setBackgroundColor(Color.GRAY);
						LOG.d("on image down");
					} else if (MotionEvent.ACTION_UP == event.getAction()) {
						v.setBackgroundColor(Color.TRANSPARENT);
					}
					return false;
				}

			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.getAlarmSleepService().onLastActivation();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		C.get().save(this);
	}

	protected void showPlayer() {
		startActivity(new Intent(this, PlaylistActivity.class));
	}


}
