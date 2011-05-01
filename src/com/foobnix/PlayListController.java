package com.foobnix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.foobnix.db.DataHelper;
import com.foobnix.model.Song;

public class PlayListController {
	private List<Song> playList = new ArrayList<Song>();
	private int active;
	private DataHelper dh;
	private Random random = new Random();

	public PlayListController(Context context) {
		dh = new DataHelper(context);
		active = 0;
	}

	public Song getNextSong() {
		if (active < playList.size() - 1) {
			active++;
		} else {
			active = 0;
		}
		return playList.get(active);
	}

	public Song getRandomSong() {
		active = random.nextInt(playList.size());
		return playList.get(active);
	}

	public Song getPrevSong() {
		if (active > 0) {
			active--;
		} else {
			active = playList.size() - 1;
		}
		return playList.get(active);
	}

	public void setPlayList(List<Song> playList) {
		this.playList = playList;
	}

	public void clear() {
		playList.clear();
		active = 0;
		dh.deleteAll();
	}

	public List<Song> getPlayList() {
		playList = dh.selectAll();
		return playList;
	}

	public void setActive(int active) {
	    this.active = active;
    }

	public int getActive() {
	    return active;
    }

}
