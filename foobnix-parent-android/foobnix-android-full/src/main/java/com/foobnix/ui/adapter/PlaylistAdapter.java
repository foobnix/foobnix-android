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

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.model.FModel;

public class PlaylistAdapter extends ArrayAdapter<FModel> {
	private Activity context;
	private final List<FModel> models;
	private final FoobnixApplication app;
	
	public PlaylistAdapter(Activity context, List<FModel> FModels, FoobnixApplication app) {
		super(context, -1, FModels);
		this.context = context;
		this.models = FModels;
		this.app = app;
	}

	public void update(List<FModel> items) {
		models.clear();
		models.addAll(items);
		notifyDataSetChanged();
	}

	public void clear(){
		super.clear();
		models.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		FModel FModel = getItem(position);
		FModel.setPosition(position);

		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			newView = inflater.inflate(R.layout.player_item, null);
		} else {
			newView = convertView;
		}
		updateLineView(FModel, newView, false);

		return newView;
	}



	private void updateLineView(FModel model, View view, boolean active) {
		if (view == null) {
			return;
		}
		TextView artist = (TextView) view.findViewById(R.id.pItemArtist);
		TextView title = (TextView) view.findViewById(R.id.pItemTitle);
		TextView num = (TextView) view.findViewById(R.id.pItemNum);
		TextView time = (TextView) view.findViewById(R.id.pItemTime);
		TextView state = (TextView) view.findViewById(R.id.pItemState);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.pItemLayout);

		artist.setText(model.getArtist());

		if (model.equals(app.getNowPlayingSong())) {
			layout.setBackgroundColor(Color.argb(60, 255, 255, 0));
		} else {
			layout.setBackgroundColor(Color.TRANSPARENT);
		}
		title.setText(model.getTitle());

		num.setText("" + (model.getPosition() + 1));
		time.setText(model.getTime());

		if (StringUtils.isEmpty(model.getPath()) || model.getPath().startsWith("http")) {
			state.setText("online");
		} else {
			state.setText("local");
		}
	}

	public List<FModel> getModels() {
	    return models;
    }


}

