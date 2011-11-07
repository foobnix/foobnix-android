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

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.TYPE;
import com.foobnix.ui.activity.TabActivity;
import com.foobnix.ui.activity.other.DownloadActitivy;
import com.foobnix.ui.adapter.PlaylistAdapter;
import com.foobnix.ui.widget.RunnableDialog;

public class PlaylistActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		onCreateActivity(this, R.layout.search);
		playlistAdapter = new PlaylistAdapter(this, app.getPlayListManager().getAll(), app);
		listView.setAdapter(playlistAdapter);
	}

	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
		FServiceHelper.getInstance().playAtPos(getApplicationContext(), pos);
	}

	@Override
	protected void actionDialog(final FModel item) {
		new RunnableDialog(PlaylistActivity.this, getString(R.string.Action))//

		        .Action(getString(R.string.Clean_Playlist), new Runnable() {

			        @Override
			        public void run() {
				        app.getPlayListManager().clear();
				        playlistAdapter.clear();
			        }
		        })//

		        .Action(getString(R.string.Delete), new Runnable() {
			        @Override
			        public void run() {
				        app.getPlayListManager().remove(item);
				        playlistAdapter.remove(item);
			        }
		        }, item != null)//

		        .Action(getString(R.string.Download), new Runnable() {

			        @Override
			        public void run() {
				        app.addToDownload(item);
				        startActivity(new Intent(PlaylistActivity.this, DownloadActitivy.class));
			        }
		        }, item != null && item.getType() == TYPE.ONLINE)//

		        .Action(getString(R.string.Download_All), new Runnable() {
			        @Override
			        public void run() {
				        List<FModel> items = app.getPlayListManager().getAll();
				        for (FModel current : items) {
					        app.addToDownload(current);
				        }
				        startActivity(new Intent(PlaylistActivity.this, DownloadActitivy.class));
			        }
		        })//

		        .show();
	}

}
