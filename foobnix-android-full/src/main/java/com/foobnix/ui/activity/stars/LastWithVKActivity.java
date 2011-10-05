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

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.ui.activity.TabActivity;
import com.foobnix.ui.activity.pref.VkontakteAccountPreferences;
import com.foobnix.util.LOG;
import com.foobnix.util.SongUtil;
import com.foobnix.util.pref.Pref;

public class LastWithVKActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!app.isOnline()) {
			LOG.d("Offline");
			Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		String token = Pref.getStr(this, Pref.VKONTAKTE_TOKEN, Pref.NULL_TOKEN);
		if (token.equals(Pref.NULL_TOKEN)) {
			finish();
			startActivity(new Intent(this, VkontakteAccountPreferences.class));
			return;
		}

		onCreateActivity(this, R.layout.search);
		listView.setAdapter(navigationAdapter);

		String lastUser = Pref.getStr(this, Pref.LASTFM_USER, "foobnix");
		String vkUserId = Pref.getStr(this, Pref.VKONTAKTE_USER_ID, "not defined");
		LOG.d("vkUserID", vkUserId);
		items.addAll(queryManager.getSearchResult(new SearchQuery(SearchBy.ROOT, lastUser, vkUserId), true));

		LOG.d("on create");
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		Stack<SearchQuery> integrationStack = app.getCache().getIntegrationStack();
		LOG.d("on restart", integrationStack.size());
		queryManager.setStack(integrationStack);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Stack<SearchQuery> stack = queryManager.getStack();
		LOG.d("on stop", stack.size());
		app.getCache().setIntegrationStack(stack);
	}

	@Override
	public void onItemClick(AdapterView<?> a, View arg1, int pos, long arg3) {
		final FModel item = (FModel) a.getItemAtPosition(pos);
		LOG.d("Choose:", item.getText());

		if (item.isFile()) {
			if (Pref.getInt(this, Pref.PLAYLIST_MODE) == Pref.PLAYLIST_MODE_AUTOMATIC) {
				List<FModel> navItesm = navigationAdapter.getItems();
				List<FModel> cleanList = SongUtil.getListWithouFolders(navItesm);
				app.getPlayListManager().setList(cleanList);
				int indexOf = cleanList.indexOf(item);
				FServiceHelper.getInstance().playAtPos(getApplicationContext(), indexOf);
			} else {
				FServiceHelper.getInstance().play(getApplicationContext(), item);
			}
		} else {
			List<FModel> result = queryManager.getSearchResult(item.getSearchQuery());
			LOG.d("Found Items: ", result.size());
			SongUtil.updateType(result, TYPE.ONLINE);
			navigationAdapter.update(result);
			listView.setSelection(0);

		}
	}

	@Override
	public void onBackPressed() {
		List<FModel> result = queryManager.previousPage();

		if (result.equals(Collections.EMPTY_LIST)) {
			finish();
			return;
		}

		LOG.d("Found Items: ", result.size());
		SongUtil.updateType(result, TYPE.ONLINE);
		navigationAdapter.update(result);
		listView.setSelection(0);
	};

	@Override
	public boolean onItemLongClick(final AdapterView<?> adapter, View arg1, final int pos, long arg3) {
		final FModel item = (FModel) adapter.getItemAtPosition(pos);
		actionDialog(item);
		return true;
	}

}
