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
package com.foobnix.api.vkontakte;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.foobnix.api.RequestHelper;
import com.foobnix.model.VKSong;
import com.foobnix.model.VKUser;

public class VkApi {
	private static String API_URL = "https://api.vkontakte.ru/method/";
	private RequestHelper requestHelper;

	public VkApi(String token) {
		requestHelper = new RequestHelper(API_URL);
		requestHelper.setDefaultParam(new BasicNameValuePair("access_token", token));
	}

	public List<VKSong> audioSearch(String text) {
		BasicNameValuePair q = new BasicNameValuePair("q", text);
		BasicNameValuePair count = new BasicNameValuePair("count", "100");

		String json = requestHelper.get("audio.search", q, count);
		List<VKSong> result = GsonResponse.toModels(json, VKSong.class);
		return result;
	}

	public List<VKUser> friendsGet(String uid) {
		BasicNameValuePair uidParam = new BasicNameValuePair("uid", uid);
		BasicNameValuePair fields = new BasicNameValuePair("fields", "uid,first_name,last_name,lists,online");

		String json = requestHelper.get("friends.get", uidParam, fields);
		List<VKUser> result = GsonResponse.toModels(json, VKUser.class);
		return result;
	}

}
