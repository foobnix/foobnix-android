/* Copyright (c) 2011 Ivan Ivanenko <ivan.ivanenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
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

import com.foobnix.api.vkontakte.VkAlbum;
import com.foobnix.api.vkontakte.VkApi;
import com.foobnix.api.vkontakte.VkApiCalls;
import com.foobnix.api.vkontakte.VkHelper;
import com.foobnix.model.VKUser;
import com.foobnix.model.VkAudio;
import com.foobnix.util.LOG;

public class TestVkontakteApi extends AndroidTestCase {

	VkApiCalls vkApi;

	protected void setUp() throws Exception {
		vkApi = new VkApi("de9b762590db6cfade32900addded1ed5e5def3def3fa897f1a833aee37acfc");
	};

	public void _testSearchResult() throws JSONException {
		List<VkAudio> search = vkApi.audioSearch("Madonna");
		assertNotNull(search);
		for (VkAudio song : search) {
			assertNotNull(song.getArtist());
			assertNotNull(song.getTitle());
			assertNotNull(song.getUrl());
			assertNotNull(song.getDuration());
		}
	}

	public void testSearchSongPath() throws JSONException {
		VkAudio search = VkHelper.getMostRelevantSong(vkApi.audioSearch("Madonna - Sorry"));
		assertNotNull(search);
		LOG.d(search.getTitle(), search.getDuration());
	}

	public void testGetUserFriends() {
		List<VKUser> users = vkApi.getUserFriends("6851750");
		for (VKUser user : users) {
			assertNotNull(user.getUid());
			assertNotNull(user.getFirstName());
			assertNotNull(user.getLastName());
		}
	}

	public void testUserAudio() throws JSONException {
		List<VkAudio> search = vkApi.getUserAudio("6851750");
		assertNotNull(search);
		for (VkAudio song : search) {
			assertNotNull(song.getArtist());
			assertNotNull(song.getTitle());
			assertNotNull(song.getUrl());
			assertNotNull(song.getDuration());
		}
	}

	public void testGroupAudio() throws JSONException {
		List<VkAudio> search = vkApi.getGroupAudio("5073524");
		assertNotNull(search);
		for (VkAudio song : search) {
			assertNotNull(song.getArtist());
			assertNotNull(song.getTitle());
			assertNotNull(song.getUrl());
			assertNotNull(song.getDuration());
		}
	}

	public void testUserAlbums() throws JSONException {
		List<VkAlbum> search = vkApi.getUserAlbums("6851750");
		assertNotNull(search);
		for (VkAlbum song : search) {
			assertNotNull(song.getTitle());
			assertNotNull(song.getAlbumId());
			assertNotNull(song.getOwnerId());
		}
	}

	public void testGroupAlbums() throws JSONException {
		List<VkAlbum> search = vkApi.getGroupAlbums("3842582");
		assertNotNull(search);
		for (VkAlbum song : search) {
			assertNotNull(song.getTitle());
			assertNotNull(song.getAlbumId());
			assertNotNull(song.getOwnerId());
		}
	}

	public void testGroupAlbumSongs() {
		List<VkAudio> search = vkApi.getGroupAlbumAudio("5073524", "13705923");
		for (VkAudio song : search) {
			assertNotNull(song.getArtist());
			assertNotNull(song.getTitle());
			assertNotNull(song.getUrl());
			assertNotNull(song.getDuration());
		}
	}

	public void testUserAlbumSongs() {
		List<VkAudio> search = vkApi.getUserAlbumAudio("9091358", "16711661");
		for (VkAudio song : search) {
			assertNotNull(song.getArtist());
			assertNotNull(song.getTitle());
			assertNotNull(song.getUrl());
			assertNotNull(song.getDuration());
		}
	}

	public void testGetUserProfile() {
		VKUser user = vkApi.getUserProfile("6851750");
		assertNotNull(user.getUid());
		assertNotNull(user.getFirstName());
		assertNotNull(user.getLastName());
	}
}
