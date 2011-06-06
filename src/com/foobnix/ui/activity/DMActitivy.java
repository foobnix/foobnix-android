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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foobnix.R;
import com.foobnix.engine.FServiceHelper;
import com.foobnix.engine.PlayListManager;
import com.foobnix.model.FModel;
import com.foobnix.ui.adapter.DMAdapter;
import com.foobnix.ui.widget.RunnableDialog;
import com.foobnix.util.Conf;
import com.foobnix.util.FolderUtil;
import com.foobnix.util.SongUtil;

public class DMActitivy extends FoobnixMenuActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm);

		FModel FModel = app.getNowPlayingSong();

		TextView text = (TextView) findViewById(R.id.dmSongTitle);
		Button download = (Button) findViewById(R.id.dmDowload);

		TextView info = (TextView) findViewById(R.id.dmInfo);

		if (FModel != null) {
			download.setOnClickListener(onDownload);
			text.setText(FModel.getText());
		}

		dmAdapter = new DMAdapter(this, app.getDowloadList());

		ListView list = (ListView) findViewById(R.id.dmList);
		list.setOnItemLongClickListener(onDialog);
		list.setOnItemClickListener(onClick);

		list.setAdapter(dmAdapter);
		info.setText(getInfoLine());

		TextView downloadTo = (TextView) findViewById(R.id.dmDownloadTo);
		downloadTo.setText(FolderUtil.normalizePath(Conf.getDownloadTo(this)));

		Button choose = (Button) findViewById(R.id.dmChoose);
		choose.setOnClickListener(onChoose);

		Button refresh = (Button) findViewById(R.id.dmRefresh);
		refresh.setOnClickListener(onRefresh);
	}

	View.OnClickListener onRefresh = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
			startActivity(new Intent(DMActitivy.this, DMActitivy.class));
		}
	};

	OnItemClickListener onClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
			final FModel item = (FModel) adapter.getItemAtPosition(pos);
			Toast.makeText(DMActitivy.this, "info: " + item.getText() + " " + item.getStatus().name(),
			        Toast.LENGTH_SHORT).show();
		}
	};

	View.OnClickListener onChoose = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(DMActitivy.this, MediaActivity.class));
		}
	};

	@Override
	protected void actionDialog(final FModel item) {
		new RunnableDialog(DMActitivy.this, "Download Manager Action")//

		        .Action(getString(R.string.Clean_Playlist), new Runnable() {

			        @Override
			        public void run() {
				        cleanPlayList();
			        }
		        }, item == null)//

		        .Action(getString(R.string.Play), new Runnable() {
			        @Override
			        public void run() {
				        FServiceHelper.getInstance().play(DMActitivy.this, item);
			        }
		        }, item != null)//

		        .Action(getString(R.string.Append), new Runnable() {
			        @Override
			        public void run() {
				        PlayListManager manager = app.getPlayListManager();
				        manager.add(item);
				        app.playOnAppend();
			        }
		        }, item != null)//

		        .Action(getString(R.string.Append_All), new Runnable() {
			        @Override
			        public void run() {
				        PlayListManager manager = app.getPlayListManager();
				        manager.addAll(dmAdapter.getItems());

				        app.playOnAppend();
				        showPlayer();
			        }
		        })//

		        .Action(getString(R.string.Delete), new Runnable() {
			        @Override
			        public void run() {
				        if (item.getStatus() != FModel.DOWNLOAD_STATUS.ACTIVE) {
					        app.getDowloadList().remove(item);
				        } else {
					        Toast.makeText(DMActitivy.this, "Can't clean downloading item" + item.getText(),
					                Toast.LENGTH_SHORT).show();
				        }
				        finish();
				        startActivity(new Intent(DMActitivy.this, DMActitivy.class));
			        }
		        }, item != null)//

		        .Action(getString(R.string.Delete_All), new Runnable() {
			        @Override
			        public void run() {
				        Iterator<FModel> iterator = app.getDowloadList().iterator();
				        while (iterator.hasNext()) {
					        FModel item = iterator.next();
					        if (item.getStatus() != FModel.DOWNLOAD_STATUS.ACTIVE) {
						        iterator.remove();
					        }
				        }
				        finish();
				        startActivity(new Intent(DMActitivy.this, DMActitivy.class));
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
		return String.format("All-%s Done-%s Fail-%s | %.2fGb", FModels.size(), done, fail, SongUtil.getGB(available));
	}

	View.OnClickListener onDownload = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FModel item = app.getNowPlayingSong();
			if (StringUtils.isNotEmpty(item.getText())) {
				app.addToDownload(item);
				ToastShort("Appended to queue: " + item.getTitle());
			}
		}
	};

	private DMAdapter dmAdapter;

	@Override
	public String getActivityTitle() {
		return "Download Manager";
	}

	@Override
	public Class<?> activityClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
