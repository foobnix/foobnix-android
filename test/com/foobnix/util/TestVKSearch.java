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

import android.test.ActivityInstrumentationTestCase2;

import com.foobnix.service.VKService;
import com.foobnix.ui.activity.OnlineActivity;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public class TestVKSearch extends ActivityInstrumentationTestCase2<OnlineActivity> {

	public TestVKSearch() {
		super("com.foobnix.online", OnlineActivity.class);
		Caller.getInstance().setCache(new MemoryCache());
	}

	/*
	 * @Ignore public void testSearchResult() throws ClientProtocolException,
	 * IOException, JSONException { List<VKSong> search = null; try { search =
	 * VKService.searchAll("Madonna", getActivity()); } catch
	 * (VKAuthorizationException e) { getActivity().startActivity(new
	 * Intent(getActivity(), VkCheckActivity.class)); } assertNotNull(search);
	 * for (VKSong song : search) { LOG.d(song.getArtist());
	 * assertNotNull(song.getArtist()); assertNotNull(song.getTitle());
	 * assertNotNull(song.getUrl()); assertNotNull(song.getDuration()); } }
	 * 
	 * @Ignore public void testLastFM() { Collection<Track> topTracks =
	 * Artist.getTopTracks("Era", Conf.LAST_FM_API_KEY); for (Track track :
	 * topTracks) { assertNotNull(track.getName()); } }
	 * 
	 * @Ignore public void testSearchSongPath() throws VKAuthorizationException
	 * { VKSong search = VKService.search("Madonna - Sorry", getActivity());
	 * assertNotNull(search); LOG.d(search.getTitle(), search.getDuration()); }
	 */
	public void testVK() {
		String[] vkUserPass = VKService.getVKUserPass("http://android.foobnix.com/vk");
		assertEquals(2, vkUserPass.length);
	}

}
