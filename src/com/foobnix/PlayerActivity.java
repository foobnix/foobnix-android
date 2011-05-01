package com.foobnix;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.foobnix.cfg.Global;
import com.foobnix.log.LOG;
import com.foobnix.model.Song;
import com.foobnix.navigation.NavigationActivity;

public class PlayerActivity extends Activity {
	private PlayListController playListController;

	private SeekBar seekBar;
	private ListView listView;
	private PlayerAdapter playerAdapter;

	private Button playPause;
	private MediaEngine mediaEngine = MediaEngine.getInstanse();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		mediaEngine.setFcontext(this);

		playListController = new PlayListController(this);
		playerAdapter = new PlayerAdapter(this, R.id.listview_playlist, playListController.getPlayList());

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		Notification notification = new Notification(R.drawable.foobnix, "Foobnix", System.currentTimeMillis());

		Context context = getApplicationContext();

		CharSequence contentTitle = "Foobnix";
		CharSequence contentText = "Foobnix";

		Intent notificationIntent = new Intent(this, PlayerActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		mNotificationManager.notify(1, notification);

		/***************/

		seekBar = ((SeekBar) findViewById(R.id.seekBar1));
		seekBar.setOnSeekBarChangeListener(new SeekListener(this));

		listView = (ListView) findViewById(R.id.listview_playlist);


		listView.setAdapter(playerAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
				Song song = (Song) adapterView.getItemAtPosition(pos);
				playSong(song);
			}
		});

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		playPause = (Button) findViewById(R.id.button_play_pause);
		playPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mediaEngine.isPlaying()) {
					mediaEngine.pause();
				} else {
					mediaEngine.start();
				}

				updateButtons();

			}
		});

		final Button next = (Button) findViewById(R.id.button_next);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				playNextSong();
			}
		});

		final Button prev = (Button) findViewById(R.id.button_prev);
		prev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Song song = playListController.getPrevSong();
				playSong(song);
			}
		});

		final Button back = (Button) findViewById(R.id.button_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), NavigationActivity.class);
				startActivity(intent);
			}
		});

		new ProgressUpdater(this).start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.player_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clearPlayerMenu:
			LOG.d("CLEAR");
			playerAdapter.clear();
			playListController.clear();
			return true;
		case R.id.settingsPlayerMenu:
			return true;
		case R.id.randomPlayerMenu:
			Global.random = !Global.random;
			if (Global.random) {
				item.setTitle("Random OFF");
			} else {
				item.setTitle("Random ON");
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void updateButtons() {
		if (mediaEngine.isPlaying()) {
			playPause.setText("Pause");
		} else {
			playPause.setText("Play");
		}
	}
	public void playSong(Song song) {
		mediaEngine.play(song);
		setTitle(song.getText());
		playerAdapter.updateViews(song.getPosition());
		playListController.setActive(song.getPosition());
		updateButtons();
	}

	public void playNextSong() {
		Song song;
		if (Global.random) {
			song = playListController.getRandomSong();
		} else {
			song = playListController.getNextSong();
		}

		playSong(song);
	}

	public SeekBar getSeekBar() {
		return seekBar;
	}

	public ListView getListView() {
		return listView;
	}

	public void setPlayListController(PlayListController playListController) {
	    this.playListController = playListController;
    }

	public PlayListController getPlayListController() {
	    return playListController;
    }

	public void setMediaEngine(MediaEngine mediaEngine) {
	    this.mediaEngine = mediaEngine;
    }

	public MediaEngine getMediaEngine() {
	    return mediaEngine;
    }

}
