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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;


public class Conf {
	public final static String FOOBNIX_PREFS = "FOOBNIX_PREFS";

	public final static String LAST_FM_API_KEY = "bca6866edc9bdcec8d5e8c32f709bea1";
	public final static String LAST_FM_API_SECRET = "800adaf46e237805a4ec2a81404b3ff2";
	public final static String VK_APP_ID = "2234333";
	public final static String VK_SECRET = "0kCUFX5mK3McLmkxPHHB";
	
	public static String getDownloadTo(Context context) {
		String def = Environment.getExternalStorageDirectory().getPath() + "/Music/Downloads";
		return PrefUtil.get(context, KEY.DOWNLOAD_TO, def);
	}

	public static String getFoobnixVersion(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.foobnix", 0);
			return String.format("Version: %s Code: %s", info.versionName, info.versionCode);
		} catch (NameNotFoundException e) {
			LOG.e("version", e);
			return "version undefined";
		}
	}

}
