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

import java.io.InputStream;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.test.AndroidTestCase;

import com.foobnix.api.lastfm.Artist;
import com.foobnix.api.lastfm.LastFmApi;
import com.foobnix.api.lastfm.Lfm;


public class TestXStream extends AndroidTestCase {

	private LastFmApi api;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		api = new LastFmApi(getContext());
	}

	public void testSimple() {
		List<Artist> artists = api.getTopArtistsByUser("matik");
		assertTrue(artists.size() > 10);
		for (Artist artist : artists) {
			assertNotNull(artist.getName());
			assertNotNull(artist.getRank());
		}
	}

	public void testFile() throws Exception {
		Serializer serializer = new Persister();
		InputStream resourceAsStream = TestXStream.class.getResourceAsStream("demo.xml");
		Lfm lfm = serializer.read(Lfm.class, resourceAsStream);
		assertEquals("ok", lfm.getStatus());

	}

}
