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
package com.foobnix.api.lastfm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;

import com.foobnix.api.RequestHelper;
import com.foobnix.util.Conf;
import com.foobnix.util.LOG;

public class LastFmApi {

	private RequestHelper requestHelper;

	public LastFmApi(Context contex) {
		requestHelper = new RequestHelper("http://ws.audioscrobbler.com/2.0/");
		BasicNameValuePair api = new BasicNameValuePair("api_key", Conf.LAST_FM_API_KEY);
		requestHelper.setDefaultParam(api);
	}

	public String getResponse(String method, BasicNameValuePair... params) {
		BasicNameValuePair methodParam = new BasicNameValuePair("method", method);
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>(Arrays.asList(params));
		list.add(methodParam);
		return requestHelper.get(list);
	}

	public List<Artist> getTopArtistsByUser(String user) {
		BasicNameValuePair userParam = new BasicNameValuePair("user", user);
		String response = getResponse("user.getTopArtists", userParam);
		response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
		LOG.d(response);

		Serializer serializer = new Persister();

		Lfm lfm = null;
		try {
			lfm = serializer.read(Lfm.class, response);
		} catch (Exception e) {
			new RuntimeException(e);
		}

		if ("fail".equals(lfm.getStatus())) {
			new RuntimeException(lfm.getError().getValue());
		}

		return lfm.getTopArtists().getArtists();
	}

}
