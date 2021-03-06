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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.foobnix.model.VkAudio;

public class JSONHelper {

	public static List<VkAudio> parseVKSongs(String jsonString) throws JSONException {
		List<VkAudio> results = new ArrayList<VkAudio>();

		JSONObject jObject = new JSONObject(jsonString);
		JSONArray jResponse = jObject.getJSONArray("response");

		for (int i = 1; i < jResponse.length(); i++) {
			JSONObject jItem = jResponse.getJSONObject(i);
			String aid = jItem.getString("aid");
			String owner_id = jItem.getString("owner_id");
			String artist = jItem.getString("artist");
			String title = jItem.getString("title");
			String duration = jItem.getString("duration");
			String url = jItem.getString("url");
			results.add(new VkAudio(aid, owner_id, artist, title, duration, url));
		}

		return results;

	}

}
