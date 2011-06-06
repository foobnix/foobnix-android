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

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.ui.activity.info.AboutArtistActivity;
import com.foobnix.ui.activity.info.LyricActivity;

public class InfoActivity extends FoobnixTabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!app.isOnline()) {
			Toast.makeText(this, R.string.Network_not_available, Toast.LENGTH_LONG).show();
			finish();
			startActivity(new Intent(this, FoobnixActivity.class));
			return;
		}

		tabHost.addTab(createTab("artist", "About", android.R.drawable.ic_menu_info_details, AboutArtistActivity.class));
		tabHost.addTab(createTab("lyric", "Lyric", android.R.drawable.ic_menu_manage, LyricActivity.class));
		// tabHost.addTab(createTab("more", "More", R.drawable.ic_menu_manage,
		// MoreActivity.class));
		tabHost.setCurrentTab(0);
	}
}
