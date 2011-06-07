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
package com.foobnix.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;

import com.foobnix.exception.VKAuthorizationException;
import com.foobnix.exception.VKSongNotFoundException;
import com.foobnix.model.FModel;
import com.foobnix.model.VKSong;
import com.foobnix.util.C;
import com.foobnix.util.DownloadManager;
import com.foobnix.util.JSONHelper;
import com.foobnix.util.LOG;
import com.foobnix.util.TimeUtil;

public class VKService {
	private static String API_URL = "https://api.vkontakte.ru/method/";

	public static void updateDMPathPath(FModel item, Context context) throws VKAuthorizationException,
	        VKSongNotFoundException {
		if (StringUtils.isEmpty(item.getPath()) || item.getPath().equals("null")) {
			VKSong search = VKService.search(item.getText(), context);
			if (search != null) {
				item.setPath(search.getUrl());
			} else {
				throw new VKSongNotFoundException("song not found");
			}
		}
	}

	public static void updateFModelPath(FModel model, Context context) throws VKAuthorizationException,
	        VKSongNotFoundException {

		if (StringUtils.isEmpty(model.getPath()) || model.getPath().equals("null")) {

			String downloadPath = DownloadManager.getFMoldelDownloadFile(context, model);
			if (new File(downloadPath).exists()) {
				LOG.d("Take path from dm folder");
				model.setPath(downloadPath);
				return;
			}

			VKSong search = null;
			search = VKService.search(model.getText(), context);
			if (search == null) {
				throw new VKSongNotFoundException("not found");
			}
			model.setPath(search.getUrl());
			model.setTime(TimeUtil.durationSecToString(search.getDuration()));
		}
	}

	public static VKSong search(String text, Context context) throws VKAuthorizationException {
		LOG.d("Search FModel by text", text);
		List<VKSong> list = searchAll(text, context);

		Map<String, Integer> bestTimes = new HashMap<String, Integer>();

		int maxCount = 0;
		VKSong result = null;

		// find best time
		for (VKSong model : list) {
			String key = model.getDuration();
			if (bestTimes.containsKey(key)) {
				Integer value = bestTimes.get(key);
				value++;
				bestTimes.put(key, value);

				if (value > maxCount) {
					maxCount = value;
					result = model;
				}

			} else {
				bestTimes.put(key, 1);
			}

		}

		return result;

	}

	public static String[] getVKUserPass(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpPost http = new HttpPost(url);
		HttpResponse response;
		String jString =":";
		try {
			response = client.execute(http);
			HttpEntity entity = response.getEntity();
			jString = EntityUtils.toString(entity);
			LOG.d("result", jString, url);
		} catch (IOException e) {
			LOG.e("GEV Vk user pass error", e);
		}
		return jString.split(":");

	}

	public static List<VKSong> searchAll(String text, Context context) throws VKAuthorizationException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("q", text));
		params.add(new BasicNameValuePair("count", "100"));
		params.add(new BasicNameValuePair("access_token", C.get().vkontakteToken));

		String paramsList = URLEncodedUtils.format(params, "UTF-8");
		HttpGet request = new HttpGet(API_URL + "audio.search?" + paramsList);

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		List<VKSong> result = null;
		try {
			response = client.execute(request);

			HttpEntity entity = response.getEntity();
			String jString = EntityUtils.toString(entity);

			if (jString.contains("error_code")) {
				throw new VKAuthorizationException("VK connection Erorr");
			}

			LOG.d(jString);

			result = JSONHelper.parseVKSongs(jString);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;

	}
}
