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
package com.foobnix.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.model.FModel;
import com.foobnix.util.LOG;

public class PlayListAdapter extends ArrayAdapter<FModel> {
	private Activity context;
	private List<FModel> models;
	private final Map<Integer, View> cache = new HashMap<Integer, View>();
	
	public PlayListAdapter(Activity context, int textViewResourceId, List<FModel> FModels) {
		super(context, textViewResourceId, FModels);
		this.context = context;
		this.models = FModels;
	}

	public void clear(){
		models.clear();
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
		FModel FModel = models.get(position);
		FModel.setPosition(position);

		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			newView = inflater.inflate(R.layout.player_item, null);
		} else {
			newView = convertView;
		}
		cache.put(position, newView);

		updateLineView(FModel, newView, false);

		return newView;
	}

	public void updateCurrentView(View view, boolean active) {
		if (view == null) {
			return;
		}
		TextView txtView = (TextView) view.findViewById(R.id.pItemNum);
		// LinearLayout txtView = (LinearLayout)
		// view.findViewById(R.id.pItemLayout);

		if (active) {
			txtView.setBackgroundColor(Color.DKGRAY);
		} else {
			txtView.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public void updateCurrentView(int pos, String time) {
		if (models.isEmpty() || pos >= models.size()) {
			return;
		}
		FModel model = models.get(pos);
		if (model == null) {
			return;
		}
		model.setTime(time);
		updateCurrentView(model);
	}

	public void updateCurrentView(FModel model) {
		if (model == null) {
			return;
		}
		clearAll();
		View view = cache.get(model.getPosition());
		// View view = getView(FModel.getPosition(), null, null);

		updateCurrentView(view, true);
		updateLineView(model, view, true);
	}


	private void updateLineView(FModel FModel, View view, boolean active) {
		if (view == null) {
			return;
		}
		TextView artist = (TextView) view.findViewById(R.id.pItemArtist);
		TextView title = (TextView) view.findViewById(R.id.pItemTitle);
		TextView num = (TextView) view.findViewById(R.id.pItemNum);
		TextView time = (TextView) view.findViewById(R.id.pItemTime);
		TextView state = (TextView) view.findViewById(R.id.pItemState);

		artist.setText(FModel.getArtist());
		title.setText(FModel.getTitle());
		num.setText("" + (FModel.getPosition() + 1));
		time.setText(FModel.getTime());
		if (StringUtils.isEmpty(FModel.getPath()) || FModel.getPath().startsWith("http")) {
			state.setText("online");
		} else {
			state.setText("local");

		}
	}

	public static String genarateLongEmptyString(int len) {
		String result = "";
		for (int i = 0; i < len; i++) {
			result += " ";
		}
		return result;
	}

	public List<FModel> getAllFModels() {
	    return models;
    }

}

