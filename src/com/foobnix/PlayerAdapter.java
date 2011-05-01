package com.foobnix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foobnix.log.LOG;
import com.foobnix.model.Song;

class PlayerAdapter extends ArrayAdapter<Song> {
	private Activity context;
	private List<Song> songs;
	private final Map<Integer, View> cache = new HashMap<Integer, View>();
	
	private final static String LOGN_EMPTY_STRING = genarateLongEmptyString(100);

	public PlayerAdapter(Activity context, int textViewResourceId, List<Song> songs) {
		super(context, textViewResourceId, songs);
		this.context = context;
		this.songs = songs;
	}

	public void clear(){
		songs.clear();
		cache.clear();
		super.clear();
	}

	public void updateViews(int pos) {
		LOG.d(pos);
		View view = cache.get(pos);
		clearAll();
		updateCurrentView(view, true);
	}

	public void clearAll() {
		for (View view : cache.values()) {
			updateCurrentView(view, false);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		Song song = songs.get(position);
		song.setPosition(position);

		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			newView = inflater.inflate(R.layout.player_item, null);
		} else {
			newView = convertView;
		}
		cache.put(position, newView);

		updateLineView(song, newView, false);

		return newView;
	}

	private void updateCurrentView(View view, boolean active) {
		TextView txtView = (TextView) view.findViewById(R.id.playerText);
		txtView.setHorizontallyScrolling(true);

		if (active) {
			txtView.setBackgroundColor(Color.DKGRAY);
		} else {
			txtView.setBackgroundColor(Color.TRANSPARENT);
		}

	}

	private void updateLineView(Song song, View view, boolean active) {
		TextView txtView = (TextView) view.findViewById(R.id.playerText);
		txtView.setText(String.format("%s \t %s %s", song.getPosition() + 1, song.getText(), LOGN_EMPTY_STRING));
		txtView.setHorizontallyScrolling(true);
	}

	public static String genarateLongEmptyString(int len) {
		String result = "";
		for (int i = 0; i < len; i++) {
			result += " ";
		}
		return result;
	}

}
