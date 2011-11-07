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

import android.os.Bundle;
import android.view.Window;

import com.foobnix.R;
import com.foobnix.ui.activity.other.DownloadActitivy;
import com.foobnix.ui.activity.stars.FolderActivity;
import com.foobnix.ui.activity.stars.LastWithVKActivity;
import com.foobnix.ui.activity.stars.PlaylistActivity;
import com.foobnix.ui.activity.stars.SearchActivity;
import com.foobnix.util.ActivityHelper;

public class HomeActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		onActivateStarTabs(this, R.layout.home);

		ActivityHelper.bindOnClick(this, R.id.homePlaylist, PlaylistActivity.class);
		ActivityHelper.bindOnClick(this, R.id.homeFolder, FolderActivity.class);


		ActivityHelper.bindOnClick(this, R.id.homeSearch, SearchActivity.class);

		ActivityHelper.bindOnClick(this, R.id.homeLastFm, LastWithVKActivity.class);
		ActivityHelper.bindOnClick(this, R.id.homeVkontakte, LastWithVKActivity.class);
		ActivityHelper.bindOnClick(this, R.id.homeRadio, DownloadActitivy.class);
	}

}
