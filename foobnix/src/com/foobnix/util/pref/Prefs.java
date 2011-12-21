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

public interface Prefs {

	String VKONTAKTE_EMAIL = "VKONTAKTE_EMAIL";
	String VKONTAKTE_AUTO = "VKONTAKTE_AUTO";
	String VKONTAKTE_PASS = "VKONTAKTE_PASS";
	String VKONTAKTE_TOKEN = "VKONTAKTE_TOKEN";
	String VKONTAKTE_USER_ID = "VKONTAKTE_USER_ID";

	String LASTFM_USER = "LASTFM_EMAIL";
	String LASTFM_PASS = "LASTFM_PASS";
	String LASTFM_ENABLE = "LASTFM_ENABLE";
	
	String ACTIVE_MEDIA_ACTIVITY = "ACTIVE_MEDIA_ACTIVITY";
	String FOLDER_PREV_PATH = "FOLDER_PREV_PATH";
	String DOWNLOAD_TO = "DOWNLOAD_TO";
	String IS_BACKGROUND = "IS_BACKGRPOND";
	String IS_SKIP_ERRORS = "IS_SKIP_ERRORS";
	String NULL_TOKEN = "NULL_TOKEN";

	String RANDOM = "RANDOM";

	String DOWNLOAD_MODE = "DOWNLOAD_MODE";
	int DOWNLOAD_MODE_COMPLEX = 0;
	int DOWNLOAD_MODE_SIMPLE = 1;

	String PLAYLIST_MODE = "PL_MODE";
	int PLAYLIST_MODE_AUTOMATIC = 0;
	int PLAYLIST_MODE_MANUAL = 1;

}
