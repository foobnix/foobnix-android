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
package com.foobnix.engine;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import com.foobnix.util.LOG;

public class WifiLocker {

	private final WifiLock wifiLock;
	private final WifiManager wifiManager;

	public WifiLocker(Context context) {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiLock = wifiManager.createWifiLock("FOOBNIX");
	}

	public void acqire() {
		if (!wifiLock.isHeld()) {
			wifiLock.acquire();
			LOG.d("Wifi LOCK");
		}
	}

	public void release() {
		if (wifiLock.isHeld()) {
			wifiLock.release();
			LOG.d("Wifi RELEASE");
		}
	}

	public WifiManager getWifiManager() {
		return wifiManager;
	}

}
