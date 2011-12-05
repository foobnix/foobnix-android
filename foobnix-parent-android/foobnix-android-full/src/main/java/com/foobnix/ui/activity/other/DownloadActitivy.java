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
package com.foobnix.ui.activity.other;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.commons.string.StringUtils;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.DOWNLOAD_STATUS;
import com.foobnix.ui.activity.TabActivity;
import com.foobnix.ui.activity.stars.SearchActivity;
import com.foobnix.ui.adapter.DMAdapter;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.SongUtil;
import com.foobnix.util.memcache.MemCache;

public class DownloadActitivy extends TabActivity {
	private DMAdapter dmAdapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateActivity(this, R.layout.search);

		onActivateMenuActivity(this);

		dmAdapter = new DMAdapter(this, app.getDowloadList());
		dmAdapter.notifyDataSetChanged();
		listView.setAdapter(dmAdapter);

		infoLine = (TextView) findViewById(R.id.infoLine);
		infoLine.setVisibility(View.VISIBLE);
		infoLine.setText(getInfoLine());

		Button refresh = (Button) findViewById(R.id.dmRefresh);
		// refresh.setOnClickListener(onRefresh);

	}

	private Handler handler = new Handler();
	private Runnable task = new Runnable() {

		@Override
		public void run() {
			listView.setSelection(getDownloadingSongPosition());
			dmAdapter.notifyDataSetChanged();
			infoLine.setText(getInfoLine());
			handler.postDelayed(task, MemCache.SEC);
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		handler.post(task);
	}

	@Override
	protected void onStop() {
		super.onStop();
		handler.removeCallbacks(task);
	}

	public int getDownloadingSongPosition() {
		int pos = 0;
		for (FModel model : app.getDowloadList()) {
			if (model.getStatus() == DOWNLOAD_STATUS.ACTIVE) {
				return pos - 1;
			}
			pos++;
		}
		return pos;
	}

	View.OnClickListener onRefresh = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
			startActivity(new Intent(DownloadActitivy.this, DownloadActitivy.class));
		}
	};

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
		// to do nothing
	}

	View.OnClickListener onChoose = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(DownloadActitivy.this, SearchActivity.class));
		}
	};

	protected void actionDialog(final FModel item) {
		new RunnableDialog(DownloadActitivy.this, getString(R.string.Download_Manager_Action))//

		        .Action(getString(R.string.Delete), new Runnable() {
			        @Override
			        public void run() {
				        if (item.getStatus() != FModel.DOWNLOAD_STATUS.ACTIVE) {
					        app.getDowloadList().remove(item);
				        } else {
					        Toast.makeText(DownloadActitivy.this, "Can't clean downloading item" + item.getText(),
					                Toast.LENGTH_SHORT).show();
				        }
				        finish();
				        startActivity(new Intent(DownloadActitivy.this, DownloadActitivy.class));
			        }
		        }, item != null)//

		        .Action(getString(R.string.Delete_All), new Runnable() {
			        @Override
			        public void run() {
				        app.cleanDMList();
				        finish();
				        startActivity(new Intent(DownloadActitivy.this, DownloadActitivy.class));
			        }
		        }).show();
	}

	OnItemLongClickListener onDialog = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> adapter, View arg1, final int pos, long arg3) {
			FModel item = (FModel) adapter.getItemAtPosition(pos);
			actionDialog(item);
			return true;
		}
	};

	public String getInfoLine() {
		int done = 0;
		int fail = 0;
		List<FModel> FModels = app.getDowloadList();
		for (FModel item : FModels) {
			switch (item.getStatus()) {
			case DONE:
				done++;
				break;
			case FAIL:
				fail++;
				break;
			case EXIST:
				done++;
				break;
			}
		}
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double available = (double) stat.getAvailableBlocks() * (double) stat.getBlockSize();
		// double total = (double) stat.getBlockCount() * (double)
		// stat.getBlockSize();
		return String.format("All-%s Done-%s Fail-%s | Free %.2fGb", FModels.size(), done, fail,
		        SongUtil.getGB(available));
	}

	View.OnClickListener onDownload = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FModel item = app.getNowPlayingSong();
			if (StringUtils.isNotEmpty(item.getText())) {
				app.addToDownload(item);
			}
		}
	};
	private TextView infoLine;

}
