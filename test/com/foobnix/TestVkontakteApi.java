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
package com.foobnix;

import java.util.List;

import org.json.JSONException;

import android.test.AndroidTestCase;

import com.foobnix.api.vkontakte.VkApi;
import com.foobnix.api.vkontakte.VkHelper;
import com.foobnix.model.VKSong;
import com.foobnix.model.VKUser;
import com.foobnix.util.LOG;
import com.foobnix.util.Res;

public class TestVkontakteApi extends AndroidTestCase {

	VkApi vkApi;

	protected void setUp() throws Exception {
		vkApi = new VkApi(Res.get(getContext(), R.string.vk_access_token));
	};

	public void testSearchResult() throws JSONException {
		List<VKSong> search = vkApi.audioSearch("Madonna");
		assertNotNull(search);
		for (VKSong song : search) {
			LOG.d(song.getArtist());
			assertNotNull(song.getArtist());
			assertNotNull(song.getTitle());
			assertNotNull(song.getUrl());
			assertNotNull(song.getDuration());
		}
	}

	public void testSearchSongPath() throws JSONException {
		VKSong search = VkHelper.getMostRelevantSong(vkApi.audioSearch("Madonna - Sorry"));
		assertNotNull(search);
		LOG.d(search.getTitle(), search.getDuration());
	}

	public void testGetFriends(){
		List<VKUser> users = vkApi.friendsGet("6851750");
		for (VKUser user : users) {
			assertNotNull(user.getUid());
			assertNotNull(user.getFirstName());
			assertNotNull(user.getLastName());
		}

	}

}
