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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foobnix.R;
import com.foobnix.model.FModel;
import com.foobnix.util.FolderUtil;

public class DMAdapter extends ArrayAdapter<FModel> {
	private List<FModel> items;
	private Activity context;

	public DMAdapter(Activity context, List<FModel> items) {
		super(context, -1, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		FModel item = items.get(position);
		item.setPosition(position);

		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			newView = inflater.inflate(R.layout.player_item, null);
		} else {
			newView = convertView;
		}

		updateLineView(item, newView, false);

		return newView;
	}

	private void updateLineView(FModel item, View view, boolean active) {
		if (view == null) {
			return;
		}
		TextView artist = (TextView) view.findViewById(R.id.pItemArtist);
		TextView title = (TextView) view.findViewById(R.id.pItemTitle);
		TextView num = (TextView) view.findViewById(R.id.pItemNum);
		TextView time = (TextView) view.findViewById(R.id.pItemTime);
		TextView state = (TextView) view.findViewById(R.id.pItemState);

		artist.setText(FolderUtil.normalizePath(item.getDownloadTo()));
		title.setText(item.getText());
		num.setText("" + (item.getPosition() + 1));
		time.setText("" + item.getPercent() + "%");
		state.setText(StringUtils.capitalize(item.getStatus().name().toLowerCase()));
	}

	public void setItems(List<FModel> items) {
		this.items = items;
	}

	public List<FModel> getItems() {
		return items;
	}

}
