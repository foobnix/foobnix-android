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
package com.foobnix.ui.activity.stars;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.foobnix.ui.activity.TabActivity;
import com.foobnix.util.pref.Pref;
import com.foobnix.util.pref.Prefs;

public class AllStarTabsActivity extends Activity {
	@SuppressWarnings("unchecked")
	private List<Class<? extends TabActivity>> activities = Arrays.asList(//
	        PlaylistActivity.class,//
	        FolderActivity.class,//
	        SearchActivity.class, //
			LastWithVKActivity.class //
	        );

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String currentClazz = Pref.getStr(this, Prefs.ACTIVE_MEDIA_ACTIVITY, FolderActivity.class.getName());

		finish();

		for (Class<?> clazz : activities) {
			if (clazz.getName().equals(currentClazz)) {
				startActivity(new Intent(this, clazz));
				return;
			}
		}

	}

}
