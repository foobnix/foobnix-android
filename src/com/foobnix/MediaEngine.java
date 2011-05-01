package com.foobnix;

import android.media.MediaPlayer;
import android.util.Log;

import com.foobnix.model.Song;

public class MediaEngine {
	private static MediaEngine instanse = new MediaEngine();
	private final MediaPlayer player = new MediaPlayer();
	private Song song;
	
	private PlayerActivity fcontext;

	public MediaEngine() {
		player.setOnCompletionListener(new MediaCompleteLisner());
	}

	public static MediaEngine getInstanse() {
		return instanse;
	}

	public void onNext() {
		fcontext.playNextSong();
	}

	public void start() {
		player.start();
	}

	public void pause() {
		player.pause();
	}

	public void seekTo(int pos) {
		player.seekTo(pos);
	}

	public boolean isPlaying() {
		return player.isPlaying();
	}

	public void play(Song song) {
		this.song = song;

		try {
			player.reset();
			player.setDataSource(song.getPath());
			player.prepare();
			player.start();
		} catch (Exception e) {
			Log.e("MediaEngine", "Music playing", e);
		}
	}

	public MediaPlayer getPlayer() {
	    return player;
    }

	public void setFcontext(PlayerActivity fcontext) {
	    this.fcontext = fcontext;
    }

	public PlayerActivity getFcontext() {
	    return fcontext;
    }

	public void setSong(Song song) {
	    this.song = song;
    }

	public Song getSong() {
	    return song;
    }

	class MediaCompleteLisner implements MediaPlayer.OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer mp) {
			onNext();
		}
	}

}
