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

import android.test.AndroidTestCase;

import com.foobnix.model.FModel;
import com.foobnix.provider.OnlineProvider;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public class TestOnlineProvider extends AndroidTestCase {
    OnlineProvider provider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        provider = new OnlineProvider(getContext());
        Caller.getInstance().setCache(new MemoryCache());
    }

    /*
     * public void testTagTracks() { List<FModel> findUserTopTracks =
     * provider.findTracksByTag("pop"); assertNotNull(findUserTopTracks);
     * assertTrue(findUserTopTracks.size() > 20); }
     */

    public void testGetTopTracks() {
        List<FModel> findUserTopTracks = provider.findUserTopTracks("ivanivanenko");
        assertNotNull(findUserTopTracks);
        assertTrue(findUserTopTracks.size() > 20);

    }

    public void testRecentTracks() {
        List<FModel> findUserTopTracks = provider.findUserRecentTracks("ivanivanenko");
        assertNotNull(findUserTopTracks);
        assertTrue(findUserTopTracks.size() > 5);

    }

}
