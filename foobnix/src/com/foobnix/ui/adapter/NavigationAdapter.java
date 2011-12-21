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
import com.foobnix.util.FolderUtil;

public class NavigationAdapter extends ArrayAdapter<FModel> {
	private final Activity context;
	private final List<FModel> items;
	private String currentPath;
	private final FoobnixApplication app;

	public NavigationAdapter(Activity context, List<FModel> items, FoobnixApplication app) {
		super(context, -1, items);
		this.context = context;
		this.items = items;
		this.app = app;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final FModel navItem = getItem(position);// items.get(position);

		View newView;
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			newView = inflater.inflate(R.layout.nav_item, null);
		} else {
			newView = convertView;
		}

		TextView text = (TextView) newView.findViewById(R.id.navTxtView);
		if (navItem.isFile()) {
			text.setText(navItem.getText());
		} else {
			if (StringUtils.isNotEmpty(navItem.getAlbum())) {
				if (StringUtils.isNotEmpty(navItem.getYear())) {
					text.setText(String.format("[%s - %s-%s]", navItem.getArtist(), navItem.getYear(),
					        navItem.getAlbum()));
				} else {
					text.setText(String.format("[%s - %s]", navItem.getArtist(), navItem.getAlbum()));
				}
			} else {
				text.setText(String.format("[%s]", navItem.getText()));
			}
		}

		LinearLayout layout = (LinearLayout) newView.findViewById(R.id.navLayout);

		if (navItem.equals(app.getNowPlayingSong())) {
			layout.setBackgroundColor(Color.argb(60, 255, 255, 0));
		} else {
			layout.setBackgroundColor(Color.TRANSPARENT);
		}

		TextView ext = (TextView) newView.findViewById(R.id.navExt);
		ext.setText(navItem.getExt());

		TextView size = (TextView) newView.findViewById(R.id.navSize);
		size.setText(navItem.getSize());

		return newView;
	}

	public void update(String path) {
		currentPath = path;
		items.clear();
		items.addAll(FolderUtil.getNavItemsByPath(path));
		notifyDataSetChanged();
	}

	public void update(List<FModel> curItems) {
		items.clear();
		items.addAll(curItems);
		notifyDataSetChanged();
	}


	public String getCurrentPath() {
	    return currentPath;
    }

	public List<FModel> getItems() {
	    return items;
    }
}
