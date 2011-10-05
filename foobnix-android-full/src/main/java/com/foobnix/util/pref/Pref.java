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
package com.foobnix.util.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.foobnix.util.LOG;

public class Pref implements Prefs {

	private final static String KEY = "FOOBNIX";
	private final static int MODE = Context.MODE_PRIVATE;

	public static void putStr(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStr(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		String res = settings.getString(key, "");
		LOG.d("getStr", key, res);
		return res;
	}

	public static String getStr(Context context, String key, String def) {
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		String res = settings.getString(key, def);
		LOG.d("getStr", key, def, res);
		return res;
	}

	public static void putBool(Context context, String key, boolean value) {
		LOG.d("putBool", key, value);
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBool(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		boolean res = settings.getBoolean(key, false);
		LOG.d("getBool", key, res);
		return res;
	}

	public static boolean getBool(Context context, String key, boolean bool) {
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		boolean res = settings.getBoolean(key, bool);
		LOG.d("getBool", key, res);
		return res;
	}

	/**
	 * @param applicationContext
	 * @param downloadMode
	 * @return
	 */
	public static int getInt(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		int res = settings.getInt(key, 0);
		LOG.d("getInt", key, res);
		return res;
	}

	public static void putInt(Context context, String key, int value) {
		LOG.d("putBool", key, value);
		SharedPreferences settings = context.getSharedPreferences(KEY, MODE);
		Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}


}
