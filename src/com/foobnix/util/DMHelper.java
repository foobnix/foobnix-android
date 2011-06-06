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
package com.foobnix.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.exception.VKSongNotFoundException;
import com.foobnix.model.FModel;
import com.foobnix.model.FModel.DOWNLOAD_STATUS;
import com.foobnix.service.VKService;

public class DMHelper {

	public static String getDownloadPath(Context context) {
		File dir = new File(Conf.getDownloadTo(context));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getPath();
	}


	public static void downloadFModel(Context context, FModel item) throws VKAuthorizationException,
	        VKSongNotFoundException {
		item.setStatus(FModel.DOWNLOAD_STATUS.ACTIVE);
		VKService.updateDMPathPath(item, context);
		download(context, item);
	}

	public static void download(Context context, FModel item) {
		try {
			downloadProccess(context, item);
		} catch (IOException e) {
			item.setStatus(DOWNLOAD_STATUS.FAIL);
		}
	}

	public static String getDownloadPath(Context context, String text) {
		return getDownloadPath(context) + "/" + text + ".mp3";
	}

	public static void downloadProccess(Context context, FModel item) throws IOException {
		item.setStatus(FModel.DOWNLOAD_STATUS.ACTIVE);
		item.setDownloadTo(getDownloadPath(context, item.getText()));

		LOG.d("begin download ", item.getDownloadTo(), item.getText(), item.getPath());

		if (new File(item.getDownloadTo()).exists()) {
			LOG.d("FModel exist", item.getDownloadTo());
			item.setStatus(FModel.DOWNLOAD_STATUS.EXIST);
			return;
		}

		URL url = new URL(item.getPath());
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("GET");
		connect.setDoOutput(true);
		connect.connect();

		FileOutputStream toStream = new FileOutputStream(new File(item.getDownloadTo()));
		InputStream fromStream = connect.getInputStream();

		if (fromStream == null) {
			LOG.d("Null from stream");
			return;
		}

		byte[] buffer = new byte[1024];
		int lenght = 0;
		int size = connect.getContentLength();
		int current = 0;

		while ((lenght = fromStream.read(buffer)) > 0) {
			toStream.write(buffer, 0, lenght);
			current += lenght;
			item.setPercent(current * 100 / size);
		}
		item.setStatus(FModel.DOWNLOAD_STATUS.DONE);
		item.setPercent(100);
		toStream.close();
		fromStream.close();
		LOG.d("end download ", item.getText(), item.getPath(), item.getDownloadTo());
		return;
	}

}
