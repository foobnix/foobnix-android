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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.model.SearchBy;
import com.foobnix.model.SearchQuery;
import com.foobnix.ui.activity.TabActivity;
import com.foobnix.ui.activity.other.VkCheckActivity;
import com.foobnix.util.LOG;
import com.foobnix.util.SongUtil;
import com.foobnix.util.pref.Pref;

public class SearchActivity extends TabActivity {

	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (!app.isOnline()) {
			LOG.d("Offline");
			Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		String token = Pref.getStr(this, Pref.VKONTAKTE_TOKEN, Pref.NULL_TOKEN);
		if (token.equals(Pref.NULL_TOKEN)) {
			finish();
			startActivity(new Intent(this, VkCheckActivity.class));
			return;
		}

		onCreateActivity(this, R.layout.search);

		listView.setAdapter(navigationAdapter);

		FModel song = app.getNowPlayingSong();
		items.addAll(queryManager.getSearchResult(new SearchQuery(SearchBy.SEARCH, song.getArtist(), song.getTitle()),
		        true));

		editText = (EditText) findViewById(R.id.searchText);
		editText.setVisibility(View.VISIBLE);
		editText.addTextChangedListener(onTextChange);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Stack<SearchQuery> integrationStack = app.getCache().getSearchStack();
		LOG.d("on restart", integrationStack.size());
		queryManager.setStack(integrationStack);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Stack<SearchQuery> stack = queryManager.getStack();
		LOG.d("on stop", stack.size());
		app.getCache().setSearchStack(stack);
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

	OnEditorActionListener onEdit = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
				        Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				onSearch(v.getText().toString());
				return true;
			}
			return false;
		}
	};

	TextWatcher onTextChange = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence query, int start, int before, int count) {
			onSearch(query.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	private void onSearch(String text) {
		editText.setHint("");
		items.clear();

		text = SongUtil.capitilizeWords(text);
		LOG.d("onKey", text);
		SearchQuery searchQuery = new SearchQuery(SearchBy.SEARCH, text, text);
		items.addAll(queryManager.getSearchResult(searchQuery, true));

		navigationAdapter.notifyDataSetChanged();

	}


	@Override
	public void onItemClick(AdapterView<?> a, View arg1, int pos, long arg3) {
		final FModel item = (FModel) a.getItemAtPosition(pos);
		LOG.d("Choose:", item.getText());

		SearchActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
	public boolean onItemLongClick(final AdapterView<?> adapter, View arg1, final int pos, long arg3) {
		final FModel item = (FModel) adapter.getItemAtPosition(pos);
		if (item.getType() != TYPE.ONLINE) {
			return false;
		}
		actionDialog(item);
		return true;
	}

}
