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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {
    private final static String KEY = "FOOBNIX";

    public static void put(Context context, PrefKeys key, String value) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putString(key.name(), value);
        editor.commit();
    }

    public static void put(Context context, PrefKeys key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putBoolean(key.name(), value);
        editor.commit();
    }

    public static String getStr(Context context, PrefKeys key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getString(key.name(), defValue);
    }

    public static String getStr(Context context, PrefKeys key) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getString(key.name(), "");
    }

    public static boolean getBool(Context context, PrefKeys key) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getBoolean(key.name(), false);
    }

}
