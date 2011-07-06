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
package com.foobnix.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;

import com.foobnix.R;
import com.foobnix.engine.FoobnixApplication;
import com.foobnix.util.ImageUtil;

public class BgImageBroadcast extends BroadcastReceiver {
	public static String KEY = "KEY";
	private View bgView;
	private final Activity activiy;

	public BgImageBroadcast(Activity activiy) {
		this.activiy = activiy;
		bgView = (View) activiy.findViewById(R.id.baseView);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String url = intent.getStringExtra(KEY);
		if (url == null || bgView == null) {
			return;
		}

		Bitmap bmp = ImageUtil.fetchImage(url);
		drawBackgound(bmp);
		((FoobnixApplication) activiy.getApplication()).getCache().setDiscCover(bmp);
	}

	public void drawBackgound(Bitmap bmp) {
		if (bmp == null || bgView == null) {
			return;
		}
		
		Display display = activiy.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		double k = (double) width / height;

		Bitmap scaled = Bitmap.createScaledBitmap(bmp, width, (int) (height * k), false);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(scaled);
		bitmapDrawable.setGravity(Gravity.TOP);
		bitmapDrawable.setAlpha(80);
		bitmapDrawable.setTileModeY(Shader.TileMode.REPEAT);
		bgView.setBackgroundDrawable(bitmapDrawable);
	}

}
